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
 * @author Andres Almiray
 */
class ClojureGriffonPlugin {
    def version = "0.5.1"
    def griffonVersion = "0.3 > *"
    def dependsOn = ["lang-bridge": "0.3"]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "Enables Clojure"
    def description = '''
Enables the usage of Clojure on your Griffon application.
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Clojure+Plugin"
}
