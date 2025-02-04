/*
 * Copyright 2008-2009 the original author or authors.
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
 * Gant script that creates an RPM based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << pluginScript("installer","_CreateInstaller")

installerWorkDir = "${basedir}/installer/rpm"
binaryDir = "${installerWorkDir}/${griffonAppName}-${griffonAppVersion}"

target(rpmSanityCheck:"") {
    depends(checkVersion, classpath, test_is_linux)
    def src = new File(installerWorkDir)
    if( src && src.list() ) {
        createRPMInstaller()
    } else {
        println """No RPM installer sources were found.
Make sure you call 'griffon prepare-rpm-installer' first
and configure the files appropriately.
"""
    }
}

target(createRPMInstaller: "Creates an RPM installer") {

    copyAllAppArtifacts()
    ant.zip( destfile: "${installerWorkDir}/SOURCES/${griffonAppName}-${griffonAppVersion}-bin.zip",
             basedir: installerWorkDir,
             includes: "${griffonAppName}-${griffonAppVersion}/**/*" )

    ant.rpm( specFile: "${griffonAppName}.spec",
             topDir: installerWorkDir,
             cleanBuildDir: "false",
             failOnError: "true" )
}

setDefaultTarget(rpmSanityCheck)