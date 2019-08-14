package wang.seamas.data.util;

import lombok.var;
import wang.seamas.data.annotation.DisplayName;
import wang.seamas.data.annotation.DisplayNameList;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-13 14:38
 */
public class FieldUtil {

    static ConcurrentMap<Class, Map<String, Field>> map;

    static {
        map = new ConcurrentHashMap<>();
    }

    public static Map<String, Field> findDisplayFields(Class tClass) {
        if (!map.containsKey(tClass)) {
            Map<String, Field> currentMap = new HashMap<>();
            var fields = getAllFields(tClass);
            for(var field : fields) {
                // 字段本名 和 DisplayName 不允许重复
                currentMap.put(field.getName(), field);
                var displayName = field.getAnnotation(DisplayName.class);
                if (displayName != null) {
                    currentMap.put(displayName.value(), field);
                }

                var displayNameList = field.getAnnotation(DisplayNameList.class);
                if (displayNameList != null) {
                    for(var display: displayNameList.value()) {
                        // displayNameList中名称，可以允许重复，如果与之前的字段名相同，则忽略
                        if (!currentMap.containsKey(display.value())) {
                            currentMap.put(display.value(), field);
                        }
                    }
                }
            }
            map.putIfAbsent(tClass, currentMap);
        }
        return map.get(tClass);
    }

    static List<Field> getAllFields(Class tClass) {
        var list = new ArrayList<Field>();
        while (tClass != Object.class) {
            var fields = tClass.getDeclaredFields();
            list.addAll(Arrays.asList(fields));
            tClass = tClass.getSuperclass();
        }

        return list;
    }
}
