includeTargets << griffonScript("Init")
includeTargets << griffonScript("Compile")

target(default: "Compile Protobuf sources with protoc") {
    depends(protoc)
}

target(protoc: "Compile Protobuf sources with protoc") {
    depends(checkVersion, classpath)
    // FIXME -- remove the following def when/if GRIFFON-96 is resolved
    gensrcDir = "${projectWorkDir}/gensrc"
    gensrcDirPath = new File(gensrcDir)

    gensrcDirPath.mkdirs()

    def protocExecutable = buildConfig?.google?.protobuf?.protoc
    if(!protocExecutable) {
        println('''Could not find protoc executable. Did you forget to define a value for it?
Make sure you have a similar setting on your griffon-app/conf/BuildSettings.groovy script
    google.protobuf.protoc = "/path/to/protoc"''')
        System.exit(1)
    }

    protobufsrc = "${basedir}/src/protobuf"
    protobufsrcDir = new File(protobufsrc)
    if(!protobufsrcDir.list().size()) {
        ant.echo(message: "[protoc] No protobuf sources found at $protobufsrc")
        System.exit(0)
    }

    boolean uptodate = true
    def skipIt = new RuntimeException()
    try {
        ant.fileset(dir: protobufsrcDir, includes: "**/*.proto").each { protofile ->
            File markerFile = new File(gensrcDir, "." + (protofile.toString() - protobufsrc).substring(1))
            if(!markerFile.exists() || protofile.file.lastModified() > markerFile.lastModified()) throw skipIt
        }
    } catch(x) {
       if(x == skipIt) uptodate = false
       else throw x
    }
    if(uptodate) return

    ant.echo(message: "[protoc] Invoking $protocExecutable on $protobufsrc")
    ant.echo(message: "[protoc] Generated sources will be placed in $gensrcDir")
    ant.fileset(dir: protobufsrcDir, includes: "**/*.proto").each { protofile ->
        ant.echo(message: "[protoc] Compiling $protofile")
        Process protoc = "$protocExecutable -I=$protobufsrc --java_out=$gensrcDir $protofile".execute([] as String[], new File(basedir))
        protoc.consumeProcessOutput(System.out, System.err)
        protoc.waitFor()
        File markerFile = new File(gensrcDirPath.absolutePath + "/." + (protofile.toString() - protobufsrc).substring(1))
        ant.touch(file: markerFile)
    }

    ant.mkdir(dir: classesDirPath)
    ant.echo(message: "[protoc] Compiling generated sources to $classesDirPath")
    try {
        String classpathId = "griffon.compile.classpath"
        ant.groovyc(destdir: classesDirPath,
                    classpathref: classpathId) {
            src(path: gensrcDirPath)
            javac(classpathref: classpathId)
        }
    }
    catch (Exception e) {
        event("StatusFinal", ["Compilation error: ${e.message}"])
        ant.fail(message: "[protoc] Could not compile generated sources: " + e.class.simpleName + ": " + e.message)
    }
}
