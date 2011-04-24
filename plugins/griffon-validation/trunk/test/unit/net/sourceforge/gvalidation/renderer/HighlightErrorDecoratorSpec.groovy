/*
 * Copyright 2010 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.gvalidation.renderer

import griffon.spock.UnitSpec
import org.springframework.context.MessageSource
import net.sourceforge.gvalidation.Errors
import java.awt.Color
import javax.swing.JComponent

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class HighlightErrorDecoratorSpec extends UnitSpec {

    def 'Should change background color to pink when decorating'(){
        given:
        HighlightErrorDecorator decorator = new HighlightErrorDecorator()

        def errorField = "email"
        def errors = new Errors()
        errors.rejectValue(errorField, 'emailErrorCode')

        def model = [errors:errors]
        def color = Color.WHITE
        def node = [getBackground:{color},setBackground:{c->color=c}] as JComponent
        def messageResource = [:] as MessageSource


        decorator.register(model, node, errorField, messageResource)

        when:
        decorator.decorate(errors, errors.getFieldError(errorField))

        then:
        decorator.originalBgColor == Color.WHITE
        node.background == Color.PINK
    }

    def 'Should restore background color when undecorating'(){
        given:
        HighlightErrorDecorator decorator = new HighlightErrorDecorator()

        def errorField = "email"
        def errors = new Errors()
        errors.rejectValue(errorField, 'emailErrorCode')

        def model = [errors:errors]
        def color = Color.WHITE
        def node = [getBackground:{color},setBackground:{c->color=c}] as JComponent
        def messageResource = [:] as MessageSource


        decorator.register(model, node, errorField, messageResource)

        when:
        decorator.decorate(errors, errors.getFieldError(errorField))
        decorator.undecorate()

        then:
        decorator.originalBgColor == Color.WHITE
        node.background == Color.WHITE
    }

}
