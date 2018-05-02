package javaposse.jobdsl.dsl

import java.lang.annotation.ElementType
import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Indicates that a method should not be included in the API documentation.
 *
 * @since 1.38
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface NoDoc {
	/**
	 * If {@code true}, the method will only be shown in the embedded API viewer.
	 *
	 * @return {@code true}, the method will only be shown in the embedded API viewer
	 * @since 1.58
	 */
	boolean embeddedOnly() default false
}
