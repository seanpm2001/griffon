/*
 * Copyright 2009-2010 the original author or authors.
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
 * Compiles sources under ${basedir}/src/commons
 *
 * @author Andres Almiray
 *
 * @since 0.2
 */

ant.property(environment:"env")
griffonHome = ant.antProject.properties."env.GRIFFON_HOME"

includePluginScript("lang-bridge", "CompileCommons")

eventCompileStart = { type ->
    if(compilingLangBridgePlugin()) return
    if(type != "source") return
    compileCommons()
}

/**
 * Detects whether we're compiling the LangBridge plugin itself
 */
private boolean compilingLangBridgePlugin() { getPluginDirForName("lang-bridge") == null }
