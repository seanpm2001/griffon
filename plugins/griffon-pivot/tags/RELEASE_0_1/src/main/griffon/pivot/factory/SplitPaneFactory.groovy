/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.pivot.factory

import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.SplitPane

/**
 * @author Andres Almiray
 */
class SplitPaneFactory extends ComponentFactory {
    SplitPaneFactory() {
        super(SplitPane)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def splitPane = super.newInstance(builder, name, value, attributes)
        // splitPane.setTopLeft(null)
        // splitPane.setBottomRight(null)
        return splitPane
    }

    void setChild(FactoryBuilderSupport factory, Object parent, Object child) {
        if(child instanceof Component) {
            if(parent.getTopLeft() == null) {
                parent.setTopLeft(child)
            } else {
                parent.setBottomRight(child)
            }
        } else {
            super.setChild(builder, parent, child)
        }
    }
}
