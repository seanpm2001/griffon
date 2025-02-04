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
 * Gant script that compiles Groovy and Java files in the src tree
 *
 * @author Graeme Rocher
 *
 * @since 0.4
 */

//import griffon.util.GriffonUtil

defaultTarget("Performs compilation on any source files (Java or Groovy) in the 'src' tree") {
    compile()
}

includeTargets << griffonScript("Init")

if(!Ant.antProject.properties."groovyJarSet") {
    Ant.path ( id : 'groovyJarSet' ) {
        fileset ( dir : "${griffonHome}/lib" , includes : "*.jar" )
    }
}
Ant.taskdef (     name : 'groovyc' ,
//                classname : 'org.codehaus.griffon.compiler.GriffonCompiler' ,
                                classname : 'org.codehaus.groovy.ant.Groovyc' ,
                classpathref : 'groovyJarSet' )

compilerClasspath = { testSources ->

    def excludedPaths = ["resources", "i18n", "conf"] // conf gets special handling
	def pluginResources = getPluginSourceFiles()

	for(dir in new File("${basedir}/griffon-app").listFiles()) {
        if(!excludedPaths.contains(dir.name) && dir.isDirectory())
            src(path:"${dir}")
    }
    // Handle conf/ separately to exclude subdirs/package misunderstandings
    src(path: "${basedir}/griffon-app/conf")
    // This stops resources.groovy becoming "spring.resources"
//    src(path: "${basedir}/griffon-app/conf/spring")

	excludedPaths.remove("conf")
    for(dir in pluginResources.file) {
        if(!excludedPaths.contains(dir.name) && dir.isDirectory()) {
            src(path:"${dir}")
        }
     }


    src(path:"${basedir}/src/main")
    include(name:'**/*.groovy')
    include(name:'**/*.java')
    javac(classpathref:"griffon.classpath", debug:"yes")
	if(testSources) {
         src(path:"${basedir}/test/unit")
         src(path:"${basedir}/test/integration")
	}
}

target(compile : "Implementation of compilation phase") {
    event("CompileStart", ['source'])
    depends(resolveDependencies)

    profile("Compiling sources to location [$classesDirPath]") {

        Ant.mkdir(dir:classesDirPath)
        try {
            Ant.groovyc(destdir:classesDirPath,
//                       projectName:baseName,
                    classpathref:"griffon.classpath",
//                       resourcePattern:"file:${basedir}/**/griffon-app/**/*.groovy",
                    encoding:"UTF-8",
                    compilerClasspath.curry(false))
        }
        catch(Exception e) {
            event("StatusFinal", ["Compilation error: ${e.message}"])
            exit(1)
        }
//        classLoader.addURL(classesDir.toURI().toURL())

    // TODO review
    // compile *GriffonPlugin.groovy if it exists
    try {
       if( new File("${basedir}").list().grep{ it =~ /GriffonPlugin\.groovy/ } ){
          Ant.groovyc(destdir:classesDirPath,
   //                       projectName:baseName,
                      classpathref:"griffon.classpath",
   //                       resourcePattern:"file:${basedir}/**/griffon-app/**/*.groovy",
                      encoding:"UTF-8") {
              src(path:"${basedir}")
              include(name:'*GriffonPlugin.groovy')
          }
       }
    }
    catch(Exception e) {
        event("StatusFinal", ["Compilation error: ${e.message}"])
        exit(1)
    }

    ClassLoader contextLoader = Thread.currentThread().getContextClassLoader()
        classLoader.addURL(classesDir.toURI().toURL())

        event("CompileEnd", ['source'])
    }

}
