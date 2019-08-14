package wang.seamas.data.util;

import lombok.var;
import wang.seamas.data.DataTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-13 14:08
 */
public class TableUtil {

    public static <T> List<T> convertToList(DataTable dataTable, Class<T> tClass) throws IllegalAccessException, InstantiationException {
        Map<Integer, Field> map = buildMap(dataTable, tClass);

        List<T> list = new ArrayList<>();
        for(var dataRow: dataTable.getRows()) {
            T obj = tClass.newInstance();
            for(var entity: map.entrySet()) {
                var index = entity.getKey();
                var field = entity.getValue();

                var value = dataRow.getColumn(index);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(obj, value);
            }
            list.add(obj);
        }
        return list;
    }

    private static <T> Map<Integer, Field> buildMap(DataTable dataTable, Class<T> tClass) {
        Map<Integer, Field> map = new HashMap<>(dataTable.getColumnsCount());
        var columns = dataTable.getColumns();

        Map<String, Field> fieldMap = FieldUtil.findDisplayFields(tClass);
        for(var index = 0; index < columns.length; index++) {
            var column = columns[index];
            var columnName = column.getName();
            if (fieldMap.containsKey(columnName)) {
                map.put(index, fieldMap.get(columnName));
            }
        }

        return map;
    }
}
