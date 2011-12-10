/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import org.scalacheck.{Test => SCTest}
import org.scalacheck.Prop.forAll

import edu.umd.cs.piccolo.PCanvas

import net.kogics.kojo.util.Utils._
import core.Style
import java.awt.Color

class TurtleUndoTest extends KojoTestBase {

  val turtle = new Turtle(SpriteCanvas.instance, "/images/turtle32.png")

  @Before
  def setUp: Unit = {
    turtle.init()
    turtle.setAnimationDelay(0)
  }

  @After
  def tearDown: Unit = {
  }

  @Test
  def testForwardUndo1 {
    val s0 = turtle.state
    turtle.forward(100)
    turtle.undo()
    val s1 = turtle.state

    assertEquals(s0, s1)
  }

  @Test
  def testManyForwardsUndo {
    val propForwardUndo = forAll { stepSize: Int =>
      val s0 = turtle.state
      turtle.forward(stepSize)
      turtle.undo()
      val s1 = turtle.state

      s0 == s1
    }
    assertTrue(SCTest.check(propForwardUndo).passed)
  }

  @Test
  def testTurnUndo1 {
    val s0 = turtle.state
    turtle.turn(40)
    turtle.undo()
    val s1 = turtle.state

    assertEquals(s0, s1)
  }

  @Test
  def testManyTurnsUndo {
    val propTurn = forAll { turnSize: Int =>
      val s0 = turtle.state
      turtle.turn(turnSize)
      turtle.undo()
      val s1 = turtle.state

      s0 == s1
    }
    assertTrue(SCTest.check(propTurn).passed)
  }

  @Test
  def testColorfulCircleUndo {
    val random = new java.util.Random
    def randomColor = new java.awt.Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))

    var states: List[SpriteState] = Nil
    states = turtle.state :: states

    for (idx <- 1 to 360) {
      turtle.setPenColor(randomColor)
      states = turtle.state :: states
      turtle.setPenThickness(idx)
      states = turtle.state :: states
      turtle.setFillColor(randomColor)
      states = turtle.state :: states
      turtle.forward(1)
      states = turtle.state :: states
      turtle.turn(1)
      states = turtle.state :: states
    }

    states = states.tail
    for (idx <- 1 to 360*5) {
      turtle.undo()
      assertEquals(states.head, turtle.state)
      states = states.tail
    }
  }

  @Test
  def testMoveToUndo1 {
    val s0 = turtle.state
    turtle.moveTo(100, 100)
    turtle.undo()
    val s1 = turtle.state

    assertEquals(s0, s1)
  }

  @Test
  def testManyMoveToUndo {
    val propForwardUndo = forAll { stepSize: Int =>
      val s0 = turtle.state
      turtle.moveTo(100, 100)
      turtle.undo()
      val s1 = turtle.state

      s0 == s1
    }
    assertTrue(SCTest.check(propForwardUndo).passed)
  }

  @Test
  def testStyleRestoreUndo1 {
    // style 1
    turtle.setPenThickness(1)
    turtle.setPenColor(Color.blue)
    turtle.setFillColor(Color.green)
    turtle.setPenFontSize(11)
    turtle.saveStyle()

    // style 2
    turtle.setPenThickness(3)
    turtle.setPenColor(Color.green)
    turtle.setFillColor(Color.blue)
    turtle.setPenFontSize(15)
    assertEquals(Style(Color.green, 3, Color.blue, 15), turtle.style)

    // change to style 1
    turtle.restoreStyle()
    assertEquals(Style(Color.blue, 1, Color.green, 11), turtle.style)

    // undo style 1 change. Back to style 2
    turtle.undo()
    assertEquals(Style(Color.green, 3, Color.blue, 15), turtle.style)

    // undo 4 steps of setting style 2
    turtle.undo()
    turtle.undo()
    turtle.undo()
    turtle.undo()
    // back to style 1
    assertEquals(Style(Color.blue, 1, Color.green, 11), turtle.style)
  }

  @Test
  def testStyleRestoreUndo2 {

    turtle.setPenThickness(5)
    turtle.saveStyle()
    turtle.setPenThickness(10)
    assertEquals(10, turtle.style.penThickness, 0.001)
    turtle.restoreStyle()
    assertEquals(5, turtle.style.penThickness, 0.001)

    // undo restore
    turtle.undo()
    assertEquals(10, turtle.style.penThickness, 0.001)

    // make sure re-restore works
    turtle.restoreStyle()
    assertEquals(5, turtle.style.penThickness, 0.001)

    // undo restore back to 10
    turtle.undo()

    // undo setting to 10
    turtle.undo()
    assertEquals(5, turtle.style.penThickness, 0.001)
  }

  @Test
  def testStyleRestoreUndo3 {
    // style 1
    turtle.setPenThickness(1)
    turtle.setPenColor(Color.blue)
    turtle.setFillColor(Color.green)
    turtle.setPenFontSize(51)
    turtle.saveStyle()

    // style 2
    turtle.setPenThickness(3)
    turtle.setPenColor(Color.green)
    turtle.setFillColor(Color.blue)
    turtle.setPenFontSize(31)
    assertEquals(Style(Color.green, 3, Color.blue, 31), turtle.style)
    assertEquals(3, turtle.penPaths.last.strokeThickness, 0.001)
 
    // change to style 1
    turtle.restoreStyle()
    assertEquals(Style(Color.blue, 1, Color.green, 51), turtle.style)
    assertEquals(1, turtle.penPaths.last.strokeThickness, 0.001)

    // undo style 1 change. Back to style 2
    turtle.undo()
    assertEquals(Style(Color.green, 3, Color.blue, 31), turtle.style)
    assertEquals(3, turtle.penPaths.last.strokeThickness, 0.001)
  }
}
