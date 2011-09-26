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
 * Gant script that creates a Clojure class
 * 
 * @author Andres Almiray
 * @author Jeff Brown
 *
 * @since 0.3
 */

import griffon.util.GriffonUtil

includeTargets << griffonScript("Init")
includeTargets << griffonScript("CreateIntegrationTest")

target('createClojureClass': "Creates a new Clojure script") {
    depends(checkVersion, parseArguments)
    promptForName(type: "Class")
    def (pkg, name) = extractArtifactName(argsMap["params"][0])
    if(!pkg) pkg = "griffon"
    name = GriffonUtil.getClassNameRepresentation(name)

    def packageDir = new File("${basedir}/src/clojure/${pkg.replace('.','/')}")
    packageDir.mkdirs()

    def classFile = new File(packageDir, "${name}.clj") 
    def fqn = "${pkg}.${name}"
    if(classFile.exists()) {
        if(!confirmInput("WARNING: ${fqn} already exists. Are you sure you want to replace this script?")) {
            exit(0)
        }
    }
    println "Creating ${fqn} ..."
    classFile.text = """\
(ns $fqn
    (:gen-class
     :methods [[hello [String] String]]))

(defn -hello
  ([this who]
     (str "Hello " who "!")))

(defn -main
  [who]
  (println (.hello (${fqn}.) who)))

"""
}

setDefaultTarget('createClojureClass')
