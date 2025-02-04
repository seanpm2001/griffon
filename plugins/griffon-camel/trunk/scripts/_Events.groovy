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

/**
 * @author Andres Almiray
 */

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('camel')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-camel-plugin', dirs: "${camelPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('camel', [
        conf: 'compile',
        name: 'griffon-camel-addon',
        group: 'org.codehaus.griffon.plugins',
        version: camelPluginVersion
    ])
    griffonSettings.dependencyManager.addPluginDependency('camel', [
        conf: 'build',
        name: 'griffon-camel-cli',
        group: 'org.codehaus.griffon.plugins',
        version: camelPluginVersion
    ])
}

eventCollectArtifacts = { artifactsInfo ->
    if(!artifactsInfo.find{ it.type == 'route' }) {
        artifactsInfo << [type: 'route', path: 'routes', suffix: 'Route']
    }
}

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{ it.path == 'routes'} ) {
        pathToInfo << [name: 'Camel Routes', path: 'routes', filetype: ['.groovy']]
    }
}
