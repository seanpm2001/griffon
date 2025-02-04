/*
 * Copyright 2010 the original author or authors.
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

package griffon.silk

import groovy.swing.factory.ImageIconFactory

/**
 * @author Andres.Almiray
 */
class SilkIconFactory extends ImageIconFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        String icon = attributes.remove('icon') ?: value 
  
        if(!icon) throw new IllegalArgumentException("In $name you must define a node value or icon:")

        if(icon.endsWith('.png')) icon -= '.png'

        value = "/com/famfamfam/silk/${icon}.png"
        super.newInstance(builder, name, value, [:])
    }
}
