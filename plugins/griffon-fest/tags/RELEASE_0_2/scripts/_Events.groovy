import org.codehaus.griffon.cli.GriffonScriptRunner as GSR

eventCleanEnd = {
    ant.delete(dir: "${projectWorkDir}/fest-classes", failonerror: false)
    ant.delete(dir: "${basedir}/test/fest-reports", failonerror: false)
}

/*
eventAllTestsStart = {
   // perform any cleanup before running fest tests here!
}

eventAllTestsEnd = {
   if( unitOnly || integrationOnly ) return

   // call run-fest after all other tests have run
   GSR.callPluginOrGriffonScript("RunFest")
}
*/

eventJarFilesStart = {
   // make sure FestGriffonPlugin.class is not added to app jar
   ant.delete(file: "${projectWorkDir}/classes/FestGriffonPlugin.class", failonerror: false)
}
