package wang.seamas.data.exception;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-12 14:44
 */
public class ExceptionBuilder {

    public static RuntimeException buildDuplicateException(String name) {
        return new DuplicateException("duplicate name of " + name);
    }

    public static RuntimeException buildIndexOutOfRangeException(int currentIndex, int capacity) {
        return new IndexOutOfRangeException("index out of range, current index is " + currentIndex + " but only " + capacity + " items found");
    }
}
