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
package staging

import org.junit.Test
import org.junit.Assert._

import net.kogics.kojo.util._

class ControlTest extends StagingTestBase {

  /* Testing manifest
   *
   * def loop(fn: => Unit) = Impl.figure0.refresh(fn)
   * def stop = Impl.figure0.stopRefresh()
   * def clear() = {
   * def fgClear() = Impl.figure0.fgClear()
   * def mouseX() = Inputs.stepMousePos.x
   * def mouseY() = Inputs.stepMousePos.y
   * def pmouseX() = Inputs.prevMousePos.x
   * def pmouseY() = Inputs.prevMousePos.y
   * val LEFT = 1
   * val CENTER = 2
   * val RIGHT = 3
   * def mouseButton = Inputs.mouseBtn
   * def mousePressed = Inputs.mousePressedFlag
   * def interpolatePolygon(pts1: Seq[Point], pts2: Seq[Point], n: Int) {
   */

  @Test
  // lalit sez: if we have more than five tests, we run out of heap space - maybe
  // a leak in the Scala interpreter/compiler subsystem. So we run (mostly)
  // everything in one test
  def test1 = {
    //W
    //W==Control==
    //W
    //W{{{
    //Wloop { ...body... }
    //W}}}
    //W
    //WRepeatedly executes the lines in the body.
    //W
    //W{{{
    //Wstop
    Tester("""import Staging._
             |
             |var a = 2
             |loop {
             |  if (a <= 0) stop else a -= 1
             |}""".stripMargin)

    //W}}}
    //W
    //WStops a running `loop`.
    //W
    //W{{{
    //Wreset
    //Wwipe
    Tester("""import Staging._
             |
             |reset
             |var a = 2
             |loop {
             |  wipe
             |  if (a <= 0) stop else a -= 1
             |}""".stripMargin)

    //W}}}
    //W
    //W`reset` erases all shapes and makes the turtle invisible.  `wipe` erases
    //Wall shapes drawn within `loop`.
    //W
    //W{{{
    //WmouseX
    //WmouseY
    Tester("""import Staging._
             |
             |reset
             |var a = 2
             |loop {
             |  val b = mouseX
             |  val c = mouseY
             |  if (a <= 0) stop else a -= 1
             |}""".stripMargin)

    //W}}}
    //W
    //WWithin `loop`, gives the _x_ / _y_ coordinates of the mouse pointer.
    //W
    //W{{{
    //WpmouseX
    //WpmouseY
    Tester("""import Staging._
             |
             |reset
             |var a = 2
             |loop {
             |  val b = pmouseX
             |  val c = pmouseY
             |  if (a <= 0) stop else a -= 1
             |}""".stripMargin)

    //W}}}
    //W
    //WWithin `loop`, gives the _x_ / _y_ coordinates of the mouse pointer
    //Wfrom the _previous_ iteration of `loop`.
    //W
    //W{{{
    //WmouseButton
    //W}}}
    //W
    //WHolds the number of the last mouse button pressed (0: none, 1: left,
    //W2: center, 3: right).  The constants `LEFT`, `CENTER`, `RIGHT` are
    //Wprovided.
    //W
    //W{{{
    //WmousePressed
    Tester("""import Staging._
             |
             |reset
             |var a = 2
             |loop {
             |  if (mousePressed) mouseButton match {
             |    case LEFT   => println("foo")
             |    case CENTER => println("bar")
             |    case RIGHT  => println("baz")
             |  }
             |  if (a <= 0) stop else a -= 1
             |}""".stripMargin)

    //W}}}
    //W
    //WYields `true` if any mouse button is pressed and `false` otherwise.
    //W

  }
}

