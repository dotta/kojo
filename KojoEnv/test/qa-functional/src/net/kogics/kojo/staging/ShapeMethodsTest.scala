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

class ShapeMethodsTest extends StagingTestBase {

  /* Testing manifest
   *
   * def node: PNode
   * var sizeFactor = 1.
   * var orientation = 90.
   * def hide() {
   * def show() {
   * def fill_=(color: Color) {
   * def fill = Utils.runInSwingThreadAndWait {
   * def rotate(amount: Double) = {
   * def rotateTo(angle: Double) = {
   * def scale(amount: Double) = {
   * def scaleTo(size: Double) = {
   * def translate(p: Point) = {
   * def offset = Utils.runInSwingThreadAndWait {
   *
   * (manually added, StrokedShapes only:)
   *  def stroke_=(color: Color) {
   *  def stroke = Utils.runInSwingThreadAndWait {
   */

  @Test
  // lalit sez: if we have more than five tests, we run out of heap space - maybe
  // a leak in the Scala interpreter/compiler subsystem. So we run (mostly)
  // everything in one test
  def test1 = {
    //W
    //W==Shape methods==
    //W
    //WAll drawn shapes have the following methods and accessors.
    //W
    //W{{{
    //'undocumented'
    Tester(
      "import Staging._ ; val a = dot(10, 15) ; val b = a.node ; " +
      "val c = a.sizeFactor ; val d = a.orientation ; " +
      "val e = a.fill ; a.fill = black",
      Some("$import Staging._" +
           "a: net.kogics.kojo.staging.Dot = Staging.Dot\\(Point\\(10[.,]00, 15[.,]00\\)\\)" +
           "b: edu.umd.cs.piccolo.nodes.PPath = edu.umd.cs.piccolo.nodes.PPath@.*" +
           "c: Double = 1.0" +
           "d: Double = 0.0" +
           "e: java.awt.Paint = null" +
           "a.fill: java.awt.Paint = java.awt.Color\\[r=0,g=0,b=0\\]"
      )
    )

    //Whide
    //Wshow
    Tester(
      "import Staging._ ; val a = dot(10, 15) ; a.hide ; a.show",
      Some("$import Staging._a: net.kogics.kojo.staging.Dot = Staging.Dot\\(Point\\(10[.,]00, 15[.,]00\\)\\)")
    )

    //W}}}
    //W
    //WMakes the shape invisible / visible.
    //W
    //W{{{
    //Wrotate(amount)
    //W}}}
    //W
    //WRotates the shape _amount_ degrees counterclockwise (for positive amounts)
    //Wor clockwise (for negative amounts).
    //W
    //W{{{
    //WrotateTo(angle)
    Tester(
      "import Staging._ ; val a = line(10, 15, 20, 40) ; a.rotate(20) ; a.rotateTo(90)",
      Some("$import Staging._" +
           "a: net.kogics.kojo.staging.Line = Staging.Line\\(Point\\(10[.,]00, 15[.,]00\\), Point\\(20[.,]00, 40[.,]00\\)\\)")
    )

    //W}}}
    //W
    //WRotates the shape to the specified angle; rotating to 90 degrees yields the
    //Woriginal orientation.
    //W
    //W{{{
    //Wscale(amount)
    //W}}}
    //W
    //WScales the shape by _amount_; 2 means doubling of each dimension, 0.5 means
    //Whalving them.
    //W
    //W{{{
    //WscaleTo(size)
    Tester(
      "import Staging._ ; val a = line(10, 15, 20, 40) ; a.scale(2.5) ; a.scaleTo(0.5)",
      Some("$import Staging._a: net.kogics.kojo.staging.Line = Staging.Line\\(Point\\(10[.,]00, 15[.,]00\\), Point\\(20[.,]00, 40[.,]00\\)\\)")
    )

    //W}}}
    //W
    //WScales the shape to the specified size; scaling to 1.0 yields the
    //Woriginal size.
    //W
    //W{{{
    //Wtranslate(amount)
    //W}}}
    //W
    //WTranslates (moves) the shape by _amount_, which is a `Point`.  The _x_
    //Wcomponent of the point specifies the amount of horizontal movement, while
    //Wthe _y_ component specifies the vertical.
    //W
    //W{{{
    //Woffset
    Tester(
      "import Staging._ ; val a = line(10, 15, 20, 40) ; a.translate(point(20, 0)) ; a.offset",
      Some("$import Staging._a: net.kogics.kojo.staging.Line = Staging.Line\\(Point\\(10[.,]00, 15[.,]00\\), Point\\(20[.,]00, 40[.,]00\\)\\)" +
           "res.*: net.kogics.kojo.core.Point = Point\\(20.00, 0.00\\)"
      )
    )

    //W}}}
    //W
    //WYields the _x_ / _y_ distance the shape has traveled.
    //W

    //'undocumented'
    Tester(
      "import Staging._ ; val a = line(10, 15, 20, 40) ; " +
      "val b = a.stroke ; a.stroke = black",
      Some("$import Staging._" +
           "a: net.kogics.kojo.staging.Line = " +
           "Staging.Line\\(Point\\(10[.,]00, 15[.,]00\\), Point\\(20[.,]00, 40[.,]00\\)\\)" +
           "b: java.awt.Paint = java.awt.Color\\[r=255,g=0,b=0\\]" +
           "a.stroke: java.awt.Paint = java.awt.Color\\[r=0,g=0,b=0\\]"
      )
    )
  }
}

