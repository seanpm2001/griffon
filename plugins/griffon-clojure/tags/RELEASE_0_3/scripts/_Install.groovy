//
// This script is executed by Griffon after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/griffon-app/jobs")
//

includeTargets << griffonScript("_GriffonInit")

def checkOptionIsSet = { where, option ->
   boolean optionIsSet = false
   where.each { prefix, v ->
       v.each { key, views ->
           optionIsSet = optionIsSet || option == key
       }
   }
   optionIsSet
}

def checkConfigOptionIsSet = { where, option ->
   boolean optionIsSet = false
   where.each { key, value ->
       optionIsSet = optionIsSet || option == key
   }
   optionIsSet
}

// append hints for config options if not present
appConfig = configSlurper.parse(new File("${basedir}/griffon-app/conf/Application.groovy").toURL())
if(!checkConfigOptionIsSet(appConfig, "griffon.clojure.dynamicPropertyName")) {
    new File("${basedir}/griffon-app/conf/Application.groovy").append("""
griffon.clojure.dynamicPropertyName = "clj"
""")
}
if(!checkConfigOptionIsSet(appConfig, "griffon.clojure.injectInto")) {
    new File("${basedir}/griffon-app/conf/Application.groovy").append("""
griffon.clojure.injectInto = ["controller"]
""")
}

ant.mkdir(dir: "${basedir}/src/clojure")
ant.mkdir(dir: "${basedir}/griffon-app/resources/clj")
ant.mkdir(dir: "${basedir}/test/tap")

// check to see if we already have a ClojureGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'ClojureGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding ClojureGriffonAddon to Builders.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'ClojureGriffonAddon'.addon=true
''')
}
