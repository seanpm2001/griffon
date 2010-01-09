/*
 * Copyright 2007-2010 the original author or authors.
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

package griffon.builder.jide.factory

import java.awt.Color
import com.jidesoft.swing.PartialSide
import com.jidesoft.swing.PartialGradientLineBorder
import groovy.swing.factory.SwingBorderFactory

/**
 * accepts attributes:<br />
 * color: java.awt.Color <br/>
 * color: java.awt.Color, thickness: int <br/>
 * color: java.awt.Color, thickness: int, roundedBorders: boolean <br/>
 *
 * Based on groovy.swing.factory.LineBorderFactory
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class PartialGradientLineBorderFactory extends SwingBorderFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        builder.context.applyBorderToParent = attributes.remove('parent')

        def colors = attributes.remove("colors")
        if (colors == null) {
            throw new RuntimeException("colors: is a required attribute for $name")
        }
        if( colors.size() % 2 ) {
           throw new RuntimeException("In $name attribute colors: must be of even size")
        }
        def thickness = attributes.remove("thickness")
        if (thickness == null) {
            thickness = 1
        }
        def roundedCorners = attributes.remove("roundedCorners")
        if (roundedCorners == null) {
            roundedCorners = false
        }
        def sides = attributes.remove("sides")
        sides = sides == null ? PartialSide.ALL : sides

        return new PartialGradientLineBorder(colors as Color[], thickness, sides)
    }
}