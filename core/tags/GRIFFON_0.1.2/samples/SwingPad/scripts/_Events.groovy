eventPrepareIzpackInstallerEnd = { ->
    ant.copy( todir: "${basedir}/installer/izpack/resources", overwrite: true ) {
        fileset( dir: "${basedir}/src/installer/izpack/resources", includes: "**" )
    }
    ant.replace( dir: "${basedir}/installer/izpack/resources" ) {
        replacefilter(token: "@app.name@", value: griffonAppName)
        replacefilter(token: "@app.version@", value: griffonAppVersion)
    }
}

eventCopyLibsEnd = { jardir ->
   ant.copy(todir: "${jardir}/flamingo") {
      fileset(dir: "${basedir}/lib/flamingo", includes:"*.jar")
   }
   ant.copy(todir: "${jardir}/tray") {
      fileset(dir: "${basedir}/lib/tray", includes:"*.jar")
   }
   ant.copy(todir: "${jardir}/macwidgets") {
      fileset(dir: "${basedir}/lib/macwidgets", includes:"*.jar")
   }
}
