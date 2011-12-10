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

class TurtleTest extends KojoTestBase {

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
  def testForward1 {
    turtle.forward(100)
    val p = turtle.position

    assertEquals(0, p.x, 0.001)
    assertEquals(100, p.y, 0.001)
  }

  @Test
  def testForward2 {
    turtle.turn(-90)
    turtle.forward(100)
    val p = turtle.position

    assertEquals(100, p.x, 0.001)
    assertEquals(0, p.y, 0.001)
  }

  @Test
  def testMotion1 {
    turtle.turn(45)
    turtle.forward(100)
    val p = turtle.position
    val l = Math.sqrt(100 * 100 / 2)

    assertEquals(-l, p.x, 0.001)
    assertEquals(l, p.y, 0.001)
  }

  @Test
  def testMotion2 {
    turtle.moveTo(-100, -100)
    turtle.turn(-45)
    turtle.forward(150)
    val p = turtle.position

    assertEquals(-250, p.x, 0.001)
    assertEquals(-100, p.y, 0.001)
  }

  @Test
  def testMotion3 {
    turtle.moveTo(-100, -100)
    turtle.setHeading(90)
    turtle.turn(-45)
    turtle.forward(Math.sqrt(2 * 100 * 100))
    val p = turtle.position

    assertEquals(0, p.x, 0.001)
    assertEquals(0, p.y, 0.001)
  }

  @Test
  def testTowards1 {
    turtle.towards(100, 100)
    assertEquals(45, turtle.heading, 0.001)
  }

  @Test
  def testTowards2 {
    turtle.towards(-100, 100)
    assertEquals(135, turtle.heading, 0.001)
  }

  @Test
  def testTowards3 {
    turtle.towards(-100, -100)
    assertEquals(225, turtle.heading, 0.001)
  }

  @Test
  def testTowards4 {
    turtle.towards(100, -100)
    assertEquals(315, turtle.heading, 0.001)
  }

  @Test
  def testTowardsRightLeft {
    turtle.jumpTo(100, 0)
    turtle.towards(0, 0)
    assertEquals(180, turtle.heading, 0.001)
  }

  @Test
  def testTowardsRightRight {
    turtle.jumpTo(100, 0)
    turtle.towards(200, 0)
    assertEquals(0, turtle.heading, 0.001)
  }

  @Test
  def testTowardsLeftRight {
    turtle.jumpTo(-100, 0)
    turtle.towards(0, 0)
    assertEquals(0, turtle.heading, 0.001)
  }

  @Test
  def testTowardsLeftLeft {
    turtle.jumpTo(-100, 0)
    turtle.towards(-200, 0)
    assertEquals(180, turtle.heading, 0.001)
  }

  @Test
  def testTowardsTopBottom {
    turtle.jumpTo(0, 100)
    turtle.towards(0, 0)
    assertEquals(270, turtle.heading, 0.001)
  }

  @Test
  def testTowardsTopTop {
    turtle.jumpTo(0, 100)
    turtle.towards(0, 200)
    assertEquals(90, turtle.heading, 0.001)
  }

  @Test
  def testTowardsBottomTop {
    turtle.jumpTo(0, -100)
    turtle.towards(0, 0)
    assertEquals(90, turtle.heading, 0.001)
  }

  @Test
  def testTowardsBottomBottom {
    turtle.jumpTo(0, -100)
    turtle.towards(0, -200)
    assertEquals(270, turtle.heading, 0.001)
  }

  @Test
  def testTurn {
    val theta0 = turtle.heading
    val turnSize = 30
    turtle.turn(turnSize)
    val theta1 = turtle.heading
    doublesEqual(theta1, theta0 + turnSize, 0.001)
  }

  @Test
  def testTurn2 {
    val theta0 = turtle.heading
    // failing test from earlier scalacheck run. but works here??
    val turnSize = 2147483647
    turtle.turn(turnSize)
    val theta1 = turtle.heading
    val e0 = theta0 + turnSize
    val expected = {
      if (e0 < 0) 360 + e0 % 360
      else if (e0 > 360)  e0 % 360
      else e0
    }
    doublesEqual(expected, theta1, 0.001)
  }

  @Test
  def testManyForwards {
    val propForward = forAll { stepSize: Int =>
      val pos0 = turtle.position
      turtle.forward(stepSize)
      val pos1 = turtle.position
      (doublesEqual(pos0.x, pos1.x, 0.001)
       && doublesEqual(pos0.y + stepSize, pos1.y, 0.001))
    }
    assertTrue(SCTest.check(propForward).passed)
  }

