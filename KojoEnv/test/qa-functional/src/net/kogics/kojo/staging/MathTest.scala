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

class MathTest extends StagingTestBase {

  /* Testing manifest
   *
   * def constrain(value: Double, min: Double, max: Double) =
   * def norm(value: Double, low: Double, high: Double) =
   * def map(value: Double, low1: Double, high1: Double, low2: Double, high2: Double) =
   * def lerp(value1: Double, value2: Double, amt: Double) =
   * def sq(x: Double) = x * x
   * def dist(x0: Double, y0: Double, x1: Double, y1: Double) =
   * def dist(p1: Point, p2: Point) =
   * def mag(x: Double, y: Double) = dist(0, 0, x, y)
   * def mag(p: Point) = dist(0, 0, p.x, p.y)
   * val PI = math.Pi
   * val TWO_PI = 2*PI
   * val HALF_PI = PI/2
   * val QUARTER_PI = PI/4
   * def sin(a: Double) = math.sin(a)
   * def cos(a: Double) = math.cos(a)
   * def tan(a: Double) = math.tan(a)
   * def radians(deg: Double) = deg.toRadians
   * def degrees(rad: Double) = rad.toDegrees
   */

  @Test
  // lalit sez: if we have more than five tests, we run out of heap space - maybe
  // a leak in the Scala interpreter/compiler subsystem. So we run (mostly)
  // everything in one test
  def test1 = {
  //W
  //W==Math==
  //W
  //W{{{
  //Wconstrain(value, min, max)
    Tester("Staging.constrain(-22, -13, 78)", Some("Double = -13.0"))
    Tester("Staging.constrain(-2, -13, 78)", Some("Double = -2.0"))
    Tester("Staging.constrain(22, -13, 78)", Some("Double = 22.0"))
    Tester("Staging.constrain(82, -13, 78)", Some("Double = 78.0"))
  //W}}}
  //W
  //WConstrains _value_ to be no less than _min_ and no greater than _max_.
  //W
  //W{{{
  //Wnorm(value, low, high)
    Tester("Staging.norm(-22, -13, 78).toFloat", Some("Float = -0.0989011"))
    Tester("Staging.norm(-2, -13, 78).toFloat", Some("Float = 0.12087912"))
    Tester("Staging.norm(22, -13, 78).toFloat", Some("Float = 0.3846154"))
    Tester("Staging.norm(82, -13, 78).toFloat", Some("Float = 1.043956"))
  //W}}}
  //W
  //WNormalizes _value_ to the range 0.0 -- 1.0, such that _value_ == _low_
  //Wyields 0.0 and _value_ == _high_ yields 1.0, and values in between yield
  //Wthe corresponding fraction.  Values outside the range yield results < 0.0 or
  //W> 1.0, scaled to the _low_ / _high_ range.
  //W
  //W{{{
  //Wmap(value, min1, max1, min2, max2)
    Tester("Staging.map(-22, -13, 78, 5, 20).toFloat", Some("Float = 3.5164835"))
    Tester("Staging.map(-2, -13, 78, 5, 20).toFloat", Some("Float = 6.8131866"))
    Tester("Staging.map(22, -13, 78, 5, 20).toFloat", Some("Float = 10.769231"))
    Tester("Staging.map(82, -13, 78, 5, 20).toFloat", Some("Float = 20.65934"))
  //W}}}
  //W
  //WMaps _value_ from the range _low1_ -- _high1_ to the range
  //W_low2_ -- _high2_.
  //W
  //W{{{
  //Wsq(value)
    Tester("Staging.sq(-8)", Some("Double = 64.0"))
  //W}}}
  //W
  //WYields _value_ squared.
  //W
  //W{{{
  //Wdist(x0, y0, x1, y1)
    Tester("Staging.dist(10, 20, 40, 60)", Some("Double = 50.0"))
  //Wdist(p1, p2)
    Tester("Staging.dist(Staging.point(10, 20), Staging.point(40, 60))", Some("Double = 50.0"))
  //W}}}
  //W
  //WYields the distance between two points.
  //W
  //W{{{
  //Wmag(x, y)
    Tester("Staging.mag(30, -40)", Some("Double = 50.0"))
  //Wmag(p)
    Tester("Staging.mag(Staging.point(30, -40))", Some("Double = 50.0"))
  //W}}}
  //W
  //WYields the distance between the given point and the point of origin.
  //W
  //W{{{
  //Wlerp(low, high, value)
    Tester("Staging.lerp(-22, -13, 0.0).toFloat", Some("Float = -22.0"))
    Tester("Staging.lerp(-22, -13, 0.3).toFloat", Some("Float = -19.3"))
    Tester("Staging.lerp(-22, -13, 0.5).toFloat", Some("Float = -17.5"))
    Tester("Staging.lerp(-22, -13, 1.0).toFloat", Some("Float = -13.0"))
  //W}}}
  //W
  //WThe inverse of `norm`; yields a number in the range _low_ -- _high_
  //Wwhich corresponds to the position of _value_ in the range 0.0 -- 1.0.
  //W
  //W===Pi===
  //W
  //WFor convenience, some Processing-style pi-related constants:
  //W
  //W{{{
  //WPI
    Tester("Staging.PI", Some("Double = " + math.Pi))
  //WTWO_PI
    Tester("Staging.TWO_PI", Some("Double = " + 2 * math.Pi))
  //WHALF_PI
    Tester("Staging.HALF_PI", Some("Double = " + math.Pi / 2))
  //WQUARTER_PI
    Tester("Staging.QUARTER_PI", Some("Double = " + math.Pi / 4))
  //W}}}
  //W
  //W===Standard functions===
  //W
  //WWhile all standard mathematic functions can be used in Staging as
  //W`math.`_func_, some frequently used functions are methods in the Staging
  //WAPI as well (they call their standard-library equivalents).
  //W{{{
  //Wsin(value)
    Tester("Staging.sin(0)", Some("Double = 0.0"))
  //Wcos(value)
    Tester("Staging.cos(0)", Some("Double = 1.0"))
  //Wtan(value)
    Tester("Staging.tan(math.Pi/4)", Some("Double = 0.9999999999999999"))
  //W}}}
  //W
  //W===Conversions===
  //W
  //WAngles can be converted from degrees to radians and vice versa.
  //W{{{
  //Wradians(value)
    Tester("Staging.radians(0)", Some("Double = 0.0"))
    Tester("Staging.radians(180)", Some("Double = " + math.Pi))
  //Wdegrees(value)
    Tester("Staging.degrees(0)", Some("Double = 0.0"))
    Tester("Staging.degrees(math.Pi)", Some("Double = 180.0"))
  //W}}}
  //W
  }
}

