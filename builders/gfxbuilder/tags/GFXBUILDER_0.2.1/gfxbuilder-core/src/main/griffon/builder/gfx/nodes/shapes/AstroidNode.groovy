/*
 * Copyright 2007-2009 the original author or authors.
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

package griffon.builder.gfx.nodes.shapes

import java.awt.Shape
import org.kordamp.jsilhouette.geom.Astroid
import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class AstroidNode extends AbstractShapeGfxNode {
    @GfxAttribute float cx = 5f
    @GfxAttribute float cy = 5f
    @GfxAttribute(alias="r") float radius = 2.5f
    @GfxAttribute(alias="a") float angle = 0f

    AstroidNode() {
        super("astroid")
    }

    AstroidNode(Astroid astroid) {
        super("astroid")
        cx = astroid.cx
        cy = astroid.cy
        radius = astroid.radius
        angle = astroid.angle
    }

    Shape calculateShape() {
       return new Astroid( cx as float,
                           cy as float,
                           radius as float,
                           angle as float )
    }
}