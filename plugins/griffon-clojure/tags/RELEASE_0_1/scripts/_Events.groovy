includeTargets << griffonScript("Init")
includeTargets << pluginScript("lang-bridge","CompileInterfaces")
//includePluginScript("lang-bridge","CompileInterfaces")

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir: "${getPluginDirForName('clojure').file}/lib/", includes: "*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

eventCompileStart = { type ->
    if(compilingClojurePlugin()) return
    if(type != "source") return
    compileInterfaces()
    def clojuresrc = "${basedir}/src/clojure"
    def clojuresrcdir = new File(clojuresrc)
    if(!clojuresrcdir.exists()) return

    ant.echo(message: "[clojure] Compiling Clojure sources to $classesDirPath")
    try {
        ant.pathconvert(pathsep:"", property: "clojure.compile.namespaces") {
            fileset(dir: clojuresrc, includes: "**/*.clj")
            chainedmapper {
                packagemapper(from: "${clojuresrc}/*.clj", to: "*")
                filtermapper {
                    replacestring(from: "_", to: "-")
                }
            }
        }
        ant.path(id: "clojure.compile.classpath") {
            path(refid: "griffon.compile.classpath")
            path(location: classesDirPath)
            path(location: clojuresrc)
        }
        ant.java(classname: "clojure.lang.Compile",
                 classpathref: "clojure.compile.classpath") {
            sysproperty(key: "clojure.compile.path", value: classesDirPath)
            arg(line: ant.antProject.properties.'clojure.compile.namespaces')
        }
    }
    catch (Exception e) {
        ant.fail(message: "Could not compile Clojure sources: " + e.class.simpleName + ": " + e.message)
    }
}

/**
 * Detects whether we're compiling the Clojure plugin itself
 */
private boolean compilingClojurePlugin() { getPluginDirForName("clojure") == null }