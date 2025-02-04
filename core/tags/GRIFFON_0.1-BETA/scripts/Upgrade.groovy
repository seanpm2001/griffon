/*
* Copyright 2004-2008 the original author or authors.
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
 * Gant script that handles upgrading of a Griffon applications
 *
 * @author Graeme Rocher
 * @author Sergey Nebolsin
 * @author Danno Ferrin
 *
 * @since Griffon 0.1
 */

/**
 * Upgrades that occur for each version:
 *
 * for 0.1
 *   - add in Config.groovy griffon.jars.pack=true and griffon.jars.sign=true
 *   - signingkey.params.sigfile is set to GRIFFON for ease of identifying signing file
 *   - startup MVC groups are now configurable in Application.groovy
 *     via application.startupGroups list
 *   - insure testing dirs are present for test-app: test/integration ant test/unit
 */

import org.codehaus.griffon.commons.GriffonContext
import org.codehaus.griffon.plugins.GriffonPluginUtils

defaultTarget("Upgrades a Griffon application from a previous version of Griffon") {
    depends( upgrade )
}

includeTargets << griffonScript("CreateApp" )
includeTargets << griffonScript("Clean" )

target( upgrade: "main upgrade target") {

    depends( createStructure )

    boolean force = args?.indexOf('-force') > -1 ? true : false

    if (appGriffonVersion != griffonVersion) {
        def gv = appGriffonVersion == null ? "?Unknown?" : appGriffonVersion
        event("StatusUpdate", [ "NOTE: Your application currently expects griffon version [$gv], "+
            "this target will upgrade it to Griffon ${griffonVersion}"])
    }

    if(!force) {
        //TODO warn user about descructive changes

//        Ant.input(message: """
//        WARNING: Something bad might happen
//                   """,
//                validargs:"y,n",
//                addproperty:"griffon.upgrade.warning")
//
//        def answer = Ant.antProject.properties."griffon.upgrade.warning"
//
//        if(answer == "n") exit(0)
//
//        if ((griffonVersion.startsWith("1.0")) &&
//            !(['utf-8', 'us-ascii'].contains(System.getProperty('file.encoding')?.toLowerCase()) )) {
//                Ant.input(message: """
//        WARNING: Something else bad might happen
//                       """,
//                    validargs:"y,n",
//                    addproperty:"griffon.another.warning")
//            answer = Ant.antProject.properties."griffon.another.warning"
//            if(answer == "n") exit(0)
//        }
    }

    clean()

    Ant.sequential {
        // removed from grails: move test dir, also has source control chceks

        delete(dir:"${basedir}/tmp", failonerror:false)

        // don't do this, first IDE Support files need to work
        createIDESupportFiles()

        // remove from grails: a bunch of servlet stuff
        // remove from grails: adding new files in grails-app/conf

        mkdir(dir: "${basedir}/test")
        mkdir(dir: "${basedir}/test/integration")
        mkdir(dir: "${basedir}/test/unit")

        // remove from grails: URLMappings

        // if Config.groovy exists and it does not contain values added
        // since 0.0 then sensible defaultsare provided which keep previous
        // behavior even if it is not the default in the current version.
        def configFile = new File(baseFile, '/griffon-app/conf/Config.groovy')
        if(configFile.exists()) {
            def configSlurper = new ConfigSlurper(System.getProperty(GriffonContext.ENVIRONMENT))
            def configObject = configSlurper.parse(configFile.toURI().toURL())

            def packJars = configObject.griffon?.jars?.pack
            def signJars = configObject.griffon?.jars?.sign
            def signingKeyFile = configObject.signingkey?.params?.sigfile

            if([packJars, signJars, signingKeyFile].contains([:])) {
                event("StatusUpdate", [ "Adding properties to Config.groovy"])
                configFile.withWriterAppend {
                    def indent = ''
                    it.writeLine '\n// The following properties have been added by the Upgrade process...'
                    if (!Boolean.valueOf(System.getProperty(GriffonContext.ENVIRONMENT_DEFAULT))) {
                        indent = '        '
                        it.writeLine "environments {\n    ${System.getProperty(GriffonContext.ENVIRONMENT)} {"
                    }
                    if(packJars == [:]) it.writeLine "${indent}griffon.jars.pack=false // jars were not automatically packed in Griffon 0.0"
                    if(signJars == [:]) it.writeLine "${indent}griffon.jars.sign=true // jars were automatically isgned in Griffon 0.0"
                    if(signingKeyFile == [:]) it.writeLine "${indent}signingkey.params.sigfile='GRIFFON' // may safely be removed, but calling upgrade will restore it"
                    if (indent != '') {
                        it.writeLine('    }\n}')
                    }
                }
            }
        }

        // if Application.groovy exists and it does not contain values added
        // since 0.0 then sensible defaultsare provided which keep previous
        // behavior even if it is not the default in the current version.
        def applicationFile = new File(baseFile, '/griffon-app/conf/Application.groovy')
        if(applicationFile.exists()) {
            def configSlurper = new ConfigSlurper(System.getProperty(GriffonContext.ENVIRONMENT))
            def configObject = configSlurper.parse(applicationFile.toURI().toURL())

            def startupGroups = configObject.application.startupGroups

            if([startupGroups].contains([:])) {
                event("StatusUpdate", [ "Adding properties to Application.groovy"])
                applicationFile.withWriterAppend {
                    def indent = ''
                    it.writeLine '\n// The following properties have been added by the Upgrade process...'
                    if (!Boolean.valueOf(System.getProperty(GriffonContext.ENVIRONMENT_DEFAULT))) {
                        indent = '        '
                        it.writeLine "environments {\n    ${System.getProperty(GriffonContext.ENVIRONMENT)} {"
                    }
                    if(startupGroups == [:]) it.writeLine "${indent}application.startupGroups=['root'] // default startup group from 0.0"
                    if (indent != '') {
                        it.writeLine('    }\n}')
                    }
                }
            }
        }

        touch(file:"${basedir}/griffon-app/i18n/messages.properties")

        event("StatusUpdate", [ "Updating application.properties"])
        propertyfile(file:"${basedir}/application.properties",
            comment:"Do not edit app.griffon.* properties, they may change automatically. "+
                "DO NOT put application configuration in here, it is not the right place!") {
            entry(key:"app.name", value:"$griffonAppName")
            entry(key:"app.griffon.version", value:"$griffonVersion")
        }
    }

    // proceed plugin-specific upgrade logic contained in 'scripts/_Upgrade.groovy' under plugin's root
    def plugins = GriffonPluginUtils.getPluginDirectories(pluginsDirPath)
    if (plugins) {
        for (pluginDir in plugins) {
            def f = new File(pluginDir)
            if (f.isDirectory() && f.name != 'core') {
                // fix for Windows-style path with backslashes

                def pluginBase = "${basedir}/plugins/${f.name}".toString().replaceAll("\\\\", "/")
                // proceed _Upgrade.groovy plugin script if exists
                def upgradeScript = new File("${pluginBase}/scripts/_Upgrade.groovy")
                if (upgradeScript.exists()) {
                    event("StatusUpdate", ["Executing ${f.name} plugin upgrade script"])
                    // instrumenting plugin scripts adding 'pluginBasedir' variable
                    def instrumentedUpgradeScript = "def pluginBasedir = '${pluginBase}'\n" + upgradeScript.text
                    // we are using text form of script here to prevent Gant caching
                    includeTargets << instrumentedUpgradeScript
                }
            }
        }

    }

    //TODO create an upgrade README
    //event("StatusUpdate", [ "Please make sure you view the README for important information about changes to your source code."])

    event("StatusFinal", [ "Project upgraded"])
}
