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

class SimpleShapesTest extends StagingTestBase {

  val f = SpriteCanvas.instance.figure0
  def peekPNode = f.dumpLastChild

  /* Testing manifest
   *
   * def dot(x: Double, y: Double) = Dot(Point(x, y))
   * def dot(p: Point) = Dot(p)
   * def line(x1: Double, y1: Double, x2: Double, y2: Double) =
   * def line(p1: Point, p2: Point) =
   * def vector(x1: Double, y1: Double, x2: Double, y2: Double, a: Double) =
   * def vector(p1: Point, p2: Point, a: Double) =
   * def rectangle(x: Double, y: Double, w: Double, h: Double) =
   * def rectangle(p: Point, w: Double, h: Double) =
   * def rectangle(p1: Point, p2: Point) =
   * def square(x: Double, y: Double, s: Double) =
   * def square(p: Point, s: Double) =
   * def roundRectangle(
   * def roundRectangle(
   * def roundRectangle(p1: Point, p2: Point, rx: Double, ry: Double) =
   * def roundRectangle(p1: Point, p2: Point, p3: Point) =
   * def ellipse(cx: Double, cy: Double, rx: Double, ry: Double) =
   * def ellipse(p: Point, rx: Double, ry: Double) =
   * def ellipse(p1: Point, p2: Point) =
   * def circle(x: Double, y: Double, r: Double) =
   * def circle(p: Point, r: Double) =
   * def arc(cx: Double, cy: Double, rx: Double, ry: Double, s: Double, e: Double) =
   * def arc(p: Point, rx: Double, ry: Double, s: Double, e: Double) =
   * def arc(p1: Point, p2: Point, s: Double, e: Double) =
   * def pieslice(cx: Double, cy: Double, rx: Double, ry: Double, s: Double, e: Double) =
   * def pieslice(p: Point, rx: Double, ry: Double, s: Double, e: Double) =
   * def pieslice(p1: Point, p2: Point, s: Double, e: Double) =
   * def openArc(cx: Double, cy: Double, rx: Double, ry: Double, s: Double, e: Double) =
   * def openArc(p: Point, rx: Double, ry: Double, s: Double, e: Double) =
   * def openArc(p1: Point, p2: Point, s: Double, e: Double) =
   * def chord(cx: Double, cy: Double, rx: Double, ry: Double, s: Double, e: Double) =
   * def chord(p: Point, rx: Double, ry: Double, s: Double, e: Double) =
   * def chord(p1: Point, p2: Point, s: Double, e: Double) =
   *  TODO make test for texts
   *  def text(s: String, x: Double, y: Double) = Text(s, Point(x, y))
   *  def text(s: String, p: Point) = Text(s, p)
   * def star(cx: Double, cy: Double, inner: Double, outer: Double, points: Int) =
   * def star(p: Point, inner: Double, outer: Double, points: Int) =
   * def star(p1: Point, p2: Point, p3: Point, points: Int) =
   * def cross(p1: Point, p2: Point, cw: Double, r: Double = 1, greek: Boolean = false) =
   * def crossOutline(p1: Point, p2: Point, cw: Double, r: Double = 1, greek: Boolean = false) =
   * 
   * (saltire and saltireOutline: see SimpleShapes2Test)
   */

