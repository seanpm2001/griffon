/*
 * Copyright 2007-2008 the original author or authors.
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
 */

import groovy.beans.Bindable
import ca.odell.glazedlists.BasicEventList

class SwingPadModel {
   @Bindable boolean dirty = false
   @Bindable boolean successfulScript = false
   @Bindable String content = ""
   @Bindable File scriptFile
   @Bindable String status = ""
   @Bindable String errors = ""
   @Bindable int caretPosition = 0
   @Bindable String currentTab = "sourceTab"

   Map builders = [
      swing:      [ enabled: true,  loaded: true,  type: "groovy.swing.SwingBuilder"],
      swingx:     [ enabled: true,  loaded: true,  type: "groovy.swing.SwingXBuilder"],
      jide:       [ enabled: true,  loaded: true,  type: "griffon.builder.jide.JideBuilder"],
      flamingo:   [ enabled: false, loaded: false, type: "griffon.builder.flamingo.FlamingoBuilder"],
      tray:       [ enabled: false, loaded: false, type: "griffon.builder.tray.TrayBuilder"],
      macwidgets: [ enabled: false, loaded: false, type: "griffon.builder.macwidgets.MacWidgetsBuilder"]
   ]

   Map samples = [:]
   @Bindable String currentSample

   @Bindable boolean horizontalLayout = true

   @Bindable Map suggestion = [:]
   List suggestions = new BasicEventList()
}