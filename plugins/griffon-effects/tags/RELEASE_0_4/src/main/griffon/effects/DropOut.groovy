/*
 * Copyright (c) 2010 Effects - Andres Almiray. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Effects - Andres Almiray nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package griffon.effects

import java.awt.Window
import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import org.pushingpixels.trident.Timeline

/**
 * Fades and moves a window.<p>
 * Parameters:
 * <ul>
 *    <li><b>anchor</b>: Anchor, anchoring point. default: Anchor.BOTTOM</li>
 * </ul>
 * <p>Shared parameters:
 * <ul>
 *    <li><b>duration</b>: long, how long should the animation take. default: 500l</li>
 *    <li><b>delay</b>: long, wait time before the animation starts. default: 0l</li>
 *    <li><b>ease</b>: TimelineEase. default: Linear</li>
 *    <li><b>wait</b>: boolean. Force the caller thread to wait until the effects finishes. default: false</li>
 * </ul>
 *
 * <p>If a callback is supplied it will be called at the end of the animation,
 * with the component and supplied parameters as arguments.</p>
 *
 * @author Andres Almiray
 */
class DropOut extends ParallelEffect {
    /**
     * Creates a new DropOut effect.<br/>
     *
     * @param params - set of options
     * @param window - the window to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    DropOut(Map params = [:], Window window, Closure callback = null) {
        super(EffectUtil.mergeParams(params, [anchor: Anchor.BOTTOM]), window, callback)
        def ps = paramsInternal()
        ps.anchor = Anchor.resolve(ps.anchor)
    }
 
    List<BasicEffect> makeEffects() { 
        def ps = paramsInternal()
        Point origin = component.location
        Dimension size = component.getSize()
        Dimension screen = Toolkit.defaultToolkit.screenSize

        if(ps.anchor == Anchor.CENTER) {
            ps.from = 100f
            ps.to = 0f
            return [
                new Fade(ps, component),
                new Scale(ps, component)
            ] 
        }

        ps.mode = 'absolute'
        switch(ps.anchor) {
            case Anchor.TOP:
                ps.x = origin.x
                ps.y = -(size.height) 
                break
            case Anchor.TOP_LEFT:
                ps.x = -(size.width) 
                ps.y = -(size.height) 
                break
            case Anchor.LEFT:
                ps.x = -(size.width) 
                ps.y = origin.y
                break
            case Anchor.BOTTOM_LEFT:
                ps.x = -(size.width) 
                ps.y = screen.height
                break
            case Anchor.BOTTOM:
                ps.x = origin.x
                ps.y = screen.height
                break
            case Anchor.BOTTOM_RIGHT:
                ps.x = screen.width
                ps.y = screen.height
                break
            case Anchor.RIGHT:
                ps.x = screen.width
                ps.y = origin.y
                break
            case Anchor.TOP_RIGHT:
                ps.x = screen.width
                ps.y = -(size.height) 
                break
        }

        return [
            new Fade(ps, component),
            new Move(ps, component)
        ]
    }
}
