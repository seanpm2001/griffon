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

// check to see if we already have a TwitterGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'TwitterGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding TwitterGriffonAddon to Builder.groovy'	
    builderConfigFile.append('''
		root.'TwitterGriffonAddon'.addon=true
	''')
}
/* Creating the twitter4j configuration file */
ant.touch(file: "${basedir}/griffon-app/resources/twitter4j.properties")

new File("${basedir}/griffon-app/resources/twitter4j.properties").text = '''
#debug=false
#oauth.consumerKey=
#oauth.consumerSecret=
#oauth.accessToken=
#oauth.accessTokenSecret=
'''