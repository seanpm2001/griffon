/*
 * Copyright 2003-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.builder.flamingo.factory

import javax.swing.JMenuBar
import groovy.swing.factory.RootPaneContainerFactory
import org.jvnet.flamingo.ribbon.*

/**
 * @author Danno Ferrin
 * @author Andres Almiray
 */
public class RibbonFrameFactory extends RootPaneContainerFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        JRibbonFrame frame
        if (FactoryBuilderSupport.checkValueIsType(value, name, JRibbonFrame)) {
            frame = value
        } else {
            frame = new JRibbonFrame()
        }
        handleRootPaneTasks(builder, frame, attributes)
        return frame
    }

    public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        if (child instanceof JMenuBar) {
            parent.JMenuBar = child
        } else {
            super.setChild(build, parent, child)
        }
    }
}
