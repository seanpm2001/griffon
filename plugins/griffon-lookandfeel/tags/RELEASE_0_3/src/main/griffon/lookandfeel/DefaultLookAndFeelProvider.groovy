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

package griffon.lookandfeel

import java.awt.Component
import java.awt.Window
import javax.swing.UIManager
import javax.swing.LookAndFeel
import javax.swing.SwingUtilities
import griffon.core.GriffonApplication

/**
 * @author Andres Almiray
 */
abstract class DefaultLookAndFeelProvider extends LookAndFeelProvider {
    DefaultLookAndFeelProvider(String name) {
        super(name)
    }
    
    void preview(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo, Component component) {
        if(!handles(lookAndFeelInfo)) return
        lookAndFeelInfo.preview(component)
    }

    void apply(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo, GriffonApplication application) {
        if(!handles(lookAndFeelInfo)) return
        SwingUtilities.invokeLater {
            LookAndFeel lookAndFeel = lookAndFeelInfo.lookAndFeel
            UIManager.setLookAndFeel(lookAndFeel)
            for(Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window)
            }
            application.event('LookAndFeelChanged', [
                LookAndFeelManager.instance.getCurrentLookAndFeelProvider(),
                LookAndFeelManager.instance.getCurrentLookAndFeelInfo(),
                lookAndFeel])
        }
    }
}
