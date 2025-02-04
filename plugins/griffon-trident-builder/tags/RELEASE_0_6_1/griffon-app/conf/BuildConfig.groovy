griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        flatDir name: 'tridentBuilderPluginLib', dirs: 'lib'
        mavenRepo 'http://repository.codehaus.org'
    }
    dependencies {
        compile('org.codehaus.griffon:tridentbuilder:0.4') {
            transitive = false
        }
        compile 'org.pushing-pixels:trident:1.3'
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}

griffon.jars.destDir='target/addon'
griffon.plugin.pack.additional.sources = ['src/gdsl']
