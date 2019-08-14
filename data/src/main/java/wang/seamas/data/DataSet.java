package wang.seamas.data;

import lombok.var;
import wang.seamas.data.exception.ExceptionBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-12 14:29
 */
public class DataSet {

    String name;
    List<DataTable> tables;

    public DataSet() {
        this("NewDataSet");
    }

    public DataSet(String name) {
        this.name = name;
        this.tables = new ArrayList<DataTable>();
    }

    public DataTable[] getTables() {
        var tbs = new DataTable[tables.size()];
        return tables.toArray(tbs);
    }

    public DataTable getTable(int index) {
        return tables.get(index);
    }

    public DataTable getTable(String name) {
        for (var table : tables) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    public void addTable(DataTable table) {
        for (var tab : tables) {
            if (tab.getName().equals(table.getName())) {
                throw ExceptionBuilder.buildDuplicateException(tab.getName());
            }
        }
        tables.add(table);
    }

    public void addRange(Collection<? extends DataTable> all) {
        for (var tab: all) {
            addTable(tab);
        }
    }
}
