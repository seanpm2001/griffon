/*
 * Copyright 2007-2008 the original author or authors.
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

package griffon.builder.gfx.runtime

import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class AbstractGfxRuntime implements GfxRuntime {
   protected GfxContext _context
   protected GfxNode _node

   AbstractGfxRuntime(GfxNode node, GfxContext context){
      _node = node
      _context = context
   }

   public GfxContext getContext() {
      _context
   }
}