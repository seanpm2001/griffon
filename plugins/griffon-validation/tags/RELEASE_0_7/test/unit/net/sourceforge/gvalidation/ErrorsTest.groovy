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

/**
 * Created by nick.zhu
 */
class ErrorsTest extends GroovyTestCase {

    public void testRemoveFieldError() {
        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode")
        errors.rejectValue("field2", "errorCode")

        assertTrue "Should have error", errors.hasFieldErrors('field')
        assertTrue "Should have error", errors.hasFieldErrors('field2')

        errors.removeError('field')

        assertFalse "Should not have error", errors.hasFieldErrors('field')
        assertTrue "Should have error", errors.hasFieldErrors('field2')
    }

    public void testHasErrors() {
        Errors errors = new Errors()

        errors.reject("errorCode")

        assertTrue "Should have error", errors.hasErrors()

        errors.clear()

        assertFalse "Should not have any error", errors.hasErrors()

        errors.rejectValue("field", "errorCode")

        assertTrue "Should have error", errors.hasErrors()
    }

    public void testRejectCausePropertyChangedEvent() {
        boolean fired = false

        def parent = [:] as Expando

        parent.metaClass.firePropertyChange << {property, oldValue, newValue -> fired = true; assertEquals "errors", property}

        Errors errors = new Errors(parent)
        errors.reject("errorCode")

        assertTrue "Should have fired property changed event", fired
    }

    public void testRejectValueCausePropertyChangedEvent() {
        boolean fired = false

        def parent = [:] as Expando

        parent.metaClass.firePropertyChange << {property, oldValue, newValue -> fired = true; assertEquals "errors", property}

        Errors errors = new Errors(parent)
        errors.rejectValue('testField', "errorCode")

        assertTrue "Should have fired property changed event", fired
    }

    public void testClearCausePropertyChangedEvent() {
        boolean fired = false

        def parent = [:] as Expando

        parent.metaClass.firePropertyChange << {property, oldValue, newValue -> fired = true; assertEquals "errors", property}

        Errors errors = new Errors(parent)
        errors.clear()

        assertTrue "Should have fired property changed event", fired
    }

    public void testRejectIgnoreNonObservableParent() {
        Errors errors = new Errors([] as Expando)

        errors.reject("errorCode")
    }

    public void testReject() {
        Errors errors = new Errors()

        errors.reject("errorCode")

        assertTrue "Should have global error", errors.hasGlobalErrors()
    }

    public void testRejectWithDefaultMessage() {
        Errors errors = new Errors()

        errors.reject("errorCode", "defaultErrorCode")

        assertTrue "Should have global error", errors.hasGlobalErrors()
        errors.each {
            assertEquals "Default error code is not correct", "defaultErrorCode", it.defaultErrorCode
        }
    }

    public void testRejectWithArgs() {
        Errors errors = new Errors()

        errors.reject("errorCode", [10, "arg2"])

        def globalErrors = errors.getGlobalErrors()

        assertEquals "Error code is incorrect", globalErrors.first().errorCode, "errorCode"
        assertEquals "Error args is incorrect", 10, globalErrors.first().arguments[0]
        assertEquals "Error args is incorrect", "arg2", globalErrors.first().arguments[1]
    }

    public void testRejectWithArgsAndDefaultMessage() {
        Errors errors = new Errors()

        errors.reject("errorCode", "defaultErrorCode", [10, "arg2"])

        def globalErrors = errors.getGlobalErrors()

        assertEquals "Error code is incorrect", globalErrors.first().errorCode, "errorCode"
        assertEquals "Default error code is incorrect", globalErrors.first().defaultErrorCode, "defaultErrorCode"
        assertEquals "Error args is incorrect", 10, globalErrors.first().arguments[0]
        assertEquals "Error args is incorrect", "arg2", globalErrors.first().arguments[1]
    }

    public void testRejectValue() {
        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode")

        assertTrue "Should have field error", errors.hasFieldErrors()
        assertTrue "Field should have error", errors.hasFieldErrors("field")
    }

    public void testRejectValueWithDefaultMessage() {
        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode", "defaultErrorCode")

        assertTrue "Should have field error", errors.hasFieldErrors()
        assertTrue "Field should have error", errors.hasFieldErrors("field")
        errors.each {
            assertEquals "Default error message is not set", "defaultErrorCode", it.defaultErrorCode
        }
    }

    public void testRejectValueWithArgs() {
        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode", [10, "arg2"])

        def fieldError = errors.getFieldError("field")

        assertEquals "Field error field value is incorrect", fieldError.field, "field"
        assertEquals "Error code is incorrect", fieldError.errorCode, "errorCode"
        assertEquals "Field error args is incorrect", fieldError.arguments[0], 10
        assertEquals "Field error args is incorrect", fieldError.arguments[1], "arg2"
    }

    public void testRejectValueWithArgsAndDefaultMessage() {
        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode", "defaultErrorCode", [10, "arg2"])

        def fieldError = errors.getFieldError("field")

        assertEquals "Field error field value is incorrect", fieldError.field, "field"
        assertEquals "Error code is incorrect", fieldError.errorCode, "errorCode"
        assertEquals "Default error code is incorrect", fieldError.defaultErrorCode, "defaultErrorCode"
        assertEquals "Field error args is incorrect", fieldError.arguments[0], 10
        assertEquals "Field error args is incorrect", fieldError.arguments[1], "arg2"
    }

    public void testIterator() {
        def pass = false

        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode")

        errors.each {
            if ("errorCode" == it.errorCode)
                pass = true
        }

        assertTrue "Should have passed", pass
    }

    public void testMultipleFieldReject() {
        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode1")
        errors.rejectValue("field", "errorCode2")

        def fieldErrors = errors.getFieldErrors("field")

        assertEquals("Multiple field errors should have been generated", 2, fieldErrors.size())
    }

    public void testMultipleGlobalReject() {
        Errors errors = new Errors()

        errors.reject("errorCode1")
        errors.reject("errorCode2")

        def globalErrors = errors.getGlobalErrors()

        assertEquals("Multiple field errors should have been generated", 2, globalErrors.size())
    }

    public void testErrorListenerMgmt(){
        Errors errors = new Errors()

        def listener1 = [name:'listener1'] as ErrorListener
        def listener2 = [name:'listener2'] as ErrorListener

        errors.addListener(listener1)

        assertTrue('Listener is not added',errors.hasListener(listener1))
        assertFalse('Listener should not be added',errors.hasListener(listener2))

        errors.removeListener(listener1)

        assertFalse('Listener should have been removed',errors.hasListener(listener1))
    }

    public void testNewFieldErrorNotification(){
        Errors errors = new Errors()

        def onFieldErrorAdded = false

        def listener = [onFieldErrorAdded: {FieldError error ->
            onFieldErrorAdded = true
            assertEquals('Error field is incorrect', 'email', error.getField())
        }] as ErrorListener

        errors.addListener(listener)

        errors.rejectValue('email', 'emailErrorCode')

        assertTrue('New field error was not notified', onFieldErrorAdded)
    }

    public void testRemoveFieldErrorNotification(){
        Errors errors = new Errors()

        def onFieldErrorRemoved = false

        def listener = [onFieldErrorRemoved: {FieldError error ->
            onFieldErrorRemoved = true
            assertEquals('Error field is incorrect', 'email', error.getField())
        }] as ErrorListener

        errors.rejectValue('email', 'emailErrorCode')

        errors.addListener(listener)

        errors.removeError('email')

        assertTrue('Remove field error was not notified', onFieldErrorRemoved)
    }


}
