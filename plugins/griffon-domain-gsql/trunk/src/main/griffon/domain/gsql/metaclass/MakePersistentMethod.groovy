/*
 * Copyright 2010 the original author or authors.
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

package griffon.domain.gsql.metaclass

import griffon.core.GriffonApplication
import griffon.core.ArtifactInfo

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import griffon.domain.gsql.GsqlDomainHandler
import griffon.domain.metaclass.AbstractMakePersistentMethod

/**
 * @author Andres Almiray
 */
class MakePersistentMethod extends AbstractMakePersistentMethod {
    private static final Log LOG = LogFactory.getLog(MakePersistentMethod)

    MakePersistentMethod(GsqlDomainHandler domainHandler) {
        super(domainHandler)
    }
}
