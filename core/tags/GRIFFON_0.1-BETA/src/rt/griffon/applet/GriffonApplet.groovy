/*
 * Copyright 2008 the original author or authors.
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
package griffon.applet

import griffon.util.GriffonApplicationHelper
import griffon.util.IGriffonApplication
import javax.swing.JApplet

/**
 * Created by IntelliJ IDEA.
 *@author Danno.Ferrin
 * Date: May 17, 2008
 * Time: 12:47:33 PM
 */
class GriffonApplet extends JApplet implements IGriffonApplication {

    Map models      = [:]
    Map views       = [:]
    Map controllers = [:]
    Map builders    = [:]

    Binding bindings = new Binding()
    ConfigObject config
    ConfigObject builderConfig

    private boolean appletContainerDispensed = false

    public void init() {
        GriffonApplicationHelper.prepare(this)
        GriffonApplicationHelper.startup(this)
    }

    public void start() {
        //GriffonApplicationHelper.callReady()
        // skip the EDT sillyness, just call ready
        ready()
    }

    public void stop() {
        GriffonApplicationHelper.runScriptInsideEDT("Stop", this)
    }

    public void destroy() {
        shutdown()
    }

    public Class getConfigClass() {
        return getClass().classLoader.loadClass("Application")
    }

    public Class getBuilderClass() {
        return getClass().classLoader.loadClass("Builder")
    }

    public Object createApplicationContainer() {
        if (appletContainerDispensed) {
            return GriffonApplicationHelper.createJFrameApplication(this)
        } else {
            appletContainerDispensed = true
            return this;
        }
    }

    public void initialize() {
        GriffonApplicationHelper.runScriptInsideEDT("Initialize", this)
    }

    public void ready() {
        GriffonApplicationHelper.runScriptInsideEDT("Ready", this)
    }

    public void shutdown() {
        GriffonApplicationHelper.runScriptInsideEDT("Shutdown", this)
    }

    public void startup() {
        GriffonApplicationHelper.runScriptInsideEDT("Startup", this)
    }
}
