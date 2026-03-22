package io.github.finoid.snapshots.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mask sensitive or dynamic fields in snapshots using JSON Path expressions.
 */
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Masks.class)
public @interface Mask {
    /**
     * The JSON Path expression to mask. If applied to a field, this can be empty to mask the field itself.
     * @return the JSON Path expression
     */
    String value() default "";

    /**
     * The value to use for redaction.
     * @return the mask value
     */
    String maskValue() default "***REDACTED***";
}
