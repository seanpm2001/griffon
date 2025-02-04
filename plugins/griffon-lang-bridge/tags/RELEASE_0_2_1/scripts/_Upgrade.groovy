//
// This script is executed by Griffon during application upgrade ('griffon upgrade'
// command). This script is a Gant script so you can use all special variables
// provided by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/griffon-app/jobs")
//
File oldsrc = new File("${basedir}/src/interfaces")
if(oldsrc.exists()) {
    ant.move(file: "${basedir}/src/interfaces",
             tofile: "${basedir}/src/commons")
}
