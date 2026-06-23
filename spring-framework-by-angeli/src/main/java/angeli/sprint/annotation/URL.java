package angeli.sprint.annotation;

public @interface URL {
    String value() default "";
    String method() default "GET";
}
