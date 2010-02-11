/*
 * Copyright 2010 the original author or authors.
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
class DomainGriffonPlugin {
    def version = 0.1
    def griffonVersion = '0.3-SNAPSHOT > *'
    def dependsOn = [i18n: 0.1, artifacts: 0.1]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "Domain class support"
    def description = '''
Domain class support
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Domain+Plugin"
}
