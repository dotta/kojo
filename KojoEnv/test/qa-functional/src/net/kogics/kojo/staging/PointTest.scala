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

//import org.junit.After
//import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import net.kogics.kojo.util._

class PointTest extends StagingTestBase {

  /* Testing manifest
   *
   * def point(x: Double, y: Double) = Point(x, y)
   * implicit def tupleDDToPoint(tuple: (Double, Double)) = Point(tuple._1, tuple._2)
   * implicit def tupleDIToPoint(tuple: (Double, Int)) = Point(tuple._1, tuple._2)
   * implicit def tupleIDToPoint(tuple: (Int, Double)) = Point(tuple._1, tuple._2)
   * implicit def tupleIIToPoint(tuple: (Int, Int)) = Point(tuple._1, tuple._2)
   * val O = Point(0, 0)
   */

  @Test
  // lalit sez: if we have more than five tests, we run out of heap space - maybe
  // a leak in the Scala interpreter/compiler subsystem. So we run (mostly)
  // everything in one test
  def test1 = {
  //W
  //W==Points==
  //W
  //WA point value can be created by calling the method
  //W
  //W{{{
  //Wpoint(xval, yval)
  //W}}}
  //W
    Tester("Staging.point(-22, -13)", Some("$net.kogics.kojo.core.Point = Point\\(-22[,.]00, -13[,.]00\\)"))
  //W
  //WThe constant `O` (capital o) has the same value of `point(0, 0)`.
    Tester("Staging.O", Some("$net.kogics.kojo.core.Point = Point\\(0[,.]00, 0[,.]00\\)"))

  //W
  //WPoint values can be added, subtracted, or negated
  //W
  //W{{{
  //Wpoint(10, 20) + point(25, 0)
    Tester(
      "Staging.point(10, 20) + Staging.point(25, 0)",
      Some("$net.kogics.kojo.core.Point = Point\\(35[,.]00, 20[,.]00\\)")
    )
  //W}}}
  //W
  //Wis the same as `point(35, 20)`
  //W
  //W{{{
  //Wpoint(35, 20) - point(25, 0)
    Tester(
      "Staging.point(35, 20) - Staging.point(25, 0)",
      Some("$net.kogics.kojo.core.Point = Point\\(10[,.]00, 20[,.]00\\)")
    )
  //W}}}
  //W
  //Wis the same as `point(10, 20)`
  //W
  //W{{{
  //W-point(10, -20)
    Tester(
      "-Staging.point(10, -20)",
      Some("$net.kogics.kojo.core.Point = Point\\(-10[,.]00, 20[,.]00\\)")
    )
  //W}}}
  //W
  //Wis the same as `point(-10, 20)`
  //W
  //WTuples of {{{Double}}}s or {{{Int}}}s are implicitly converted to
  //W{{{Point}}}s where applicable, if `Staging` has been imported.
    Tester("""import Staging._
             |var a = Staging.point(0,0)
             |a = (-22, -13)""".stripMargin,
      Some("$import Staging._" +
      "a: net.kogics.kojo.core.Point = Point\\(-22[,.]00, -13[,.]00\\)" +
      "a: net.kogics.kojo.core.Point = Point\\(-22[,.]00, -13[,.]00\\)"))
    Tester("""import Staging._
             |var a = Staging.point(0,0)
             |a = (-22., -13)""".stripMargin,
      Some("$import Staging._" +
      "a: net.kogics.kojo.core.Point = Point\\(-22[,.]00, -13[,.]00\\)" +
      "a: net.kogics.kojo.core.Point = Point\\(-22[,.]00, -13[,.]00\\)"))
    Tester("""import Staging._
             |var a = Staging.point(0,0)
             |a = (-22, -13.)""".stripMargin,
      Some("$import Staging._" +
      "a: net.kogics.kojo.core.Point = Point\\(-22[,.]00, -13[,.]00\\)" +
      "a: net.kogics.kojo.core.Point = Point\\(-22[,.]00, -13[,.]00\\)"))
    Tester("""import Staging._
             |var b = Staging.point(0,0)
             |b = (5., .45)""".stripMargin,
      Some("$import Staging._" +
      "b: net.kogics.kojo.core.Point = Point\\(5[,.]00, 0[,.]45\\)" +
      "b: net.kogics.kojo.core.Point = Point\\(5[,.]00, 0[,.]45\\)"))
  //W
  }
}

