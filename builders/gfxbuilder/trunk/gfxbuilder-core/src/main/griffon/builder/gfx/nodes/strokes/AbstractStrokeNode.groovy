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
 */

package griffon.builder.gfx.nodes.strokes

import java.awt.Stroke
import java.awt.Shape

import griffon.builder.gfx.GfxNode
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.StrokeProvider

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class AbstractStrokeNode extends GfxNode implements StrokeProvider {
    protected Stroke _stroke

    public AbstractStrokeNode(String name) {
       super( name )
    }

    void apply(GfxContext context) {}

    Stroke getStroke(){
       if( _stroke == null ){
          _stroke = createStroke()
       }
       _stroke
    }

    Shape createStrokedShape( Shape shape ){
       createStroke().createStrokedShape(shape)
    }

    protected abstract Stroke createStroke()
}
