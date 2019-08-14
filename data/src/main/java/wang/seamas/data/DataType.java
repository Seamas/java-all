package wang.seamas.data;

import java.sql.Timestamp;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-12 14:29
 */
public enum DataType {
    String() {
        @Override
        Object convert(Object object) {
            return object == null ? null : object.toString();
        }
    },
    Int() {
        @Override
        Object convert(Object object) {
            return object == null ? null : Integer.valueOf(object.toString());
        }
    },
    Long() {
        @Override
        Object convert(Object object) {
            return object == null ? null : java.lang.Long.valueOf(object.toString());
        }
    },
    Double() {
        @Override
        Object convert(Object object) {
            return object == null ? null : java.lang.Double.valueOf(object.toString());
        }
    },
    Date() {
        @Override
        Object convert(Object object) {
            if (object == null) {
                return null;
            }

            String value = object.toString();
            if (value.matches("\\d")) {
                java.lang.Long l = java.lang.Long.valueOf(value);
                Timestamp ts = new Timestamp(l);
                java.util.Date date = ts;
                return date;
            } else {
                return java.util.Date.parse(value);
            }
        }
    },
    Boolean() {
        @Override
        Object convert(Object object) {
            return object == null ? null : java.lang.Boolean.valueOf(object.toString());
        }
    },
    Byte() {
        @Override
        Object convert(Object object) {
            return object == null ? null : java.lang.Byte.valueOf(object.toString());
        }
    },
    Default;

    Object convert(Object object) {
        return object;
    }
}
