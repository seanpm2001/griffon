/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * Gant script that creates a new Griffon reusable custom constraint
 *
 * @author nick.zhu
 *
 * @since 0.3
 */

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

target('default': "Creates a new constraint") {
    depends(checkVersion, parseArguments)

    promptForName(type: "Constraint")

    def name = argsMap["params"][0]
    createArtifact(name: name, suffix: "Constraint", type: "Constraint", path: "griffon-app/constraints")
	createUnitTest(name: name, suffix: "Constraint")
}