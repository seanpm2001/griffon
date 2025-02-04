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
 * Gant script that runs FEST tests
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

Ant.property(environment:"env")
griffonHome = Ant.antProject.properties."env.GRIFFON_HOME"

defaultTarget("Run FEST tests") {
    depends(checkVersion, configureProxy, packageApp, classpath)
    runFestImpl()
}

includeTargets << griffonScript("Package")

festSourceDir = "${basedir}/test/fest"
festTargetDir = "${projectWorkDir}/fest-classes"
festReportDir = config.griffon.testing.reports.destDir ?: "${basedir}/test/fest-reports"
festPluginBase = getPluginDirForName('fest').file as String
_fest_skip = false

Ant.path( id : 'festJarSet' ) {
    fileset( dir: "${festPluginBase}/lib/test" , includes : "*.jar" )
}

Ant.taskdef( resource: "testngtasks", classpathref: "festJarSet" )

Ant.path( id: "fest.compile.classpath" ) {
    path(refid:"griffon.classpath")
    path(refid:"festJarSet")
    pathelement(location: "${classesDirPath}")
}

Ant.path( id: "fest.runtime.classpath" ) {
    path(refid:"fest.compile.classpath")
    pathelement(location: "${festTargetDir}")
}

target(runFestImpl:"Run FEST tests") {
    checkFestTestsSources()
    compileFestTests()
    runFestTests()
}

target(checkFestTestsSources:"") {
    def src = new File( festSourceDir )
    if( !src || !src.list() ) {
        println "No FEST sources were found. SKIPPING"
        _fest_skip = true
    }
}

target(compileFestTests: "") {
    if( _fest_skip ) return
    event("CompileStart", ['fest'])

    Ant.mkdir( dir: festTargetDir )
    try {
        Ant.groovyc( destdir: festTargetDir,
                     classpathref: "fest.compile.classpath",
                     encoding: "UTF-8" ) {
           src( path: "${festSourceDir}" )
           include( name: "**/*.groovy" )
           include( name: "**/*.java" )
           javac( classpathref: "fest.compile.classpath", debug:"yes" )
       }
    }
    catch(Exception e) {
        event("StatusFinal", ["Compilation error: ${e.message}"])
        exit(1)
    }

    event("CompileEnd", ['fest'])
}

target(runFestTests: "") {
    if( _fest_skip ) return
    Ant.mkdir( dir: festReportDir )
    Ant.testng( classpathref: "fest.runtime.classpath",
                outputDir: "${festReportDir}",
                suitename: "$baseName suite",
                haltOnfailure: false ) {
        classfileset( dir: "${festTargetDir}", includes: "**/*Test.class" )
    }
}
