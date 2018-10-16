package ime.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface UriAnnotation
{
    String path();
    String scheme() default "";
    String host() default "";

    /**
     * 是否允许外部跳转
     */
    boolean exported() default true;

    /**
     * 要添加的interceptors
     */
    Class[] interceptors() default {};
}
