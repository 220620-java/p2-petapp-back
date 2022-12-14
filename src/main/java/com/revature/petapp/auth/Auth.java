package com.revature.petapp.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation that flags a controller method as requiring authentication.
 * @author SierraNicholes
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {
	String requiredRole() default "none";
}
