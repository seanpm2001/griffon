/*
 * Copyright 2009-2010 the original author or authors.
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

includeTargets << griffonScript("Init")
includePluginScript("clojure", "_ClojureCommon")

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('clojure')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-clojure-plugin', dirs: "${clojurePluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('clojure', [
        conf: 'compile',
        name: 'griffon-clojure-addon',
        group: 'org.codehaus.griffon.plugins',
        version: clojurePluginVersion
    ])
}

eventCompileStart = {
    if(compilingPlugin('clojure')) return
    compileClojureSrc()
}

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{ it.path == "src.commons"} ) {
        pathToInfo << [name: "Common Sources", path: "src.commons", filetype: [".groovy",".java"]]
    }
    // TODO -- match multiline comments -> (comment ...)
    if(!pathToInfo.find{ it.path == "src.clojure"} ) {
        def EMPTY = /^\s*$/
        pathToInfo << [name: "Clojure Sources", path: "src.clojure", filetype: [".clj"], locmatcher: {file ->
            def loc = 0
            file.eachLine { line ->
                if(line ==~ EMPTY || line ==~ /^\s*\;.*/) return
                loc++
            }
            loc
        }]
    }
}
