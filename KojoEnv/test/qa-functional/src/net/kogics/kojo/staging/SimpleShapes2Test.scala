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

class SimpleShapes2Test extends StagingTestBase {

  val f = SpriteCanvas.instance.figure0
  def peekPNode = f.dumpLastChild

  /* Testing manifest
   *
   * def saltire(p1: Point, p2: Point, s: Double) = Saltire(p1, p2, s)
   * def saltireOutline(p1: Point, p2: Point, s: Double) = SaltireOutline(p1, p2, s)
   */

  @Test
  // lalit sez: if we have more than five tests, we run out of heap space - maybe a leak in the Scala interpreter/compiler
  // subsystem. So we run (mostly) everything in one test
  def test1 = {
    f.clear
    
    //W
    //W===Crosses===
    //W
    //WA cross is defined by rectangular dimensions (given as two `point`s), a
    //Wcross width `cw`, a ratio `r`, and a flag `greek` which is false by default.
    //W
    //WThe horizontal beam of the cross is `cw` times width of bounds, and the
    //Wvertical beam is `cw` times height of bounds, _unless_ `greek` is true,
    //Win which case both beams are `cw` times the shorter dimension of bounds.
    //W
    //WThe horizontal beam is centered vertically (and horizontally, which
    //Wtypically only matters on a greek cross).  The vertical beam is placed
    //Wsuch that the canton has a vertical side / horizontal side ratio of `r`.
    //W(A symmetric cross has `r` = height of bounds / width of bounds.)
    //W
    //W{{{
    //Wcross(lowerLeft, upperRight, cw, r, greek)
    Tester("import Staging._ ; cross((10, 10), (110, 70), 12, 110./70)")
    assertEquals(
      "PPath(10,10 L34.0000,37.2727 L34.0000,70.0000 L46.0000,70.0000 " +
      "L46.0000,37.2727 L110.000,37.2727 L110.000,25.2727 L46.0000,25.2727 " +
      "L46.0000,10.0000 L34.0000,10.0000 L34.0000,25.2727 L10.0000,25.2727 " +
      "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W
    //WA cross outline with a cross width (_cw_) is the graphical difference
    //Wbetween a cross with a cross width of 4 * _cw_ / 3 and a cross with a
    //Wcross width of 5 * _cw_ / 6.
    //W
    //W{{{
    //WcrossOutline(lowerLeft, upperRight, cw, r, greek)
    Tester("import Staging._ ; crossOutline((10, 10), (110, 70), 12, 110./70)")
    assertEquals(
      "PPath(10,10 L32.0000,39.2727 L32.0000,70.0000 L35.0000,70.0000 " +
      "L35.0000,36.2727 L10.0000,36.2727 L48.0000,70.0000 L48.0000,39.2727 " +
      "L110.000,39.2727 L110.000,36.2727 L45.0000,36.2727 L45.0000,70.0000 " +
      "L110.000,23.2727 L48.0000,23.2727 L48.0000,10.0000 L45.0000,10.0000 " +
      "L45.0000,26.2727 L110.000,26.2727 L32.0000,10.0000 L32.0000,23.2727 " +
      "L10.0000,23.2727 L10.0000,26.2727 L35.0000,26.2727 L35.0000,10.0000 " +
      "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W

    //W
    //WA saltire is similar to a symmetric cross, but the beams follow the
    //Wdiagonals of the bounds.
    //W
    //W{{{
    //Wsaltire(lowerLeft, upperRight, cw)
    Tester("import Staging._ ; saltire((10, 10), (110, 70), 12)")
    println(makeString(peekPNode))
    assertEquals(
      "PPath(10,10 L16.0000,70.0000 L60.0000,44.3200 L104.000,70.0000 " +
      "L110.000,70.0000 L110.000,64.0000 L69.9600,40.0000 L110.000,16.0000 " +
      "L110.000,10.0000 L104.000,10.0000 L60.0000,35.6800 L16.0000,10.0000 " +
      "L10.0000,10.0000 L10.0000,16.0000 L50.0400,40.0000 L10.0000,64.0000 " +
      "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W
    //WA saltireOutline is to a saltire as a crossOutline is to a cross.
    //W
    //W{{{
    //WsaltireOutline(lowerLeft, upperRight, cw)
    Tester("import Staging._ ; saltireOutline((10, 10), (110, 70), 12)")
    assertEquals(
      "PPath(10,10 L18.0000,70.0000 L60.0000,45.3200 L102.000,70.0000 " +
      "L105.000,70.0000 L60.0000,43.3200 z M110.000,66.0000 L110.000,63.0000 " +
      "L73.9600,40.0000 L110.000,17.0000 L110.000,14.0000 L68.9600,40.0000 " +
      "z M105.000,10.0000 L102.000,10.0000 L60.0000,34.6800 L18.0000,10.0000 " +
      "L15.0000,10.0000 L60.0000,36.6800 z M10.0000,14.0000 L10.0000,17.0000 " +
      "L46.0400,40.0000 L10.0000,63.0000 L10.0000,66.0000 L51.0400,40.0000 " +
      "z M0.00000,0.00000 )",
      makeString(peekPNode)
    )

    //W}}}
    //W
  /*
   *  def saltireOutline(p1: Point, p2: Point, s: Double) = SaltireOutline(p1, p2, s)
   */

  }
}

