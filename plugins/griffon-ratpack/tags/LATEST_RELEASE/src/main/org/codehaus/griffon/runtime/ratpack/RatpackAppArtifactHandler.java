/*
 * Copyright 2010-2011 the original author or authors.
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

package org.codehaus.griffon.runtime.ratpack;

import griffon.plugins.ratpack.GriffonRatpackAppClass;

import org.codehaus.griffon.runtime.core.ArtifactHandlerAdapter;
import griffon.core.GriffonClass;
import griffon.core.GriffonApplication;

/**
 * @author Andres Almiray
 */
public class RatpackAppArtifactHandler extends ArtifactHandlerAdapter {
    public RatpackAppArtifactHandler(GriffonApplication app) {
        super(app, GriffonRatpackAppClass.TYPE, GriffonRatpackAppClass.TRAILING);
    }

    protected GriffonClass newGriffonClassInstance(Class clazz) {
        return new DefaultGriffonRatpackAppClass(getApp(), clazz);
    }
}
