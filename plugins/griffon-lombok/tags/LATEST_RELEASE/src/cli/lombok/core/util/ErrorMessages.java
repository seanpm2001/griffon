/*
 * Created on Dec 6, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package lombok.core.util;

import java.lang.annotation.Annotation;

/**
 * Common error messages.
 *
 * @author Alex Ruiz
 */
public final class ErrorMessages {
    public static String canBeUsedOnClassOnly(Class<? extends Annotation> annotationType) {
        return errorMessage("@%s can be used on classes only", annotationType);
    }

    public static String canBeUsedOnFieldOnly(Class<? extends Annotation> annotationType) {
        return errorMessage("@%s can be used on fields only", annotationType);
    }

    private static String errorMessage(String format, Class<? extends Annotation> annotationType) {
        return String.format(format, annotationType.getName());
    }

    private ErrorMessages() {
    }
}
