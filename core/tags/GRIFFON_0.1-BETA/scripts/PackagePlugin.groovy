import org.codehaus.griffon.commons.GriffonClassUtils as GCU

import groovy.xml.MarkupBuilder

/*
* Copyright 2004-2005 the original author or authors.
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
 * Gant script that handles the packaging of a Griffon plugin into a distribution zipfile
 *
 * @author Graeme Rocher
 *
 * @since 0.4
 */
//import org.codehaus.griffon.compiler.support.GriffonResourceLoaderHolder


appName = ""

pluginIncludes = [
    "application.properties",
    "*GriffonPlugin.groovy",
    "plugin.xml",
    "griffon-app/**",
    "lib/**",
    "scripts/**",
//    "web-app/**",
    "src/**"
]

pluginExcludes = [
//    "web-app/WEB-INF/**",
//    "web-app/plugins/**",
//    "griffon-app/conf/spring/resources.groovy",
//    "griffon-app/conf/*DataSource.groovy",
    "griffon-app/conf/Application.groovy",
    "griffon-app/conf/Builder.groovy",
    "griffon-app/conf/Config.groovy",
//    "griffon-app/conf/BootStrap.groovy",
//    "griffon-app/conf/UrlMappings.groovy",
//    "griffon-app/conf/log4j.*.properties",
    "**/.svn/**",
    "test/**",
    "**/CVS/**"
]

defaultTarget("Packages a Griffon plugin into a zip for distribution") {
   packagePlugin()
}

includeTargets << griffonScript("Init" )
includeTargets << griffonScript("Compile" )
includeTargets << griffonScript("CreateApp" )
includeTargets << griffonScript("Package" )

target(packagePlugin:"Implementation target") {
    //depends (checkVersion,packagePlugins, packageApp,compile)
    depends (checkVersion,packagePlugins,/*packageApp,*/compile)


    def pluginFile
    new File("${basedir}").eachFile {
        if(it.name.endsWith("GriffonPlugin.groovy")) {
            pluginFile = it
        }
    }

    if(!pluginFile) Ant.fail("Plugin file not found for plugin project")

    try {
        pluginClass = classLoader.loadClass(pluginFile.name[0..-8])
        plugin = pluginClass.newInstance()
    }
    catch(Throwable t) {
        event("StatusError", [ t.message])
        GriffonUtil.deepSanitize(t)
        t.printStackTrace(System.out)
        Ant.fail("Cannot instantiate plugin file")
    }
    pluginName = GCU.getScriptName(GCU.getLogicalName(pluginClass, "GriffonPlugin"))
    println "plugin - $pluginName"
    // Generate plugin.xml descriptor from info in *GriffonPlugin.groovy
    new File("${basedir}/plugin.xml").delete()
    def writer = new IndentPrinter( new PrintWriter( new FileWriter("${basedir}/plugin.xml")))
    def xml = new MarkupBuilder(writer)
    def props = ['author','authorEmail','title','description','documentation']
//    def resourceList = GriffonResourceLoaderHolder.resourceLoader.getResources()
//    def local = plugin.properties['local'] ?: true
    xml.plugin(name:"${pluginName}",version:"${plugin.version}"/*,local:"${(local ?: false)}"*/) {
        props.each {
            if( plugin.properties[it] ) "${it}"(plugin.properties[it])
        }
//        resources {
//            for(r in resourceList) {
//                 def matcher = r.URL.toString() =~ artefactPattern
//                 def name = matcher[0][1].replaceAll('/', /\./)
//                 resource(name)
//            }
//        }
    }

    // Package plugin's zip distribution
    pluginZip = "${basedir}/griffon-${pluginName}-${plugin.version}.zip"
    Ant.delete(file:pluginZip)
    def includesList = pluginIncludes.join(",")
    def excludesList = pluginExcludes.join(",")
    Ant.zip(basedir:"${basedir}", destfile:pluginZip, includes:includesList, excludes:excludesList, filesonly:true)
}