/*
 * Copyright 2008 the original author or authors.
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

import org.jvnet.flamingo.common.AbstractCommandButton
import org.jvnet.flamingo.common.JCommandButtonStrip

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class CommandButtonStripFactory extends AbstractFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
        if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, JCommandButtonStrip) ) {
            return value
        }

        FlamingoFactoryUtils.translateCommandButtonConstants(attributes)
        def stripOrientation = attributes.remove("stripOrientation") ?: JCommandButtonStrip.StripOrientation.HORIZONTAL
        return new JCommandButtonStrip(stripOrientation)
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (!(child instanceof AbstractCommandButton)) {
            return
        }
        try {
            def constraints = builder.context.constraints
            if (constraints != null) {
                parent.add(child, constraints)
                builder.context.remove('constraints')
            } else {
                parent.add(child)
            }
        } catch (MissingPropertyException mpe) {
            parent.add(child)
        }
    }

    public boolean isLeaf() {
        return false
    }
}