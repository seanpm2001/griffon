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

package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.EmailValidator

/**
 * Created by nick.zhu
 */
class EmailValidatorTest extends GroovyTestCase {

    public void testEmailValidation(){
        EmailValidator email = new EmailValidator(this)

        assertTrue("Should be skipped", (boolean) email.call("blah", this, false))
        assertFalse("Should not be a valid email address", (boolean) email.call("blah", this, true))
        assertTrue("Should be a valid email address", (boolean) email.call("blah@somesite.com", this, true))
    }
    
}
