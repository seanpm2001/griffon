/*
 * Copyright 2004-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.transform;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

/**
 * A class annotation used to make a class Comparable by multiple Comparators.
 *
 * @author Andres Almiray
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@GroovyASTTransformationClass("org.codehaus.griffon.compiler.support.SortableASTTransformation")
public @interface Sortable {
    /**
     * Property names to include in the comparison algorithm
     */
    String[] includes() default {};

    /**
     * Property names to exclude in the comparison algorithm
     */
    String[] excludes() default {};
}