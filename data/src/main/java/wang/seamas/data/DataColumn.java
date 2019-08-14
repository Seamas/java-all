package wang.seamas.data;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-12 14:29
 */
public class DataColumn {

    String name;
    DataType dataType;

    // region constructor

    public DataColumn() {
        this("");
    }

    public DataColumn(String name) {
        this(name, DataType.Default);
    }

    public DataColumn(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    // endregion

    // region getter

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    // endregion
}