  @Test
  def testManyTurns {
    val propTurn = forAll { turnSize: Int =>
      val theta0 = turtle.heading
      turtle.turn(turnSize)
      val theta1 = turtle.heading
      val e0 = theta0 + turnSize
      val expected = {
        if (e0 < 0) 360 + e0 % 360
        else if (e0 > 360)  e0 % 360
        else e0
      }
      doublesEqual(expected, theta1, 0.001)
    }
    assertTrue(SCTest.check(propTurn).passed)
  }

  @Test
  def testManyTowardsQ1 {
    val propTowards = forAll { n: Double =>
      val x = Math.abs(n)
      val y = x+10
      turtle.towards(x, y)
      doublesEqual(Math.atan(y/x), deg2radians(turtle.heading), 0.001)
    }
    assertTrue(SCTest.check(propTowards).passed)
  }

  @Test
  def testManyTowardsQ2 {
    val propTowards = forAll { n: Double =>
      val x = -Math.abs(n)
      val y = Math.abs(n+20)
      turtle.towards(x, y)
      doublesEqual(Math.Pi + Math.atan(y/x), deg2radians(turtle.heading), 0.001)
    }
    assertTrue(SCTest.check(propTowards).passed)
  }

  @Test
  def testManyTowardsQ3 {
    val propTowards = forAll { n: Double =>
      val x = -Math.abs(n) - 1
      val y = x - 30
      turtle.towards(x, y)
      doublesEqual(Math.Pi + Math.atan(y/x), deg2radians(turtle.heading), 0.001)
    }
    assertTrue(SCTest.check(propTowards).passed)
  }

  @Test
  def testManyTowardsQ4 {
    val propTowards = forAll { n: Double =>
      val x = Math.abs(n)
      val y = -x - 10
      turtle.towards(x, y)
      doublesEqual(2*Math.Pi + Math.atan(y/x), deg2radians(turtle.heading), 0.001)
    }
    assertTrue(SCTest.check(propTowards).passed)
  }

  @Test
  def testManyMoveTos {
    val propMoveTo = forAll { n: Double =>
      val x = n
      val y = n+15
      turtle.moveTo(x, y)
      val pos1 = turtle.position
      (doublesEqual(x, pos1.x, 0.001)
       && doublesEqual(y, pos1.y, 0.001))
    }
    assertTrue(SCTest.check(propMoveTo).passed)
  }

  @Test
  def testDistanceTo1 {
    assertEquals(100, turtle.distanceTo(100, 0), 0.001)
  }

  @Test
  def testDistanceTo2 {
    assertEquals(100, turtle.distanceTo(0, 100), 0.001)
  }

  @Test
  def testDistanceTo3 {
    turtle.changePos(10, 10)
    assertEquals(Math.sqrt(90*90*2), turtle.distanceTo(100, 100), 0.001)
  }

  @Test
  def testDistanceTo4 {
    turtle.changePos(-10, -10)
    assertEquals(Math.sqrt(90*90*2), turtle.distanceTo(-100, -100), 0.001)
  }

  @Test
  def testDelayFor1 {
    turtle._animationDelay = 100
    assertEquals(100, turtle.delayFor(100))
  }

  @Test
  def testDelayFor2 {
    turtle._animationDelay = 100
    assertEquals(1000, turtle.delayFor(1000))
  }

  @Test
  def testDelayFor3 {
    turtle._animationDelay = 80
    assertEquals(240, turtle.delayFor(300))
  }

  @Test
  def testDisallowNegativeAnimDelay {
    try {
      turtle.setAnimationDelay(-1)
      fail("Negative Animation Delay Not Allowed")
    }
    catch {
      case ex: IllegalArgumentException => assertTrue(true)
    }
  }

  @Test
  def testDisallowNegativePenThickness {
    try {
      turtle.setPenThickness(-1)
      fail("Negative Pen Thickness Not Allowed")
    }
    catch {
      case ex: IllegalArgumentException => assertTrue(true)
    }
  }

  @Test
  def testStyleSaveRestore {
    turtle.setPenThickness(1)
    turtle.setPenColor(Color.blue)
    turtle.setFillColor(Color.green)
    turtle.setPenFontSize(19)
    assertEquals(Style(Color.blue, 1, Color.green, 19), turtle.style)
    turtle.saveStyle()
    turtle.setPenThickness(3)
    turtle.setPenColor(Color.green)
    turtle.setFillColor(Color.blue)
    turtle.setPenFontSize(29)
    assertEquals(Style(Color.green, 3, Color.blue, 29), turtle.style)
    turtle.restoreStyle()
    assertEquals(Style(Color.blue, 1, Color.green, 19), turtle.style)
  }
}
