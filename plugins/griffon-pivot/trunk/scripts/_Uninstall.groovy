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

appToolkits = metadata.'app.toolkits'.split(',').toList() - 'pivot'
if(appToolkits) {
    updateMetadata('app.toolkits': appToolkits.join(','))
} else {
    metadata.remove('app.toolkits')
    metadata.persist()
}

def builderConfigText = new StringBuffer()
boolean insideOldDefs = false
boolean insidePivotPluginDefs = false
builderConfigFile.eachLine { line ->
    switch(line) {
        case ~/.*PIVOT_PLUGIN_COMMENT_START.*/: insideOldDefs = true;  break
        case ~/.*PIVOT_PLUGIN_COMMENT_END.*/: insideOldDefs = false; break
        case ~/.*ADDED_BY_PIVOT_PLUGIN_START.*/: insidePivotPluginDefs = true; break
        case ~/.*ADDED_BY_PIVOT_PLUGIN_END.*/: insidePivotPluginDefs = false; break
        default:
            if(insideOldDefs || !insidePivotPluginDefs) builderConfigText += line + '\n'
    }
}
builderConfigFile.text = builderConfigText

printFramed("""Please review your View scripts as they may contain
Pivot specific nodes which won't work after uninstalling
""")
