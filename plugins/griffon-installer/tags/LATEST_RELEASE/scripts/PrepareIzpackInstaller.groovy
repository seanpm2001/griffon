/*
 * Copyright 2008 the original author or authors.
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
 * Gant script that prepares an IzPack based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

Ant.property(environment:"env")
griffonHome = Ant.antProject.properties."env.GRIFFON_HOME"

defaultTarget("Prepare IzPack installer") {
    prepareIzPackInstaller()
}

includeTargets << griffonScript("Init")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << pluginScript("installer","_PrepareInstaller")

target(prepareIzPackInstaller: "Prepares an IzPack installer") {
    event( "PrepareIzpackInstallerStart", [] )

    installerWorkDir = "${basedir}/installer/izpack"
    resourcesDir = installerWorkDir + "/resources"
    binaryDir = installerWorkDir + "/binary"
    prepareBinary()

    Ant.copy( todir: resourcesDir ) {
        fileset( dir: "${installerPluginBase}/src/templates/izpack" )
    }
    Ant.replace( dir: resourcesDir, includes: "*.xml,*.html,*.txt" ) {
        replacefilter( token: "@app.name@", value:"${griffonAppName}" )
        replacefilter( token: "@app.version@", value:"${griffonAppVersion}" )
    }

    event( "PrepareIzpackInstallerEnd", [] )
}
