package wang.seamas.data.annotation;

import java.lang.annotation.*;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-13 14:05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface DisplayNameList {

    DisplayName[] value();
}
