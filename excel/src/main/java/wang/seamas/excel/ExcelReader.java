package wang.seamas.excel;

import com.sun.istack.internal.NotNull;
import lombok.var;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import wang.seamas.data.*;
import wang.seamas.excel.exception.FileNotCompatibleException;
import wang.seamas.excel.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-12 09:22
 */
public class ExcelReader {

    static final String XLS = ".xls";
    static final String XLSX = ".xlsx";

    Workbook workbook;
    int skipRow = 0;
    boolean hasHeadRow = true;

    public ExcelReader read(@NotNull String fileName, @NotNull String password) throws IOException, GeneralSecurityException {
        try (InputStream stream = new FileInputStream(fileName)) {
            if (fileName.endsWith(XLS)) {
                org.apache.poi.hssf.record.crypto.Biff8EncryptionKey
                        .setCurrentUserPassword(password);
                workbook = WorkbookFactory.create(stream);
            } else if (fileName.endsWith(XLSX)) {
                POIFSFileSystem fileSystem = new POIFSFileSystem(stream);
                EncryptionInfo encryptionInfo = new EncryptionInfo(fileSystem);
                Decryptor decryptor = Decryptor.getInstance(encryptionInfo);
                decryptor.verifyPassword(password);
                workbook = new XSSFWorkbook(decryptor.getDataStream(fileSystem));
            } else {
                throw new FileNotCompatibleException(fileName + " is not excel file");
            }
        }
        return this;
    }

    public ExcelReader read(@NotNull String fileName) throws IOException {
        try (InputStream stream = new FileInputStream(fileName)) {
            if (fileName.endsWith(XLS)) {
                workbook = new HSSFWorkbook(stream);
            } else if (fileName.endsWith(XLSX)) {
                workbook = new XSSFWorkbook(stream);
            } else {
                throw new FileNotCompatibleException(fileName + " is not excel file");
            }
        }
        return this;
    }

    public ExcelReader skipSheetAt(int index) {
        workbook.removeSheetAt(index);
        return this;
    }

    public ExcelReader beforeSheetAt(int index) {
        var total = workbook.getNumberOfSheets();
        for(var i = index; i < total; i++) {
            workbook.removeSheetAt(index);
        }
        return this;
    }

    public ExcelReader afterSheetAt(int index) {
        for(var i = 0; i < index; i++) {
            workbook.removeSheetAt(0);
        }
        return this;
    }

    public ExcelReader onlyAt(int index) {
        return beforeSheetAt(index + 1).afterSheetAt(index);
    }

    public ExcelReader skipSheetWithName(String name) {
        var index = workbook.getSheetIndex(name);
        return skipSheetAt(index);
    }

    public ExcelReader skipRows(int index) {
        skipRow = index;
        return this;
    }

    public ExcelReader useHeadRow(boolean headRow) {
        this.hasHeadRow = headRow;
        return this;
    }

    public DataSet asDataSet() {
        var dataSet = new DataSet();
        for(var index = 0; index < workbook.getNumberOfSheets(); index++) {
            var table = readDataTable(workbook.getSheetAt(index));
            dataSet.addTable(table);
        }
        return dataSet;
    }


    protected DataTable readDataTable(Sheet sheet) {
        DataTable dataTable = new DataTable(sheet.getSheetName());
        int firstRow = sheet.getFirstRowNum();
        firstRow = firstRow > skipRow ? firstRow: skipRow;
        int lastRow = sheet.getLastRowNum();

        int firstCell = sheet.getRow(firstRow).getFirstCellNum();
        int lastCell = sheet.getRow(firstRow).getLastCellNum();

        int contentRowIndex = firstRow;
        if (hasHeadRow) {
            contentRowIndex = contentRowIndex + 1;
            var headRow = sheet.getRow(firstRow);
            var contentRow = sheet.getRow(contentRowIndex);
            for(var index = firstCell; index <= lastCell; index++) {
                var columnName = getStringValue(headRow.getCell(index));
                if (StringUtils.isNullOrEmpty(columnName)) {
                    columnName = "Column" + index;
                }
                DataColumn dataColumn = new DataColumn(columnName, determineType(contentRow.getCell(index)));
                dataTable.addColumns(dataColumn);
            }
        } else {
            var contentRow = sheet.getRow(contentRowIndex);
            for(var index = firstCell; index <= lastCell; index++) {
                DataColumn dataColumn = new DataColumn("Column" + index, determineType(contentRow.getCell(index)));
                dataTable.addColumns(dataColumn);
            }
        }

        for(var rowIndex = contentRowIndex; rowIndex <= lastRow; rowIndex++) {
            DataRow dataRow = dataTable.newRow();
            var contentRow = sheet.getRow(rowIndex);
            for (var index = 0; index <= lastCell - firstCell; index++) {
                dataRow.setColumn(index, getObjectValue(contentRow.getCell(index + firstCell)));
            }
        }
        return dataTable;
    }

    protected Object getObjectValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        var cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case _NONE:
            case BLANK:
            case ERROR:
            default:
                return null;
        }
    }

    protected String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        var cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue() ? "true": "false";
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case _NONE:
            case BLANK:
            case ERROR:
            default:
                return "";
        }
    }

    protected DataType determineType(Cell cell) {
        if (cell == null) {
            return DataType.Default;
        }
        var cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return DataType.String;
            case BOOLEAN:
                return DataType.Boolean;
            case NUMERIC:
                return DataType.Double;
            case FORMULA:
            case _NONE:
            case BLANK:
            case ERROR:
            default:
                return DataType.Default;
        }
    }
}
