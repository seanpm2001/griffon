/*
 * Copyright 2010 the original author or authors.
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
package griffon.domain.metaclass;

import griffon.core.GriffonApplication;
import griffon.core.ArtifactInfo;
import griffon.domain.DynamicMethod;
import griffon.domain.GriffonPersistenceUtil;
import griffon.util.GriffonClassUtils;

import groovy.lang.MissingMethodException;

import java.util.Map;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * @author Andres Almiray
 */
public abstract class AbstractFindAllPersistentMethod extends AbstractStaticPersistentMethod {
    public AbstractFindAllPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass, Pattern.compile("^"+ DynamicMethod.FIND_ALL.getMethodName() +"$"));
    }

    protected final Object doInvokeInternal(Class clazz, String methodName, Object[] arguments) {
        if(arguments.length == 0) {
            return findAll(clazz);
        }
        final Object arg = arguments[0] instanceof CharSequence ? arguments[0].toString() : arguments[0];
        if(arg instanceof String) {
            String query = ((String) arg).trim();
            Object[] queryArgs = null;
            Map queryNamedArgs = null;
            int max = GriffonPersistenceUtil.retrieveMaxValue(arguments);
            int offset = GriffonPersistenceUtil.retrieveOffsetValue(arguments);

            if(arguments.length > 1) {
                if (arguments[1] instanceof Collection) {
                    queryArgs = GriffonClassUtils.collectionToObjectArray((Collection) arguments[1]);
                } else if (arguments[1].getClass().isArray()) {
                    queryArgs = (Object[]) arguments[1];
                } else if (arguments[1] instanceof Map) {
                    queryNamedArgs = (Map) arguments[1];
                }
            }

            if(queryNamedArgs != null) {
                for(Iterator it = queryNamedArgs.entrySet().iterator(); it.hasNext();) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if(!(entry.getKey() instanceof String))
                        throw new IllegalArgumentException("Named parameter's name must be String: " + queryNamedArgs.toString());
                }
                queryNamedArgs.remove(GriffonPersistenceUtil.ARGUMENT_MAX);
                queryNamedArgs.remove(GriffonPersistenceUtil.ARGUMENT_OFFSET);
            }

            if(queryNamedArgs != null) {
                return findWithNamedArgs(query, queryNamedArgs, max, offset); 
            } else {
                return findWithQueryArgs(query, queryArgs != null ? queryArgs : GriffonPersistenceUtil.EMPTY_ARRAY, max, offset); 
            }
        } else if(arg instanceof Map) {
            return findByProperties((Map) arg);
        } else if(getDomainClass().getKlass().isAssignableFrom(clazz) ) {
            return findByExample(arg);
        }
        throw new MissingMethodException(methodName, clazz, arguments);
    }

    protected Collection findAll(Class clazz) {
        return Collections.emptyList();
    }

    protected Collection findWithQueryArgs(String query, Object[] queryArgs, int max, int offset) {
        return Collections.emptyList();
    }

    protected Collection findWithNamedArgs(String query, Map namedArgs, int max, int offset) {
        return Collections.emptyList();
    }

    protected Collection findByProperties(Map properties) {
        return Collections.emptyList();
    }

    protected Collection findByExample(Object example) {
        return Collections.emptyList();
    }
}
