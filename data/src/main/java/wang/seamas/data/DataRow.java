package wang.seamas.data;

import lombok.var;
import wang.seamas.data.exception.ExceptionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-12 14:29
 */
public class DataRow {

    DataTable table;
    List<Object> values;

    public DataRow(DataTable table) {
        this.table = table;
        values = new ArrayList<Object>();
    }

    public DataColumn[] getColumns() {
        return this.table.getColumns();
    }

    public void setColumn(String name, Object object) {
        for (var index = 0; index < this.table.columns.size(); index++) {
            if (this.table.columns.get(index).name.equals(name)) {
                setColumn(index, object);
                return;
            }
        }
    }

    public void setColumn(int index, Object object) {
        if (index >= this.table.columns.size()) {
            throw ExceptionBuilder.buildIndexOutOfRangeException(index, this.table.columns.size());
        }

        var column = this.table.columns.get(index);
        values.add(index, column.dataType.convert(object));
    }

    public Object getColumn(String name) {
        for (var index = 0; index < this.table.columns.size(); index++) {
            if (this.table.columns.get(index).name.equals(name)) {
                return getColumn(index);
            }
        }
        return null;
    }

    public Object getColumn(int index) {
        if (index >= this.table.columns.size()) {
            throw ExceptionBuilder.buildIndexOutOfRangeException(index, this.table.columns.size());
        }
        return values.get(index);
    }
}
