/*
 * Copyright 2010 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.gvalidation.swing

import griffon.spock.UnitSpec

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorIconFactorySpec extends UnitSpec {

    def 'Factory should be able to create icon'(){
        ErrorIconFactory factory = new ErrorIconFactory()

        when:
        def icon = factory.newInstance(null, null, null, null)

        then:
        icon != null
        icon instanceof ErrorIcon
    }

}
