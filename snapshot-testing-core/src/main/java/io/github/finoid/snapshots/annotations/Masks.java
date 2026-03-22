package io.github.finoid.snapshots.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container for repeatable {@link Mask} annotation.
 */
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Masks {
    Mask[] value();
}
