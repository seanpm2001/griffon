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

package org.codehaus.griffon.runtime.scaffolding

import griffon.plugins.scaffolding.ValueObject
import griffon.util.GriffonClassUtils
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
class DefaultAttributeModel<B, T> extends AbstractAttributeModel<B, T> {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAttributeModel)

    final ValueObject<B> bean

    DefaultAttributeModel(Class<B> beanClass, ValueObject<B> bean, String propertyName) {
        super(beanClass, propertyName)
        this.bean = bean

        setValueFromProperty(false)
        bean.addValueChangeListener(beanChangedUpdater)
    }

    private final PropertyChangeListener beanChangedUpdater = new PropertyChangeListener() {
        void propertyChange(PropertyChangeEvent evt) {
            setValueFromProperty()
        }
    }

    protected T readPropertyFromBean() {
        if (!bean.value) return null
        propertyDescriptor.readMethod.invoke(bean.value, GriffonClassUtils.EMPTY_ARGS)
    }

    protected void writePropertyToBean(T val) {
        if (!bean.value) return
        propertyDescriptor.writeMethod.invoke(bean.value, [val] as Object[])
    }

    String toString() {
        String desc = [
                'description=', modelDescriptor,
                ', value=', value,
                ', beanClass=', beanClass,
                ', bean=', bean
        ].join('')
        '[' + desc + ']'
    }
}
