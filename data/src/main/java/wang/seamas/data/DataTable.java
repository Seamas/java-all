package wang.seamas.data;

import lombok.var;
import wang.seamas.data.exception.ExceptionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-12 14:29
 */
public class DataTable {
    String name;
    List<DataColumn> columns;
    List<DataRow> rows;

    public DataTable() {
        this("NewTable");
    }

    public DataTable(String name) {
        this.name = name;

        this.columns = new ArrayList<DataColumn>();
        this.rows = new ArrayList<DataRow>();
    }

    public void addColumns(DataColumn column) {
        for(var col: columns) {
            if (col.name.equals(column.name)) {
                throw ExceptionBuilder.buildDuplicateException(column.name);
            }
        }
        columns.add(column);
        for(var dataRow : rows) {
            dataRow.setColumn(column.getName(), null);
        }
    }

    public DataRow newRow() {
        var dataRow = new DataRow(this);
        rows.add(dataRow);
        return dataRow;
    }

    public DataColumn[] getColumns() {
        DataColumn[] dataColumns = new DataColumn[columns.size()];
        return columns.toArray(dataColumns);
    }

    public int getColumnsCount() {
        return columns.size();
    }

    public DataRow[] getRows() {
        DataRow[] dataRows = new DataRow[rows.size()];
        return rows.toArray(dataRows);
    }

    public int getRowsCount() {
        return rows.size();
    }

    public String getName() {
        return name;
    }



}
