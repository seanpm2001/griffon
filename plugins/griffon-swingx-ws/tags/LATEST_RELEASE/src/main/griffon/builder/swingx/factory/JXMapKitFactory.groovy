/*
 * Copyright 2009 the original author or authors.
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

package griffon.builder.swingx.factory

import groovy.swing.factory.ComponentFactory
import org.jdesktop.swingx.JXMapKit

/**
 * @author Andres Almiray
 */
class JXMapKitFactory extends ComponentFactory {
    JXMapKitFactory() {
        super(JXMapKit)
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        def mapKit = new JXMapKit()
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, JXMapKit)) {
            mapKit = value
        }
        if(!attributes.containsKey('defaultProvider')) {
            mapKit.defaultProvider = JXMapKit.DefaultProviders.OpenStreetMaps
        }
        if(!attributes.containsKey('zoom')) {
            mapKit.zoom = 15
        }

        builder.context.override = attributes.remove('overrideWaypointPainter')

        mapKit
    }
}
