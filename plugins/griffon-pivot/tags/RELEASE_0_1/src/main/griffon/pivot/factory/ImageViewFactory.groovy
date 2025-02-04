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

package griffon.pivot.factory

import griffon.pivot.PivotUtils
import org.apache.pivot.wtk.ImageView
import org.apache.pivot.wtk.media.Image

/**
 * @author Andres Almiray
 */
class ImageViewFactory extends ComponentFactory {
    ImageViewFactory() {
        super(ImageView)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof GString) value = value as String
        if(value instanceof String || value instanceof URL || value instanceof Image) {
            ImageView view = new ImageView()
            view.setImage(value)
            return view
        }

        if (FactoryBuilderSupport.checkValueIsTypeNotString(value, name, ImageView)) {
            return value
        }

        return new ImageView()
    }

    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        attributes.each { property, value ->
            if(!PivotUtils.applyAsEventListener(builder, property, value, node)) {
                PivotUtils.setBeanProperty(property, value, node)
            }
        }
       
        return false
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(child instanceof Image) parent.setImage(child)
        else super.setChild(builder, parent, child)
    }
}
