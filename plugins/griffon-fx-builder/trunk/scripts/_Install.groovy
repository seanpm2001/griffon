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

ant.property(environment: "env")
ant.mkdir(dir: "${basedir}/src/javafx")

ConfigSlurper configSlurper = new ConfigSlurper()
o = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
boolean builderIsSet
o.each() { prefix, v ->
    v.each { builder, views ->
        builderIsSet = builderIsSet || 'griffon.builder.fx.FxBuilder' == builder
    }
}

if (!builderIsSet) {
    println 'Adding FxBuilder to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
fx.'griffon.builder.fx.FxBuilder'.view = '*'
""")
}
