/*
 * Copyright 2003-2007 the original author or authors.
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

package griffon.charva.factory

import charva.awt.Component
import charva.awt.Window
import charvax.swing.JScrollPane
import charvax.swing.JViewport

class ScrollPaneFactory extends BeanFactory {

    public ScrollPaneFactory() {
        this(JScrollPane)
    }

    public ScrollPaneFactory(Class klass) {
        super(klass, false);
    }

    public void setChild(FactoryBuilderSupport factory, Object parent, Object child) {
        if (!(child instanceof Component) || (child instanceof Window)) {
            return;
        }
        if (parent.getViewport()?.getView() != null) {
            throw new RuntimeException("ScrollPane can only have one child component");
        }
        if (child instanceof JViewport) {
            parent.setViewport(child);
        } else {
            parent.setViewportView(child);
        }

    }

}