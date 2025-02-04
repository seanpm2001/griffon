/*
 * Copyright 2003-2007 the original author or authors.
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
package griffon.charva.impl;

import groovy.lang.Closure;

import charva.awt.event.ActionEvent;

import charvax.swing.AbstractAction;

/** 
 * A default action implementation
 *
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @version $Revision: 6778 $
 */
public class DefaultAction extends AbstractAction {

    private Closure closure;

    public void actionPerformed(ActionEvent event) {
        if (closure == null) {
            throw new NullPointerException("No closure has been configured for this Action");
        }
        closure.call(event);
    }

    public Closure getClosure() {
        return closure;
    }

    public void setClosure(Closure closure) {
        this.closure = closure;
    }

}
