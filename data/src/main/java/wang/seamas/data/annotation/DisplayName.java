package wang.seamas.data.annotation;

import java.lang.annotation.*;

/**
 * @Author: Seamas.Wang
 * @Date: 2019-08-13 13:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(DisplayNameList.class)
@Inherited
public @interface DisplayName {
    String value() default "";
}
