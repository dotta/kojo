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
package net.kogics.kojo
package turtle

import java.awt.Color

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.CountDownLatch
import edu.umd.cs.piccolo.nodes.PText

import core.Style

object Command {
  val AlwaysValid = new AtomicBoolean(true)
}

abstract sealed class Command(val valid: AtomicBoolean)
case class Forward(n: Double, v: AtomicBoolean) extends Command(v)
case class Turn(angle: Double, v: AtomicBoolean)  extends Command(v)
case class Clear(v: AtomicBoolean) extends Command(v)
case class Remove(v: AtomicBoolean) extends Command(v)
case class PenUp(v: AtomicBoolean) extends Command(v)
case class PenDown(v: AtomicBoolean) extends Command(v)
case class Towards(x: Double, y: Double, v: AtomicBoolean) extends Command(v)
case class JumpTo(x: Double, y: Double, v: AtomicBoolean) extends Command(v)
case class MoveTo(x: Double, y: Double, v: AtomicBoolean) extends Command(v)
case class SetAnimationDelay(d: Long, v: AtomicBoolean) extends Command(v)
case class GetAnimationDelay(latch: CountDownLatch, v: AtomicBoolean) extends Command(v)
case class GetPosition(latch: CountDownLatch, v: AtomicBoolean) extends Command(v)
case class GetHeading(latch: CountDownLatch, v: AtomicBoolean) extends Command(v)
case class SetPenColor(color: Color, v: AtomicBoolean) extends Command(v)
case class SetPenThickness(t: Double, v: AtomicBoolean) extends Command(v)
case class SetFontSize(n: Int, v: AtomicBoolean) extends Command(v)
case class SetFillColor(color: Color, v: AtomicBoolean) extends Command(v)
case class SaveStyle(v: AtomicBoolean) extends Command(v)
case class RestoreStyle(v: AtomicBoolean) extends Command(v)
case class GetStyle(latch: CountDownLatch, v: AtomicBoolean) extends Command(v)
case class BeamsOn(v: AtomicBoolean) extends Command(v)
case class BeamsOff(v: AtomicBoolean) extends Command(v)
case class Write(text: String, v: AtomicBoolean) extends Command(v)
case class Show(v: AtomicBoolean) extends Command(v)
case class Hide(v: AtomicBoolean) extends Command(v)
case object CommandDone
case object Undo extends Command(Command.AlwaysValid)
case class GetState(latch: CountDownLatch, v: AtomicBoolean) extends Command(v)
case class PlaySound(voice: core.Voice, v: AtomicBoolean) extends Command(v)

abstract sealed class UndoCommand
case class UndoChangeInPos(oldPos: (Double, Double)) extends UndoCommand
case class UndoChangeInHeading(oldHeading: Double) extends UndoCommand
case class UndoPenAttrs(color: Color, thickness: Double, fillColor: Color, fontSize: Int) extends UndoCommand
case class UndoPenState(currPen: Pen) extends UndoCommand
case class UndoWrite(ptext: PText) extends UndoCommand
case class UndoVisibility(visible: Boolean, beamsOn: Boolean) extends UndoCommand
case class UndoSaveStyle() extends UndoCommand
case class UndoRestoreStyle(currStyle: Style, savedStyle: Style) extends UndoCommand

case class CompositeUndoCommand(cmds: List[UndoCommand]) extends UndoCommand

case class SpriteState(posx: Long, posy: Long,
                       heading: Long,
                       color: Color, thickness: Long, fillColor: Color,
                       currPen: Pen,
                       textNodes: List[PText],
                       visible: Boolean, beamsOn: Boolean)