/*
 * Copyright (C) 2010 Peter Lewerin <peter.lewerin@tele2.se>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.kojo
package staging

import util.Utils

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._
import edu.umd.cs.piccolo.util._
import edu.umd.cs.piccolo.event._

//import net.kogics.kojo.util.Utils

import javax.swing._

import core._

object Inputs {
  import edu.umd.cs.piccolo.event._
  //import java.awt.event.InputEvent

  @volatile
  var mousePos: Point = API.O
  @volatile
  var prevMousePos: Point = API.O
  @volatile
  var stepMousePos: Point = API.O
  @volatile
  var mouseBtn = 0
  @volatile
  var mousePressedFlag = false

  def activityStep() = {
    prevMousePos = stepMousePos
    stepMousePos = mousePos
  }

  def init() {
    Utils.runInSwingThread {
      val iel = new PBasicInputEventHandler {
        // This method is invoked when a node gains the keyboard focus.
        override def keyboardFocusGained(e: PInputEvent) {
          e match {
            case ee =>
              //println("keyboardFocusGained: e=" + ee)
          }
        }
        // This method is invoked when a node loses the keyboard focus.
        override def keyboardFocusLost(e: PInputEvent) {
          e match {
            case ee =>
              //println("keyboardFocusLost: e=" + ee)
          }
        }
        // Will get called whenever a key has been pressed down.
        override def keyPressed(e: PInputEvent) {
          e match {
            case ee =>
              //println("keyPressed: e=" + ee)
          }
        }
        // Will get called whenever a key has been released.
        override def keyReleased(e: PInputEvent) {
          e match {
            case ee =>
              //println("keyReleased: e=" + ee)
          }
        }
        // Will be called at the end of a full keystroke (down then up).
        override def keyTyped(e: PInputEvent) {
          e match {
            case ee =>
              //println("keyTyped: e=" + ee)
          }
        }
        // Will be called at the end of a full click (mouse pressed followed by mouse released).
        override def mouseClicked(e: PInputEvent) {
          super.mouseClicked(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseBtn = e.getButton
          e match {
            case ee =>
              //println("mouseClicked: e=" + ee)
          }
        }
        // Will be called when a drag is occurring.
        override def mouseDragged(e: PInputEvent) {
          super.mouseDragged(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          e match {
            case ee =>
              //println("mouseDragged: e=" + ee)
          }
        }
        // Will be invoked when the mouse enters a specified region.
        override def mouseEntered(e: PInputEvent) {
          super.mouseEntered(e)
          //e.pushCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR))
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          e match {
            case ee =>
              //println("mouseEntered: e=" + ee)
          }
        }
        // Will be invoked when the mouse leaves a specified region.
        override def mouseExited(e: PInputEvent) {
          super.mouseExited(e)
          //e.popCursor
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mousePressedFlag = false
          e match {
            case ee =>
              //println("mouseExited: e=" + ee)
          }
        }
        // Will be called when the mouse is moved.
        override def mouseMoved(e: PInputEvent) {
          super.mouseMoved(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          e match {
            case ee =>
              //println("mouseMoved: e=" + ee)
          }
        }
        // Will be called when a mouse button is pressed down.
        override def mousePressed(e: PInputEvent) {
          super.mousePressed(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseBtn = e.getButton
          mousePressedFlag = true
          e match {
            case ee =>
              //println("mousePressed: e=" + ee)
          }
//          Impl.canvas.getRoot.getDefaultInputManager.setKeyboardFocus(null)
        }
        // Will be called when any mouse button is released.
        override def mouseReleased(e: PInputEvent) {
          super.mouseReleased(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseBtn = e.getButton
          mousePressedFlag = false
          e match {
            case ee =>
              //println("mouseReleased: e=" + ee)
          }
        }
        // This method is invoked when the mouse wheel is rotated.
        override def mouseWheelRotated(e: PInputEvent) {
          super.mouseWheelRotated(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          e match {
            case ee =>
              //println("mouseWheelRotated: e=" + ee)
          }
        }
        // This method is invoked when the mouse wheel is rotated by a block.
        override def mouseWheelRotatedByBlock(e: PInputEvent) {
          super.mouseWheelRotatedByBlock(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          e match {
            case ee =>
              //println("mouseWheelRotatedByBlock: e=" + ee)
          }
        }
      }

      //iel.setEventFilter(new PInputEventFilter(PInputEventFilter.ALL_MODIFIERS_MASK))
      //InputEvent.
      //KEY_EVENT_MASK, MOUSE_EVENT_MASK, MOUSE_MOTION_EVENT_MASK, MOUSE_WHEEL_EVENT_MASK,

      Impl.canvas.addInputEventListener(iel)
    }
  }
}  
