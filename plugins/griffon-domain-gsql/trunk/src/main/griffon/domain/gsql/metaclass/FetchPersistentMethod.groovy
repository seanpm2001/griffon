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

import griffon.gsql.GsqlHelper
import griffon.domain.gsql.GsqlDomainClassHelper
import griffon.domain.metaclass.AbstractFetchPersistentMethod

/**
 * @author Andres Almiray
 */
class FetchPersistentMethod extends AbstractFetchPersistentMethod {
    private static final Log LOG = LogFactory.getLog(FetchPersistentMethod)

    FetchPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected Object fetch(Object key) {
        def query = GsqlDomainClassHelper.instance.fetchQuery('fetch', domainClass.klass)
        if(query instanceof Closure) query = query(domainClass, key) 
        LOG.trace("> ${domainClass.simpleName}.fetch(${key})")
        LOG.trace(query)

        def instance = null
        GsqlHelper.instance.withSql { sql ->
            def row = sql.firstRow(query, [key])
            if(row) instance = GsqlDomainClassHelper.instance.makeAndPopulateInstance(domainClass.klass, row)
        }
        LOG.trace("< ${domainClass.simpleName}.fetch(${key}) => ${instance}")
        instance
    }
}
