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

class SVGShapesTest extends StagingTestBase {
  val f = SpriteCanvas.instance.figure0
  def peekPNode = f.dumpLastChild

  /* Testing manifest
   * 
   * def svgShape(node: scala.xml.Node) = SvgShape(node)
   */

  @Test
  // lalit sez: if we have more than five tests, we run out of heap space - maybe a leak in the Scala interpreter/compiler
  // subsystem. So we run (mostly) everything in one test
  def test = {
    f.clear

    //W
    //W==SVG Shapes==
    //W
    //WA `rect` element takes a pair of _x_, _y_ coordinates for the lower left
    //Wcorner and a _width_ and _height_.  Example:
    //W
    //W{{{
    //WsvgShape(<rect x="15" y="15" width="25" height="5"/>)
    Tester("""import Staging._ ; svgShape(<rect x="15" y="15" width="25" height="5"/>)""")
    assertEquals("PPath(15,15 L40.0000,15.0000 L40.0000,20.0000 L15.0000,20.0000 " +
                 "L15.0000,15.0000 z M0.00000,0.00000 )",
                 makeString(peekPNode))

    //W}}}
    //W
    //WA `circle` element takes a pair of _x_, _y_ coordinates for the center,
    //Wand a _radius_.  Example:
    //W
    //W{{{
    //WsvgShape(<circle cx="15" cy="15" r="25"/>)
    Tester("""import Staging._ ; svgShape(<circle cx="15" cy="15" r="25"/>)""")
    assertEquals("PPath(-10,-10 C40.0000,28.8071 28.8071,40.0000 15.0000,40.0000 " +
                 "C1.19288,40.0000 -10.0000,28.8071 -10.0000,15.0000 " +
                 "C-10.0000,1.19288 1.19288,-10.0000 15.0000,-10.0000 " +
                 "C28.8071,-10.0000 40.0000,1.19288 40.0000,15.0000 z " +
                 "M0.00000,0.00000 )",
                 makeString(peekPNode))

    //W}}}
    //W
    //WAn `ellipse` element takes a pair of _x_, _y_ coordinates for the center,
    //Wa _horizontal radius_, and a _vertical radius_.  Example:
    //W
    //W{{{
    //WsvgShape(<ellipse cx="15" cy="15" rx="35" ry="25"/>)
    Tester("""import Staging._ ; svgShape(<ellipse cx="15" cy="15" rx="35" ry="25"/>)""")
    assertEquals("PPath(-20,-10 C50.0000,28.8071 34.3300,40.0000 15.0000,40.0000 " +
                 "C-4.32997,40.0000 -20.0000,28.8071 -20.0000,15.0000 " +
                 "C-20.0000,1.19288 -4.32997,-10.0000 15.0000,-10.0000 " +
                 "C34.3300,-10.0000 50.0000,1.19288 50.0000,15.0000 z " +
                 "M0.00000,0.00000 )",
                 makeString(peekPNode))

    //W}}}
    //W
    //WA `line` element takes two pairs of _x_, _y_ coordinates.  Example:
    //W
    //W{{{
    //WsvgShape(<line x1="15" y1="15" x2="40" y2="20"/>)
    Tester("""import Staging._ ; svgShape(<line x1="15" y1="15" x2="40" y2="20"/>)""")
    assertEquals("PPath(15,15 L40.0000,20.0000 M0.00000,0.00000 )",
                 makeString(peekPNode))

    //W}}}
    //W
    //WA `polyline` element takes a _points_ argument, which is a string of
    //Wcoordinates.  Example:
    //W
    //W{{{
    //WsvgShape(<polyline points="15,15 25,35 40,20 45,25 50,10"/>)
    Tester("""import Staging._ ; svgShape(<polyline points="15,15 25,35 40,20 45,25 50,10"/>)""")
    assertEquals("PPath(15,10 L25.0000,35.0000 L40.0000,20.0000 " +
                 "L45.0000,25.0000 L50.0000,10.0000 M0.00000,0.00000 )",
                 makeString(peekPNode))

    //W}}}
    //W
    //WA `polygon` element takes a _points_ argument, which is a string of
    //Wcoordinates.  Example:
    //W
    //W{{{
    //WsvgShape(<polygon points="15,15 25,35 40,20 45,25 50,10"/>)
    Tester("""import Staging._ ; svgShape(<polygon points="15,15 25,35 40,20 45,25 50,10"/>)""")
    assertEquals("PPath(15,10 L25.0000,35.0000 L40.0000,20.0000 " +
                 "L45.0000,25.0000 L50.0000,10.0000 z M0.00000,0.00000 )",
                 makeString(peekPNode))

    //W}}}
    //W
    //WA `path` element takes a _d_ argument, which is a string of path commands
    //Wwith following coordinates.  Example:
    //W
    //W{{{
    //WsvgShape(<path d="M15,15 40,15 40,20 15,20 z"/>)
    Tester("""import Staging._ ; svgShape(<path d="M15,15 40,15 40,20 15,20 z"/>)""")
    assertEquals("PPath(15,15 L40.0000,15.0000 L40.0000,20.0000 L15.0000,20.0000 " +
                 "z M0.00000,0.00000 )",
                 makeString(peekPNode))

    //W}}}
    //W

/*
  <g> and <svg> elements are temporarily unsupported
    //M
    //M===Syntax===
    //M
    //M{{{
    //MsvgShape(<g>... svg elements ...</g>)
    Tester("""import Staging._
             |svgShape(<g>
             |           <rect x="15" y="15" width="25" height="5"/>
             |           <circle cx="15" cy="15" r="25"/>
             |</g>)""".stripMargin)
    //println(makeString(peekPNode)) ; fail
    //*

    assertEquals("" +
                 "",
                 makeString(peekPNode))

    n += 1
    testPPath(f.dumpChild(n),
              "m-11.00,-11.00 C40.00,28.8071 28.8071,40.00 15.00,40.00 " +
              "C1.19288,40.00 -10.00,28.8071 -10.00,15.00 " +
              "C-10.00,1.19288 1.19288,-10.00 15.00,-10.00 " +
              "C28.8071,-10.00 40.00,1.19288 40.00,15.00 " +
              "z M0.00,0.00 ")
    n += 1
    //M}}}
    //M
    //Mdraws and returns multiple shapes.

    //M
    //M===Syntax===
    //M
    //M{{{
    //MsvgShape(<svg>... svg elements ...</svg>)
    Tester("""import Staging._
             |svgShape(<svg>
             |           <rect x="45" y="45" width="25" height="5"/>
             |           <g>
             |             <rect x="15" y="15" width="25" height="5"/>
             |             <circle cx="15" cy="15" r="25"/>
             |           </g>
             |</svg>)""".stripMargin)
    assertEquals("" +
                 "",
                 makeString(peekPNode))

    n += 2
    testPPath(f.dumpChild(n),
              "m-11.00,-11.00 C40.00,28.81 28.81,40.00 15.00,40.00 " +
              "C1.19,40.00 -10.00,28.81 -10.00,15.00 " +
              "C-10.00,1.19 1.19,-10.00 15.00,-10.00 " +
              "C28.81,-10.00 40.00,1.19 40.00,15.00 " +
              "z M0.00,0.00 ")
    assertEquals("" +
                 "",
                 makeString(peekPNode))

    n += 1
    //M}}}
    //M
    //Mdraws and returns multiple shapes.
*/

    //W
    //W===Styles for SVG shapes===
    //W
    //WFill and stroke color, and stroke width, can be set for the element's
    //Wshape by adding attributes `fill`, `stroke`, and `stroke-width`  The
    //Wfirst two take a color name or descriptor as argument, while the latter
    //Wtakes a number.  Example:
    //W
    //W{{{
    //WsvgShape(<rect x="15" y="15" stroke-width="4.9" width="250" stroke="navy" height="50" fill="none"/>)
    //W}}}
    //W

  }
}

