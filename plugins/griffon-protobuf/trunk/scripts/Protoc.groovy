includeTargets << griffonScript("Init")

target(default: "Compile Protobuf sources with protoc") {
    depends(protoc)
}

target(protoc: "Compile Protobuf sources with protoc") {
    // FIXME -- remove the following defs when GRIFFON-96 is resolved
    gensrcDir = "${projectWorkDir}/gensrc"
    gensrcDirPath = new File(gensrcDir)

    gensrcDirPath.mkdirs()

    String protocExecutable = buildConfig?.google?.protobuf?.protoc
    if(!protocExecutable) {
        println('''Could not find protoc executable. Did you forget to define a value for it?
Make sure you have a similar setting on your griffon-app/conf/BuildSettings.groovy script

google.protobuf.protoc = "/path/to/protoc"''')
        System.exit(1)
    }

    protobufsrc = "${basedir}/src/protobuf"
    protobufsrcDir = new File(protobufsrc)
    if(!protobufsrcDir.list().size()) {
        println "[protoc] No protobuf sources found at $protobufsrc"
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

    println "[protoc] Invoking $protocExecutable on $protobufsrc"
    println "[protoc] Generated sources will be placed at $gensrcDir"
    ant.fileset(dir: protobufsrcDir, includes: "**/*.proto").each { protofile ->
        println "[protoc] Compiling $protofile"
        Process protoc = "$protocExecutable -I=$protobufsrc --java_out=$gensrcDir $protofile".execute([] as String[], new File(basedir))
        protoc.consumeProcessOutput(System.out, System.err)
        protoc.waitFor()
        File markerFile = new File(gensrcDirPath.absolutePath + "/." + (protofile.toString() - protobufsrc).substring(1))
        ant.touch(file: markerFile)
    }
}
