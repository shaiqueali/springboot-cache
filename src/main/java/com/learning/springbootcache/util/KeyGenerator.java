package com.learning.springbootcache.util;

import java.lang.reflect.Method;

/**
 * https://youlearncode.com/spring-boot-cache/
 * 5.3 Creating a Custom KeyGenerator for Cache Key
 * As you can infer, this attribute is mutually exclusive with the key attribute of the previous section.
 *
 * In our specific case, meaning the getCarsWithPriceFilter method, the target will be the CarService object,
 * the method will be the signature of the method and the params will be the value passed to the method.
 */

@FunctionalInterface
public interface KeyGenerator {

    /**
     * Generate a key for the given method and its parameters.
     * @param target the target instance
     * @param method the method being called
     * @param params the method parameters (with any var-args expanded)
     * @return a generated key
     */
    Object generate(Object target, Method method, Object... params);
}
