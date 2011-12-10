/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.core
import java.awt.Color

case class Style(val penColor: Color, val penThickness: Double, val fillColor: Color, val fontSize: Int)

trait TurtleMover extends RichTurtleCommands {
  def forward(n: Double): Unit
  def turn(angle: Double): Unit
  def penUp(): Unit
  def penDown(): Unit
  def setPenColor(color: Color): Unit
  def setPenThickness(t: Double): Unit
  def setFillColor(color: Color): Unit
  def saveStyle(): Unit
  def restoreStyle(): Unit
  def style: Style
  def towards(x: Double, y: Double): Unit
  def towards(p: Point): Unit = towards(p.x, p.y)
  def position: Point
  def heading: Double
  def jumpTo(x: Double, y: Double): Unit
  def jumpTo(p: Point): Unit = jumpTo(p.x, p.y)
  def setPosition(x: Double, y: Double) = jumpTo(x, y)
  def setPosition(p: Point): Unit = jumpTo(p)
  def moveTo(x: Double, y: Double): Unit
  def moveTo(p: Point): Unit = moveTo(p.x, p.y)
  def setHeading(angle: Double) = turn(angle - heading)
  def home() = {
    moveTo(0, 0)
    setHeading(90)
  }
  def animationDelay: Long
  def setAnimationDelay(d: Long)
  def beamsOn(): Unit
  def beamsOff(): Unit
  def write(text: String): Unit
  def write(obj: Any): Unit = write(obj.toString)
  def visible(): Unit
  def invisible(): Unit
  def clear(): Unit
  def undo(): Unit
  def waitFor(): Unit = animationDelay
  def playSound(voice: Voice): Unit
  def setPenFontSize(n: Int)
}
