/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sourceforge.gvalidation

import net.sourceforge.gvalidation.models.AnnotatedModel
import net.sourceforge.gvalidation.models.ModelBeanWithCallback

/**
 * Created by nick.zhu
 */
class BeforeValidationCallbackTest extends BaseTestCase {

    public void testBeforeValidationCallback() {
        def model = generateModel('ModelBeanWithCallback.groovy')

        assertFalse "Callback should not have been invoked", model.callbackInvoked

        model.validate()

        assertTrue "Callback should have been invoked", model.callbackInvoked
    }

    public void testBeforeValidationWithoutCallback() {
        def model = generateModel()

        model.validate()

        assertNotNull "Errors should have been generated", model.errors
    }

}
