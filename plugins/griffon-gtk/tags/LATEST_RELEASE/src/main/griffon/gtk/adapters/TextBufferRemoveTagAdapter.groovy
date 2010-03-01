/*
* Copyright 2009-2010 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
 
package griffon.gtk.adapters

import griffon.gtk.impl.BuilderDelegate
import org.gnome.gtk.TextBuffer
import org.gnome.gtk.TextIter
import org.gnome.gtk.TextTag
import org.gnome.gtk.TextBuffer.RemoveTag

/**
 * @author Andres Almiray
 */
class TextBufferRemoveTagAdapter extends BuilderDelegate implements RemoveTag {
    private Closure onRemoveTagClosure

    TextBufferRemoveTagAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onRemoveTag(Closure callback) {
        onRemoveTagClosure = callback
        onRemoveTagClosure.delegate = this
    }

    void onRemoveTag(TextBuffer arg0, TextTag arg1, TextIter arg2, TextIter arg3) {
        if(onRemoveTagClosure) onRemoveTagClosure.call(arg0, arg1, arg2, arg3)
    }
}