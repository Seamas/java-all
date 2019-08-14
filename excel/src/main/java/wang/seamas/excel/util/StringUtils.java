package wang.seamas.excel.util;

import lombok.var;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-09 16:08
 */
public class StringUtils {

    public static Boolean startWithIgnoreCase(String source, String prefix) {
        return source.toUpperCase().startsWith(prefix.toUpperCase());
    }

    public static String toCamelCase(String source) {
        return toCamelCase(source, true);
    }

    public static String toCamelCase(String source, Boolean pascal) {
        source = source.trim();
        var temp = new char[source.length()];
        int i = 0;
        var capital = pascal;
        for(char c: source.toCharArray()) {
            if (c >= '0' && c <= '9') {
                temp[i++] = c;
                capital = true;
            } else if (c >= 'A' && c <= 'Z') {
                temp[i++] = capital ? c : (char)(c + 32);
                capital = false;
            } else if (c >= 'a' && c <= 'z') {
                temp[i++] = capital ? (char)(c - 32) : c;
                capital = false;
            } else {
                capital = true;
            }
        }
        return new String(temp, 0, i);
    }

    public static boolean isNullOrEmpty(String source) {
        return source == null || source.length() == 0;
    }
}
