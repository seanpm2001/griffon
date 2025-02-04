// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// griffon.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${griffonSettings.griffonWorkDir}/${appName}-config.properties",
//                             "file:${griffonSettings.griffonWorkDir}/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    griffon.config.locations << "file:" + System.properties["${appName}.config.location"]
// }


// log4j configuration
log4j {
    appender.stdout = "org.apache.log4j.ConsoleAppender"
    appender.'stdout.layout'="org.apache.log4j.PatternLayout"
    appender.'stdout.layout.ConversionPattern'='[%r] %c{2} %m%n'
    appender.errors = "org.apache.log4j.FileAppender"
    appender.'errors.layout'="org.apache.log4j.PatternLayout"
    appender.'errors.layout.ConversionPattern'='[%r] %c{2} %m%n'
    appender.'errors.File'="stacktrace.log"
    rootLogger="error,stdout"
    logger {
        griffon="error"
        StackTrace="error,errors"
        org {
            codehaus.groovy.griffon.commons="info" // core / classloading
        }
    }
    additivity.StackTrace=false
}

// key signing information
environments {
    development {
        signingkey {
            params {
                sigfile = "GRIFFON"
                keystore = "${basedir}/griffon-app/conf/keys/devKeystore"
                alias = 'development'
                storepass = 'BadStorePassword'
                keypass   = 'BadKeyPassword'
                lazy      = true // only sign when unsigned
            }
        }

    }
    production {
        signingkey {
            params {
                sigfile = "GRIFFON"
                keystore = "CHANGE ME"
                alias = 'CHAMGE ME'
                // NOTE: for production keys it is more secure to rely on key prompting
                // no value means we will prompt //storepass = 'BadStorePassword'
                // no value means we will prompt //keypass   = 'BadKeyPassword'
                lazy = false // sign, regardless of existing signatures
            }
        }

        griffon {
            jars {
                destDir = "${basedir}/staging"
            }
            webstart {
                codebase = "CHANGE ME"
            }
        }
    }
}

griffon {
    jars {
        sign = false
        pack = false
        destDir = "${basedir}/staging"
        jarName = "${appName}.jar"
    }
    webstart {
        codebase = "${new File(griffon.jars.destDir).toURI().toASCIIString()}"
        jnlp = "application.jnlp"
    }
    applet {
        jnlp = "applet.jnlp"
        html = "applet.html"
    }
}


// The following properties have been added by the Upgrade process...
griffon.extensions.jarUrls = [] // remote jars were not possible in Griffon 0.1
griffon.extensions.jnlpUrls = [] // remote jars were not possible in Griffon 0.1
// you may now tweak memory parameters
//griffon.memory.min='16m'
//griffon.memory.max='64m'
//griffon.memory.maxPermSize='64m'
