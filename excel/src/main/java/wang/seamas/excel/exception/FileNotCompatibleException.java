package wang.seamas.excel.exception;

import java.io.IOException;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-13 09:49
 */
public class FileNotCompatibleException extends IOException {

    public FileNotCompatibleException(String message) {
        super(message);
    }
}
