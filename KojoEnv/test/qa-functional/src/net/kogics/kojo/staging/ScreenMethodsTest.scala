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

class ScreenMethodsTest extends StagingTestBase {

  /* Testing manifest
   *
   * def screenWidth = Screen.rect.getWidth.toInt
   * def screenHeight = Screen.rect.getHeight.toInt
   * def screenSize(width: Int, height: Int) = Screen.size(width, height)
   * def screenMid = Screen.rect.getCenter2D
   * def screenExt = Screen.rect.getExt
   * def background(bc: Color) = {
   */
  def peekZoom = {
    val a = SpriteCanvas.instance.getCamera.getViewTransformReference
    (
      "%.4g" format a.getScaleX,
      "%.4g" format a.getScaleY,
      "%.4g" format a.getTranslateX,
      "%.4g" format a.getTranslateY
    )
  }

  @Test
  def testEvalSession = {
  //W
  //W==User Screen==
  //W
  //WThe current width and height of the user screen is stored in the variables
  //W`screenWidth` and `screenHeight` (both are 0 by default).
  //W
    Tester("Staging.screenWidth", Some("Int = 0"))
    Tester("Staging.screenHeight", Some("Int = 0"))

  //W
  //WThe methods `screenMid` and `screenExt` yield the coordinates of the middle
  //Wpoint of the user screen and the coordinates of the upper right corner of
  //Wthe user screen (the extreme point), respectively.  Both return value
  //W`point(0, 0)` if `screenSize` hasn't been called yet.
    Tester("Staging.screenMid", Some("$net.kogics.kojo.core.Point = Point\\(0[,.]00, 0[,.]00\\)"))
    Tester("Staging.screenExt", Some("$net.kogics.kojo.core.Point = Point\\(0[,.]00, 0[,.]00\\)"))
    Tester("Staging.screenSize(10,10)", Some("$\\(Int, Int\\) = \\(10,10\\)"))
    Tester("Staging.screenMid", Some("$net.kogics.kojo.core.Point = Point\\(5[,.]00, 5[,.]00\\)"))
    Tester("Staging.screenExt", Some("$net.kogics.kojo.core.Point = Point\\(10[,.]00, 10[,.]00\\)"))

  //W
  //WThe dimensions of the user screen can be set by calling
  //W
  //W{{{
  //WscreenSize(width, height)
  //W}}}
    Tester("Staging.screenSize(250, 150)", Some("(Int, Int) = (250,150)"))
    assertEquals(("3.000","-3.000","-375.0","225.0"), peekZoom)
    Tester("Staging.screenWidth", Some("Int = 250"))
    Tester("Staging.screenHeight", Some("Int = 150"))

  //W
  //WThe orientation of either axis can be reversed by negation, e.g.:
  //W
  //W{{{
  //WscreenSize(width, -height)
  //W}}}
  //W
  //Wmakes (0,0) the upper left corner and (width, height) the lower right
  //Wcorner.
    Tester("Staging.screenSize(250, -150)", Some("(Int, Int) = (250,150)"))
    assertEquals(("3.000","3.000","-375.0","-225.0"), peekZoom)
    Tester("Staging.screenSize(-250, 150)", Some("(Int, Int) = (250,150)"))
    assertEquals(("-3.000","-3.000","375.0","225.0"), peekZoom)
    Tester("Staging.screenSize(-250, -150)", Some("(Int, Int) = (250,150)"))
    assertEquals(("-3.000","3.000","375.0","-225.0"), peekZoom)

  //W
  //WThe background color for the user screen area can be set:
  //W
  //W{{{
  //Wbackground(color)
    var beforeStroke = SpriteCanvas.instance.figure0.lineStroke.toString
    Tester("""import Staging._
             |screenSize(250, 150)
             |background(black)""".stripMargin,
           None)
    assertEquals(beforeStroke, SpriteCanvas.instance.figure0.lineStroke.toString)

    Tester("Staging.strokeWidth(8)", None)
    beforeStroke = SpriteCanvas.instance.figure0.lineStroke.toString
    Tester("""import Staging._
             |screenSize(250, 150)
             |background(black)""".stripMargin,
           None)
    assertEquals(beforeStroke, SpriteCanvas.instance.figure0.lineStroke.toString)

  //W}}}
  //W
  }
}

