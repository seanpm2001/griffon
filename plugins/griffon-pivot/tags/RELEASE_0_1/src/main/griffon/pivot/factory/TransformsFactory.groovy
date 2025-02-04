/*
 * Copyright 2003-2010 the original author or authors.
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

import org.apache.pivot.wtk.media.drawing.Shape

/**
 * @author Andres Almiray
 */
class TransformsFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsNull(value, name)
        []
    }

    public void setParent(FactoryBuilderSupport builder, Object parent, Object node) {
        if(node instanceof Shape) node.each { parent.transforms.add(it) }
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(child instanceof Shape.Transform) parent << child
    }
}
