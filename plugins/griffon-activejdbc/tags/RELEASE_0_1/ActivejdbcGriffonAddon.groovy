/*
 * Copyright 2011 the original author or authors.
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

import griffon.core.GriffonApplication
import griffon.activejdbc.ActivejdbcConnector
import griffon.activejdbc.BaseHolder

/**
 * @author Andres Almiray
 */
class ActivejdbcGriffonAddon {
    void addonInit(GriffonApplication app) {
        ConfigObject config = ActivejdbcConnector.instance.createConfig(app)
        ActivejdbcConnector.instance.connect(app, config)
    }

    def events = [
        ShutdownStart: { app ->
            ConfigObject config = ActivejdbcConnector.instance.createConfig(app)
            ActivejdbcConnector.instance.disconnect(app, config)
        },
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.activejdbc?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            def mc = app.artifactManager.findGriffonClass(klass).metaClass
            mc.withActivejdbc = ActivejdbcConnector.instance.withActivejdbc
            mc.withActiveSql = ActivejdbcConnector.instance.withActiveSql
        }
    ]

    def exportWithJmx = { exporter, domain, ctx ->
        exporter.beans."${domain}:service=activeJdbcDatasource,type=configuration" = BaseHolder.instance.dataSource
    }
}
