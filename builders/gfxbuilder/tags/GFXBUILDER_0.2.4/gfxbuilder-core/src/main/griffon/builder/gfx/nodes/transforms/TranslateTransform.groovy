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

package griffon.builder.gfx.nodes.transforms

import java.awt.geom.AffineTransform

import griffon.builder.gfx.GfxAttribute

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class TranslateTransform extends AbstractTransform {
    @GfxAttribute double x = 0d
    @GfxAttribute double y = 0d

    TranslateTransform() {
       super("translate")
    }

    TranslateTransform clone() {
       new TranslateTransform(x: x, y: y, enabled: enabled)
    }

    AffineTransform getTransform() {
       AffineTransform.getTranslateInstance(x as double, y as double)
    }
}