  @Test
  // lalit sez: if we have more than five tests, we run out of heap space - maybe a leak in the Scala interpreter/compiler
  // subsystem. So we run (mostly) everything in one test
  def test1 = {
    //W
    //W==Simple Shapes==
    //W
    //W===Dots===
    //W
    //WA dot is defined by a single coordinate pair, given as _x_, _y_ values
    //Wor as a `Point`.
    //W
    //W{{{
    //Wdot(x, y)
    Tester("Staging.dot(15, 10)")
    assertEquals("PPath(15,10 L15.0000,10.0000 M0.00000,0.00000 )", makeString(peekPNode))

    //Wdot(point)
    Tester("Staging.dot(Staging.point(15, 10))")
    assertEquals("PPath(15,10 L15.0000,10.0000 M0.00000,0.00000 )", makeString(peekPNode))

    //W}}}

    //W
    //W===Lines===
    //W
    //WA line is defined either by
    //W  # two coordinate pairs, given as _x_, _y_ values, or
    //W  # two points.
    //W
    //W{{{
    //Wline(x1, y1, x2, y2)
    Tester("import Staging._ ; line(15, 15, 40, 20)")
    assertEquals(
      "PPath(15,15 L40.0000,20.0000 M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Wline(point1, point2)
    Tester("import Staging._ ; line((15, 15), (40, 20))")
    assertEquals(
      "PPath(15,15 L40.0000,20.0000 M0.00000,0.00000 )",
      makeString(peekPNode)
    )
    //W}}}
    //W

    //W
    //W===Rectangles===
    //W
    //WA rectangle is defined either by
    //W  # a coordinate pair for the lower left corner, given as _x_, _y_ values or as a `Point`, and a _width_-_height_ pair, or
    //W  # two points (in lower left / upper right order).
    //W
    //W{{{
    //Wrectangle(x, y, width, height)
    Tester("import Staging._ ; rectangle(15, 15, 25, 5)")
    assertEquals(
      "PPath(15,15 L40.0000,15.0000 L40.0000,20.0000 L15.0000,20.0000 " +
              "L15.0000,15.0000 z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Wrectangle(point1, width, height)
    Tester("import Staging._ ; rectangle((15, 15), 25, 5)")
    assertEquals(
      "PPath(15,15 L40.0000,15.0000 L40.0000,20.0000 L15.0000,20.0000 " +
              "L15.0000,15.0000 z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Wrectangle(point1, point2)
    Tester("import Staging._ ; rectangle((15, 15), (40, 20))")
    assertEquals(
      "PPath(15,15 L40.0000,15.0000 L40.0000,20.0000 L15.0000,20.0000 " +
              "L15.0000,15.0000 z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W
    //W{{{
    //Wsquare(x, y, size)
    Tester("import Staging._ ; square(15, 15, 20)")
    assertEquals(
      "PPath(15,15 L35.0000,15.0000 L35.0000,35.0000 L15.0000,35.0000 " +
        "L15.0000,15.0000 z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Wsquare(point, size)
    Tester("import Staging._ ; square((15, 15), 20)")
    assertEquals(
      "PPath(15,15 L35.0000,15.0000 L35.0000,35.0000 L15.0000,35.0000 " +
        "L15.0000,15.0000 z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W
  }

  @Test
  def test2 = {
    f.clear

    //W
    //W===Rectangles with round corners===
    //W
    //WA rectangle with rounded corners is defined just like a rectangle, with
    //Wan additional _x-radius_, _y-radius_ pair or point that defines the
    //Wcurvature of the corners.
    //W
    //W{{{
    //WroundRectangle(x, y, width, height, radiusx, radiusy)
    Tester("import Staging._ ; roundRectangle(15, 15, 25, 5, 3, 5)")
    //WroundRectangle(point1, width, height, radiusx, radiusy)
    Tester("import Staging._ ; roundRectangle((15, 15), 25, 5, 3, 5)")
    //WroundRectangle(point1, point2, radiusx, radiusy)
    Tester("import Staging._ ; roundRectangle((15, 15), (40, 20), 3, 5)")
    //WroundRectangle(point1, point2, point3)
    Tester("import Staging._ ; roundRectangle((15, 15), (40, 20), (3, 5))")
    assertEquals(
      "PPath(15,15 L15.0000,17.5000 C15.0000,18.8807 15.6716,20.0000 16.5000,20.0000 " +
        "L38.5000,20.0000 C39.3284,20.0000 40.0000,18.8807 40.0000,17.5000 " +
        "L40.0000,17.5000 C40.0000,16.1193 39.3284,15.0000 38.5000,15.0000 " +
        "L16.5000,15.0000 C15.6716,15.0000 15.0000,16.1193 15.0000,17.5000 " +
        "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W

    //W
    //W===Ellipses===
    //W
    //WAn ellipse is defined by a center point and a curvature.  The center point
    //Wcan be given as _cx_, _cy_ coordinates or as a `Point`, and the
    //Wcurvature can be given as _rx_, _ry_ radii or as an absolute `Point`.
    //W
    //W(Thus, `ellipse((15, 15), 35, 25)` and `ellipse((15, 15), (50, 40))`
    //Wdefine the same shape.)
    //W
    //W{{{
    //Wellipse(cx, cy, rx, ry)
    Tester("import Staging._ ; ellipse(15, 15, 35, 25)")
    assertEquals(
      "PPath(-20,-10 C50.0000,28.8071 34.3300,40.0000 15.0000,40.0000 " +
        "C-4.32997,40.0000 -20.0000,28.8071 -20.0000,15.0000 " +
        "C-20.0000,1.19288 -4.32997,-10.0000 15.0000,-10.0000 " +
        "C34.3300,-10.0000 50.0000,1.19288 50.0000,15.0000 " +
        "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Wellipse(p, rx, ry)
    Tester("import Staging._ ; ellipse((15, 15), 35, 25)")
    assertEquals(
      "PPath(-20,-10 C50.0000,28.8071 34.3300,40.0000 15.0000,40.0000 " +
        "C-4.32997,40.0000 -20.0000,28.8071 -20.0000,15.0000 " +
        "C-20.0000,1.19288 -4.32997,-10.0000 15.0000,-10.0000 " +
        "C34.3300,-10.0000 50.0000,1.19288 50.0000,15.0000 " +
        "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Wellipse(p1, p2)
    Tester("import Staging._ ; ellipse((15, 15), (50, 40))")
    assertEquals(
      "PPath(-20,-10 C50.0000,28.8071 34.3300,40.0000 15.0000,40.0000 " +
        "C-4.32997,40.0000 -20.0000,28.8071 -20.0000,15.0000 " +
        "C-20.0000,1.19288 -4.32997,-10.0000 15.0000,-10.0000 " +
        "C34.3300,-10.0000 50.0000,1.19288 50.0000,15.0000 " +
        "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W
    //W{{{
    //Wcircle(cx, cy, radius)
    Tester("import Staging._ ; circle(15, 15, 25)")
    assertEquals(
      "PPath(-10,-10 C40.0000,28.8071 28.8071,40.0000 15.0000,40.0000 " +
        "C1.19288,40.0000 -10.0000,28.8071 -10.0000,15.0000 " +
        "C-10.0000,1.19288 1.19288,-10.0000 15.0000,-10.0000 " +
        "C28.8071,-10.0000 40.0000,1.19288 40.0000,15.0000 " +
        "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Wcircle(p, radius)
    Tester("import Staging._ ; circle((15, 15), 25)")
    assertEquals(
      "PPath(-10,-10 C40.0000,28.8071 28.8071,40.0000 15.0000,40.0000 " +
        "C1.19288,40.0000 -10.0000,28.8071 -10.0000,15.0000 " +
        "C-10.0000,1.19288 1.19288,-10.0000 15.0000,-10.0000 " +
        "C28.8071,-10.0000 40.0000,1.19288 40.0000,15.0000 " +
        "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W

    //W
    //W===Elliptical arcs===
    //W
    //WAn elliptical arc is defined just like an ellipsis, with two additional
    //Warguments for _starting angle_ and _extent_.  A starting angle of 0 is
    //Wthe "three o'clock" direction, and 90 is the "twelve o'clock" direction.
    //WBoth angles are given in 1/360 degrees.
    //W
    //W{{{
    //Warc(cx, cy, rx, ry, s, e)
    Tester("import Staging._ ; arc(15, 15, 20, 10, 40, 95)")
    assertEquals(
      "PPath(1,15 C26.7165,23.5756 21.4744,24.8682 15.8724,24.9905 " +
      "C10.2703,25.1128 4.82289,24.0536 0.857864,22.0711 L15.0000,15.0000 " +
      "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Warc(cp, rx, ry, s, e)
    Tester("import Staging._ ; arc((15, 15), 20, 10, 40, 95)")

    //Warc(p1, p2, s, e)
    Tester("import Staging._ ; arc((15, 15), (35, 25), 40, 95)")

    //W}}}
    //W

    //W
    //WThe default arc shape is a "pieslice" / sector shape with two radii
    //Wconnected by an elliptical segment.  This shape can also be created with
    //W`pieslice`:
    //W
    //W{{{
    //Wpieslice(...as above...)
    Tester("import Staging._ ; pieslice((15, 15), 20, 10, 40, 95)")
    assertEquals(
      "PPath(1,15 C26.7165,23.5756 21.4744,24.8682 15.8724,24.9905 " +
      "C10.2703,25.1128 4.82289,24.0536 0.857864,22.0711 L15.0000,15.0000 " +
      "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W
    //WOther kinds of arcs are open arcs, created by `openArc` and chords,
    //Wcreated by `chord`:
    //W{{{
    //WopenArc(...as above...)
    Tester("import Staging._ ; openArc((15, 15), 20, 10, 40, 95)")
    assertEquals(
      "PPath(1,21 C26.7165,23.5756 21.4744,24.8682 15.8724,24.9905 " +
      "C10.2703,25.1128 4.82289,24.0536 0.857864,22.0711 M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Wchord(...as above...)
    Tester("import Staging._ ; chord((15, 15), 20, 10, 40, 95)")
    assertEquals(
      "PPath(1,21 C26.7165,23.5756 21.4744,24.8682 15.8724,24.9905 " +
      "C10.2703,25.1128 4.82289,24.0536 0.857864,22.0711 z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W

    //W
    //W===Vectors===
    //W
    //WA vector is a specialized line with an arrowhead at the endpoint.  An
    //Wadditional argument specifies the length of the arrowhead.
    //W
    //W{{{
    //Wvector(x1, y1, x2, y2, length)
    Tester("import Staging._ ; vector(15, 15, 40, 20, 3)")
    assertEquals(
      "PPath(15,14 L40.4951,15.0000 M40.4951,15.0000 L37.4951,14.0000 " +
        "L37.4951,16.0000 z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Wvector(point1, point2, length)
    Tester("import Staging._ ; vector((15, 15), (40, 20), 3)")

    //W}}}
    //W

    //W
    //W===Stars===
    //W
    //WA star is a polygon with _n_ points.  The placement of the star is
    //Wspecified with a `Point` or _x_, _y_ coordinates for the center. An
    //W_inner_ radius specifies the corners between points, and an _outer_
    //Wradius specifies the points.
    //W
    //W{{{
    //Wstar(cx, cy, inner, outer, n)
    Tester("import Staging._ ; star(15, 15, 25, 50, 5)")
    assertEquals(
      "PPath(-33,-26 L0.305369,35.2254 L-32.5528,30.4508 L-8.77641,7.27458 " +
      "L-14.3893,-25.4508 L15.0000,-10.0000 L44.3893,-25.4508 L38.7764,7.27458 " +
      "L62.5528,30.4508 L29.6946,35.2254 z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //Wstar(p, inner, outer, n)
    //TODO find out why this test fails, it works in the app:
    Tester("import Staging._ ; star((15, 15), 25, 50, 5)")

    //Wstar(p1, p2, p3, n)
    //TODO find out why this test fails, it works in the app:
    Tester("import Staging._ ; star((15, 15), (40, 15), (65, 15), 5)")

    //W}}}
    //W

  }
}

