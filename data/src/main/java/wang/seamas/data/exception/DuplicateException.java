package wang.seamas.data.exception;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-12 14:43
 */
public class DuplicateException extends RuntimeException {

    public DuplicateException(String message) {
        super(message);
    }
}
