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

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

// check to see if we already have a Db4oGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'Db4oGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding Db4oGriffonAddon to Builder.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'Db4oGriffonAddon'.addon=true
''')
}

appConfig = configSlurper1.parse(new File("$basedir/griffon-app/conf/Application.groovy").toURL())
if(!(appConfig.flatten().'griffon.db4o.injectInto')) {
    new File("${basedir}/griffon-app/conf/Application.groovy").append("""
griffon.db4o.injectInto = ['controller']
""")
}

if(!new File("${basedir}/griffon-app/conf/Db4oConfig.groovy").exists()) {
   createArtifact(
      name: "Db4oConfig",
      suffix: "",
      type: "Db4oConfig",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/BootstrapDb4o.groovy").exists()) {
   createArtifact(
      name: "BootstrapDb4o",
      suffix: "",
      type: "BootstrapDb4o",
      path: "griffon-app/conf")
}
