scriptEnv = "test"

Ant.property(environment:"env")
griffonHome = Ant.antProject.properties."env.GRIFFON_HOME"
System.setProperty('cobertura.code.coverage', 'on')

includeTargets << griffonScript("Package")
includeTargets << griffonScript("Bootstrap")
includeTargets << griffonScript("TestApp")

pluginHome = getPluginDirForName('code-coverage').file as String
reportFormat = 'html'
postProcessReports = false
codeCoverageExclusionList = [
    //"**/*BootStrap*",
    "Application*",
    "Builder*",
    "Config*",
    //"**/*DataSource*",
    "**/*resources*",
    //"**/*UrlMappings*",
    "**/*Tests*",
    "**/griffon/test/**",
    "**/org/codehaus/groovy/griffon/**",
    "**/PreInit*",
    "CodeCoverageGriffonPlugin*" ]

// We need to save the exit code from the 'testApp' target.
def testAppExitCode = 0

//TODO - would like this to work (put file in tmp dir), but cobertura creates a cobertura.ser in the running dir anyway...
//dataFile = "${griffonTmp}/cobertura.ser"
dataFile = "cobertura.ser"

target ('default': "Test App with Cobertura") {
    depends(classpath, checkVersion, configureProxy)

    packageApp()

    // Check whether the project defines its own directory for test
    // reports.
    coverageReportDir = "${config.griffon.testing.reports.destDir ?: testDir}/cobertura"

    // Add any custom exclusions defined by the project.
    // this needs to happen AFTER packageApp so config.groovy is properly loaded
    if (config.coverage.exclusions) {
      codeCoverageExclusionList += config.coverage.exclusions
    }

    cleanup()

    Ant.path(id: "cobertura.classpath"){
        fileset(dir:"${pluginHome}/lib/cobertura"){
            include(name:"*.jar")
        }
    }

    if(args?.indexOf('-xml') >-1) {
        reportFormat = 'xml'
        args -= '-xml'
    }

    if (args?.indexOf('-nopost') > -1) {
        postProcessReports = false
        args -= '-nopost'
    }

    compileTests()
    instrumentTests()
    testApp()

    flushCoverageData()

    coberturaReport()
    if (postProcessReports) {postProcessReports()}
    Ant.delete(dir:testDirPath)
    event("StatusFinal", ["Cobertura Code Coverage Complete (view reports in: ${coverageReportDir})"])

    // Exit the script using the code returned by 'testApp'.
    System.exit(testAppExitCode)
}

target(cleanup:"Remove old files") {
    Ant.delete(file:"${dataFile}", quiet:true)
    Ant.delete(dir:testDirPath, quiet:true)
    Ant.delete(dir:coverageReportDir, quiet:true)
}

target(instrumentTests:"Instruments the compiled test cases") {
    Ant.taskdef (  classpathRef : 'cobertura.classpath', resource:"tasks.properties" )
    try {
        //for now, instrument classes in the same directory griffon creates for testClasses
        //TODO - need to figure out how to put cobertura instrumented classes in different dir
        //and put that dir in front of testClasses in the classpath
        Ant.'cobertura-instrument' (datafile:"${dataFile}") {
            fileset(dir:testDirPath) {
                include(name:"**/*.class")
                codeCoverageExclusionList.each { pattern ->
                    exclude(name:pattern)
                }
            }
        }
    }
    catch(Exception e) {
       event("StatusFinal", ["Compilation Error: ${e.message}"])
       exit(1)
    }
}

target(coberturaReport:"Generate Cobertura Reports") {
    Ant.mkdir(dir:"${coverageReportDir}")
    Ant.taskdef (  classpathRef : 'cobertura.classpath', resource:"tasks.properties" )
    try {
        Ant.'cobertura-report'(destDir:"${coverageReportDir}", datafile:"${dataFile}", format:reportFormat){
            //load all these dirs independently so the dir structure is flattened,
            //otherwise the source isn't found for the reports
            fileset(dir:"${basedir}/griffon-app/controllers")
            fileset(dir:"${basedir}/griffon-app/models")
            fileset(dir:"${basedir}/griffon-app/views")
            fileset(dir:"${basedir}/griffon-app/lifecycle")
//             fileset(dir:"${basedir}/griffon-app/taglib")
//             fileset(dir:"${basedir}/griffon-app/utils")
            fileset(dir:"${basedir}/src/main")
            if (config.coverage?.sourceInclusions){
                config.coverage.sourceInclusions.each {
                    fileset(dir:"${basedir}/${it}")
                }
            }
        }
    }
    catch(Exception e) {
       event("StatusFinal", ["Compilation Error: ${e.message}"])
       exit(1)
    }
}

target('postProcessReports': 'replace controller closure class name with action name') {
//     def startTime = new Date().time
//     def controllers = griffonApp.getArtefacts(org.codehaus.groovy.griffon.commons.ControllerArtefactHandler.TYPE)
//     controllers.each {controllerClass ->
//         def closures = [:]
//         controllerClass.reference.propertyDescriptors.each {propertyDescriptor ->
//             def closure = controllerClass.getPropertyOrStaticPropertyOrFieldValue(propertyDescriptor.name, Closure)
//             if (closure) {
//                 Ant.replace(dir: "${coverageReportDir}",
//                         token: ">${closure.class.name}<",
//                         value: ">${controllerClass.fullName}.${propertyDescriptor.name}<") {
//                     include(name: "${controllerClass.fullName}.html")
//                     include(name: "frame-summary*.html")
//                 }
//             }
//         }
//     }
//     def endTime = new Date().time
//     println "Post processed reports in ${endTime - startTime}ms"
}

target('exit':"override exit") { code ->
    // Save the exit code.
    testAppExitCode = code
}

def flushCoverageData() {
    //per the Cobertura FAQ, force save of coverage data before JVM exits...
    //see http://cobertura.sourceforge.net/faq.html "Cobertura only writes the coverage data file when..."
    try {
        def saveClass = Class.forName('net.sourceforge.cobertura.coveragedata.ProjectData')
        def saveMethod = saveClass.getDeclaredMethod("saveGlobalProjectData", new Class[0])
        saveMethod.invoke(null,new Object[0]);
    } catch (Throwable t) {
        event("StatusFinal", ["Unable to flush Cobertura code coverage data."])
        exit(1)
    }
}
