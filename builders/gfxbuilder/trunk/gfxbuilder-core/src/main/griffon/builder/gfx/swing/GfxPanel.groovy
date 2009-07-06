/*
 * Copyright 2007-2009 the original author or authors.
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

package griffon.builder.gfx.swing

import java.awt.Graphics
import java.awt.LayoutManager
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing.JPanel
import griffon.builder.gfx.*

/**
 * A Panel that can use a GfxOperation to draw itself.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class GfxPanel extends JPanel implements PropertyChangeListener, MouseListener,
   MouseMotionListener, MouseWheelListener, KeyListener {
     private GfxNode _node
     private GfxContext _context = new GfxContext()
     private boolean displayed
     private List errorListeners = []
     private List lastTargets = []

     boolean animate

     GfxPanel(){
         super(null)
         addMouseListener(this)
         addMouseMotionListener(this)
         addMouseWheelListener(this)
         addKeyListener(this)
     }

     public void setLayout(LayoutManager mgr){
         // do not allow the layout to be changed
     }

     /**
      * Returns the current GfxNode of this Panel
      * @return the current GfxNode of this Panel
      */
     public GfxNode getNode(){
         return _node
     }

     /**
      * Sets the GfxNode for this Panel.<br>
      * If the panel is visible, a <code>repaint()</code> will be ensued
      */
     public void setNode(GfxNode node){
         if( node ){
             if(_node){
                _node.removePropertyChangeListener(this)
             }
             _node = node
             _node.addPropertyChangeListener(this)
             if(visible){
                 repaint()
             }
         }
     }

     public void paintComponent(Graphics g){
         _context.g = g
         _context.component = this
         if(_node){
             g.clearRect(0, 0, size.width as int, size.height as int)
             try{
                 _context.eventTargets = []
                 _context.groupSettings = [:]
                 _node.apply(_context)
             }catch(Exception e){
                 fireGfxErrorEvent(e)
             }
         }
     }

     public void addGfxErrorListener(GfxErrorListener listener){
         if( !listener || errorListeners.contains(listener) ) return
         errorListeners.add( listener )
     }

     public void removeGfxErrorListener(GfxErrorListener listener){
         if( listener ) errorListeners.remove( listener )
     }

     public List getGfxErrorListeners(){
         return Collections.unmodifiableList(errorListeners)
     }

     protected void fireGfxErrorEvent(Throwable t) {
         t.printStackTrace()
         def event = new GfxErrorEvent(this, t)
         errorListeners.each { listener ->
            listener.errorOccurred( event )
         }
     }

     public void propertyChange( PropertyChangeEvent event ){
         if(animate && visible){
             repaint()
         }
     }

     /* ===== MouseListener ===== */

     public void mouseEntered(MouseEvent e){
         lastTargets.clear()
     }

     public void mouseExited(MouseEvent e){
         lastTargets.clear()
     }

     public void mousePressed(MouseEvent e){
         fireMouseEvent( e, "mousePressed" )
     }

     public void mouseReleased(MouseEvent e){
         fireMouseEvent( e, "mouseReleased" )
     }

     public void mouseClicked(MouseEvent e){
         fireMouseEvent( e, "mouseClicked" )
     }

     /* ===== MouseMotionListener ===== */

     public void mouseMoved(MouseEvent e){
         if( !_context.eventTargets ) return
         def targets = getTargets(e)
         if( targets ){
            def oldTargets = []
            def visitedTargets = []
            lastTargets.each { target ->
               if( !targets.contains(target) ){
                  oldTargets << target
               }else{
                  visitedTargets << target
               }
            }
            def newTargets = targets - visitedTargets
            oldTargets.each { t -> t.mouseExited(new GfxInputEvent(this, e, t)) }
            newTargets.each { t -> t.mouseEntered(new GfxInputEvent(this, e, t)) }
            targets.each { t -> t.mouseMoved(new GfxInputEvent(this, e, t)) }
            lastTargets = targets
//                 def inputEvent = new GfxInputEvent( this, e, target )
//                 if( target != lastTarget ){
//                    if( lastTarget ) lastTarget.mouseExited( new GfxInputEvent( this, e, target ) )
//                    lastTarget = target
//                    target.mouseEntered( inputEvent )
//                 }
//                 target.mouseMoved( inputEvent )
         }else if(lastTargets){
            lastTargets.each { it.mouseExited(new GfxInputEvent(this, e, it)) }
            lastTargets.clear()
         }
     }

     public void mouseDragged(MouseEvent e){
         fireMouseEvent( e, "mouseDragged" )
     }

     /* ===== MouseWheelListener ===== */

     public void mouseWheelMoved(MouseWheelEvent e){
         fireMouseEvent( e, "mouseWheelMoved" )
     }

     /* ===== KeyListener ===== */

     public void keyPressed(KeyEvent e){

     }

     public void keyReleased(KeyEvent e){

     }

     public void keyTyped(KeyEvent e){

     }

     /* ===== PRIVATE ===== */

     private void fireMouseEvent(MouseEvent e, String mouseEventMethod){
         if( !_context.eventTargets ) return
         getTargets(e).each { target ->
            def inputEvent = new GfxInputEvent(this, e, target)
            target."$mouseEventMethod"( inputEvent )
         }
     }

     private def getTargets(MouseEvent e){
         List targets = []
         def eventTargets = _context.eventTargets
         for(target in eventTargets.reverse()){
             //def bp = target.getBoundingShape(_context)
             //def s = target.runtime.transformedShape
             def s = target.runtime.getBoundingShape()
             if(s && s.contains(e.point)){
                 targets << target
                 if(!target.passThrough) break
             }
         }
         targets
     }
}