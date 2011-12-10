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

class ColorTest extends StagingTestBase {

  /* Testing manifest
   * 
   * def grayColors(grayMax: Int) = ColorMaker(GRAY(grayMax))
   * def grayColorsWithAlpha(grayMax: Int, alphaMax: Int) =
   * def rgbColors(rMax: Int, gMax: Int, bMax: Int) =
   * def rgbColorsWithAlpha(rMax: Int, gMax: Int, bMax: Int, alphaMax: Int) =
   * def hsbColors(hMax: Int, sMax: Int, bMax: Int) =
   * def color(s: String) = ColorMaker.color(s)
   * def fill(c: Color) = Impl.figure0.setFillColor(c)
   * def noFill = Impl.figure0.setFillColor(null)
   * def stroke(c: Color) = Impl.figure0.setPenColor(c)
   * def noStroke = Impl.figure0.setPenColor(null)
   * def strokeWidth(w: Double) = Impl.figure0.setPenThickness(w)
   * def withStyle(fc: Color, sc: Color, sw: Double)(body: => Unit) =
   * def saveStyle = Style.save
   * def restoreStyle = Style.restore
   * implicit def ColorToRichColor (c: java.awt.Color) = RichColor(c)
   * def lerpColor(from: RichColor, to: RichColor, amt: Double) =
   *
   * (manually added:)
   * def alpha = c.getAlpha
   * def red = c.getRed
   * def blue = c.getBlue
   * def green = c.getGreen
   * def hue = {
   * def saturation = (this.hsb(1) * 255).toInt
   * def brightness = (this.hsb(2) * 255).toInt
   */

  @Test
  // lalit sez: if we have more than five tests, we run out of heap space - maybe
  // a leak in the Scala interpreter/compiler subsystem. So we run (mostly)
  // everything in one test
  def test1 = {
  //W
  //W==Color==
  //W
  //WA color value can be created by calling one of the methods
  //W
  //W{{{
  //Wcolor(red, green, blue)
  //Wcolor(value)
  //W}}}
  //W
  //Wwhich each yields an instance of `java.awt.Color`.
  //W
  //WAnother way to specify color is through a color maker.  For instance, to get
  //Wa grayscale color, call `grayColors`.  The argument sets a limit to the
  //Wnumber of colors that can be specified.  E.g. `grayColors(100)` allows 101
  //Wshades of gray to be specified (0 to 100).  To get one of those colors, call
  //Wthe color maker with the color number as argument.
  //W
  //W{{{
  //Wval cm = grayColors(lim) ; cm(num)
    Tester(
      "import Staging._ ; val cm = grayColors(255) ; println(cm(22))",
      Some("$java.awt.Color\\[r=22,g=22,b=22\\]import Staging._" +
           "cm: net.kogics.kojo.staging.GrayColorMaker = net.kogics.kojo.staging.GrayColorMaker@.*")
    )
  //W}}}
  //W
  //Wcreates a grayscale color with a "whiteness" equal to `num` / `lim`.
  //W
  //WThe color maker can also take a `Double` as argument, in which case the
  //Wresulting color has a "whiteness" of `val` (expected to be in the range
  //W0.0 <= val <= 1.0):
  //W
  //W{{{
  //Wval cm = grayColors(lim) ; cm(val) // where val is a Double
    Tester(
      "import Staging._ ; val cm = grayColors(255) ; println(cm(.1))",
      Some("$java.awt.Color\\[r=26,g=26,b=26\\]import Staging._" +
           "cm: net.kogics.kojo.staging.GrayColorMaker = net.kogics.kojo.staging.GrayColorMaker@.*")
    )

  //W}}}
  //W
  //WTo get a non-opaque grayscale color maker, call `grayColorsWithAlpha` with
  //Wtwo limit values, one for the highest shade number and one for the highest
  //Walpha number.  The color maker takes corresponding numbers for shade and
  //Walpha.
  //W
  //W{{{
  //Wval cm = grayColorsWithAlpha(grayLim, alphaLim) ; cm(grayNum, alphaNum)
    val gacm = "net.kogics.kojo.staging.GrayAlphaColorMaker"
    Tester(
      "import Staging._ ; val cm = grayColorsWithAlpha(255, 255) ; println(cm(22, 22))",
      Some("$java.awt.Color\\[r=22,g=22,b=22\\]import Staging._" +
           "cm: " + gacm + " = " + gacm + "@.*")
    )
  //W}}}
  //W
  //Wcreates a grayscale color with a "whiteness" equal to `grayNum` / `grayLim`
  //Wand a opacity equal to `alphaNum` / `alphaLim`.
  //W
  //WThe color maker can also take `Double` arguments, in which case the
  //Wresulting color has a "whiteness" of `grayVal` and opacity of `alphaVal`
  //W(both expected to be in the range 0.0 <= val <= 1.0):
  //W
  //W{{{
  //Wval cm = grayColorsWithAlpha(grayLim, alphaLim) ; cm(grayVal, alphaVal)
    Tester(
      "import Staging._ ; val cm = grayColorsWithAlpha(255, 255) ; println(cm(.1, .1))",
      Some("$java.awt.Color\\[r=26,g=26,b=26\\]import Staging._" +
           "cm: " + gacm + " = " + gacm + "@.*")
    )

  //W}}}
  //W
  //WTo create a color maker for RGB colors, use `rgbColors` and pass three
  //Wlimit values to it (for red, green, and blue)
  //W
  //W{{{
  //Wval cm = rgbColors(redLim, greenLim, blueLim) ; cm(redNum, greenNum, blueNum)
    val rcm = "net.kogics.kojo.staging.RgbColorMaker"
    Tester(
      "import Staging._ ; val cm = rgbColors(255, 255, 255) ; println(cm(22, 22, 22))",
      Some("$java.awt.Color\\[r=22,g=22,b=22\\]import Staging._" +
           "cm: " + rcm + " = " + rcm + "@.*")
    )
  //W}}}
  //W
  //Wcreates a color with a "redness" equal to `redNum` / `redLim`, etc.
  //W
  //WThe color maker can also take `Double` arguments, in which case the
  //Wresulting color has a "redness" of `redVal`, etc (all expected to be in
  //Wthe range 0.0 <= val <= 1.0):
  //W
  //W{{{
  //Wval cm = rgbColors(redLim, greenLim, blueLim) ; cm(redVal, greenVal, blueVal)
    Tester(
      "import Staging._ ; val cm = rgbColors(255, 255, 255) ; println(cm(.1, .1, .1))",
      Some("$java.awt.Color\\[r=26,g=26,b=26\\]import Staging._" +
           "cm: " + rcm + " = " + rcm + "@.*")
    )

  //W}}}
  //W
  //WTo create a color maker for RGB colors with transparency, use
  //W`rgbColorsWithAlpha` and pass four limit values to it (for red, green, blue,
  //Wand alpha)
  //W
  //W{{{
  //Wval cm = rgbColorsWithAlpha(redLim, greenLim, blueLim, alphaLim) ; cm(redNum, greenNum, blueNum, alphaNum)
    val racm = "net.kogics.kojo.staging.RgbAlphaColorMaker"
    Tester(
      "import Staging._ ; val cm = rgbColorsWithAlpha(255, 255, 255, 255) ; println(cm(22, 22, 22, 22))",
      Some("$java.awt.Color\\[r=22,g=22,b=22\\]import Staging._" +
           "cm: " + racm + " = " + racm + "@.*")
    )
  //W}}}
  //W
  //Wcreates a color with a "redness" equal to `redNum` / `redLim`, etc.
  //W
  //WThe color maker can also take `Double` arguments, in which case the
  //Wresulting color has a "redness" of `redVal`, etc (all expected to be in
  //Wthe range 0.0 <= val <= 1.0):
  //W
  //W{{{
  //Wval cm = rgbColorsWithAlpha(redLim, greenLim, blueLim, alphaLim) ; cm(redVal, greenVal, blueVal, alphaVal)
    Tester(
      "import Staging._ ; val cm = rgbColorsWithAlpha(255, 255, 255, 255) ; println(cm(.1, .1, .1, .1))",
      Some("$java.awt.Color\\[r=26,g=26,b=26\\]import Staging._" +
           "cm: " + racm + " = " + racm + "@.*")
    )

  //W}}}
  //W
  //WTo create a color maker for HSB colors, use `hsbColors` and pass three
  //Wlimit values to it (for hue, saturation, and brightness).
  //W
  //W{{{
  //Wval cm = hsbColors(hueLim, saturationLim, brightnessLim) ; cm(hueNum, saturationNum, brightnessNum)
    val hcm = "net.kogics.kojo.staging.HsbColorMaker"
    Tester(
      "import Staging._ ; val cm = hsbColors(255, 255, 255) ; println(cm(22, 22, 22))",
      Some("$java.awt.Color\\[r=22,g=21,b=20\\]import Staging._" +
           "cm: " + hcm + " = " + hcm + "@.*")
    )
  //W}}}
  //W
  //Wcreates a color with an effective hue of `hueNum` / `hueLim`, etc.
  //W
  //WThe color maker can also take `Double` arguments, in which case the
  //Wresulting color has an effective hue of `hueVal`, etc (all expected to be in
  //Wthe range 0.0 <= val <= 1.0):
  //W
  //W{{{
  //Wval cm = hsbColors(hueLim, saturationLim, brightnessLim) ; cm(hueVal, saturationVal, brightnessVal)
    Tester(
      "import Staging._ ; val cm = hsbColors(255, 255, 255) ; println(cm(.1, .1, .1))",
      Some("$java.awt.Color\\[r=26,g=24,b=23\\]import Staging._" +
           "cm: " + hcm + " = " + hcm + "@.*")
    )

  //W}}}
  //W
  //WFinally,
  //W
  //W{{{
  //WnamedColor(colorName)
    Tester(
      """import Staging._ ; println(namedColor("#99ccDD"))""",
      Some("java.awt.Color[r=153,g=204,b=221]import Staging._")
    )

    Tester(
      """import Staging._ ; println(namedColor("aliceblue"))""",
      Some("java.awt.Color[r=240,g=248,b=255]import Staging._")
    )

  //W}}}
  //W
  //Wwhere _colorName_ is either
  //W
  //W    * "none",
  //W    * one of the names in this list: http://www.w3.org/TR/SVG/types.html#ColorKeywords, or
  //W    * a string with the format "#rrggbb" (in hexadecimal)
  //W
  //Wreturns the described color.
  //W
  //WLinear interpolation between two colors is done using `lerpColor`:
  //W
  //W{{{
  //WlerpColor(colorFrom, colorTo, amount)
    Tester(
      """import Staging._
        |
        |val a = namedColor("#99ccDD")
        |val b = namedColor("#003366")
        |println(lerpColor(a, b, 0))""".stripMargin,
      Some("java.awt.Color[r=153,g=204,b=221]import Staging._" +
           "a: java.awt.Color = java.awt.Color[r=153,g=204,b=221]" +
           "b: java.awt.Color = java.awt.Color[r=0,g=51,b=102]")
    )

    Tester(
      """import Staging._
        |
        |val a = namedColor("#99ccDD")
        |val b = namedColor("#003366")
        |println(lerpColor(a, b, 0.3))""".stripMargin,
      Some("java.awt.Color[r=107,g=158,b=185]import Staging._" +
           "a: java.awt.Color = java.awt.Color[r=153,g=204,b=221]" +
           "b: java.awt.Color = java.awt.Color[r=0,g=51,b=102]")
    )

    Tester(
      """import Staging._
        |
        |val a = namedColor("#99ccDD")
        |val b = namedColor("#003366")
        |println(lerpColor(a, b, 1))""".stripMargin,
      Some("java.awt.Color[r=0,g=51,b=102]import Staging._" +
           "a: java.awt.Color = java.awt.Color[r=153,g=204,b=221]" +
           "b: java.awt.Color = java.awt.Color[r=0,g=51,b=102]")
    )

  //W}}}
  //W
  //WWhen drawing figures, the _fill_ color, which is used for the insides, and
  //Wthe _stroke_ color, which is used for the edges, can be set and unset.
  //W
  //WTo set the fill color, call `fill`.
  //W
  //W{{{
  //Wfill(color)
    Tester(
      """import Staging._
        |
        |val a = namedColor("#99ccDD")
        |fill(a)""".stripMargin,
      Some("import Staging._" +
           "a: java.awt.Color = java.awt.Color[r=153,g=204,b=221]")
    )
    assertEquals("java.awt.Color[r=153,g=204,b=221]", SpriteCanvas.instance.figure0.fillColor.toString)

  //W}}}
  //W
  }

  @Test
  def test2 = {
  //WTo unset the fill color, call `noFill`, or `fill` with a `null` argument.
  //W
  //W{{{
  //WnoFill
  //Wfill(null)
    Tester("import Staging._ ; fill(namedColor(\"#99ccDD\")) ; noFill")
    assertNull(SpriteCanvas.instance.figure0.fillColor)

    Tester("import Staging._ ; fill(namedColor(\"#99ccDD\")) ; fill(null)")
    assertNull(SpriteCanvas.instance.figure0.fillColor)

  //W}}}
  //W
  //WTo set the stroke color, call `stroke`.
  //W
  //W{{{
  //Wstroke(color)
    Tester(
      """import Staging._ ; stroke(namedColor("#99ccDD"))""",
      Some("import Staging._")
    )
    assertEquals("java.awt.Color[r=153,g=204,b=221]", SpriteCanvas.instance.figure0.lineColor.toString)

  //W}}}
  //W
  //WTo unset the stroke color, call `noStroke`, or `stroke` with a `null` argument.
  //W
  //W{{{
  //WnoStroke
  //Wstroke(null)
    Tester("import Staging._ ; stroke(null)", Some("import Staging._"))
    assertNull(SpriteCanvas.instance.figure0.lineColor)

    Tester(
      """import Staging._ ; stroke(namedColor("#99ccDD")) ; noStroke""",
      Some("import Staging._")
    )
    assertNull(SpriteCanvas.instance.figure0.lineColor)

  //W}}}
  //W
  //WTo set the stroke width, call `strokeWidth`.
  //W
  //W{{{
  //WstrokeWidth(value)
    Tester("""import Staging._ ; stroke(red) ; strokeWidth(2)""", Some("import Staging._"))
    Tester("""import Staging._ ; strokeWidth(2)""", Some("import Staging._"))
    assertEquals(2.0, SpriteCanvas.instance.figure0.lineStroke.asInstanceOf[java.awt.BasicStroke].getLineWidth, 0.01)
    Tester("""import Staging._ ; strokeWidth(2.0)""", Some("import Staging._"))
    assertEquals(2.0, SpriteCanvas.instance.figure0.lineStroke.asInstanceOf[java.awt.BasicStroke].getLineWidth, 0.01)
    Tester("""import Staging._ ; strokeWidth(.2)""", Some("import Staging._"))

  //W}}}
  //W
  //WTo set the fill, stroke, and stroke width just for the extent of some lines
  //Wof code, use `withStyle`.
  //W
  //W{{{
  //WwithStyle(fillColor, strokeColor, strokeWidth) { ...code... }
    Tester("""import Staging._
             |
             |fill(green)
             |stroke(black)
             |strokeWidth(1.0)
             |withStyle(red, blue, 4) {/**/}""".stripMargin, Some("import Staging._"))
    assertEquals("java.awt.Color[r=0,g=255,b=0]", SpriteCanvas.instance.figure0.fillColor.toString)
    assertEquals("java.awt.Color[r=0,g=0,b=0]", SpriteCanvas.instance.figure0.lineColor.toString)
    assertEquals(1.0, SpriteCanvas.instance.figure0.lineStroke.asInstanceOf[java.awt.BasicStroke].getLineWidth, 0.01)

  //W}}}
  //M
  //MThe fill, stroke, and stroke width can also be saved with `saveStyle` and
  //Mlater restored with `restoreStyle` (the latter quietly fails if no style
  //Mhas been saved yet).
  //M
  //M{{{
  //MsaveStyle
  //MrestoreStyle
  /*
    Tester("""import Staging._
             |
             |fill(green)
             |stroke(black)
             |strokeWidth(1.0)
             |saveStyle
             |fill(red)
             |stroke(blue)
             |strokeWidth(4)
             |restoreStyle""".stripMargin, Some("import Staging._"))
    assertEquals("java.awt.Color[r=0,g=255,b=0]", SpriteCanvas.instance.figure0.fillColor.toString)
    assertEquals("java.awt.Color[r=0,g=0,b=0]", SpriteCanvas.instance.figure0.lineColor.toString)
    assertEquals(1.0, SpriteCanvas.instance.figure0.lineStroke.asInstanceOf[java.awt.BasicStroke].getLineWidth, 0.01)
    */

  //M}}}
  //W
  //WThe Color type is 'pimped' with the following accessors:
  //W
  //W{{{
  //Walpha
    Tester(
      """import Staging._
        |
        |println(rgbColorsWithAlpha(255, 255, 255, 255)(.1, .1, .1, .1).alpha)
      """.stripMargin,
      Some("26import Staging._")
    )

  //Wred
    Tester(
      """import Staging._
        |
        |println(rgbColors(255, 255, 255)(.1, .1, .1).red)
      """.stripMargin,
      Some("26import Staging._")
    )

  //Wblue
    Tester(
      """import Staging._
        |
        |println(rgbColors(255, 255, 255)(.1, .1, .1).blue)
      """.stripMargin,
      Some("26import Staging._")
    )

  //Wgreen
    Tester(
      """import Staging._
        |
        |println(rgbColors(255, 255, 255)(.1, .1, .1).green)
      """.stripMargin,
      Some("26import Staging._")
    )

  //Whue
    Tester(
      """import Staging._
        |
        |println(hsbColors(255, 255, 255)(.1, .1, .1).hue)
      """.stripMargin,
      Some("15import Staging._")
    )
  //Wsaturation
    Tester(
      """import Staging._
        |
        |println(hsbColors(255, 255, 255)(.1, .1, .1).saturation)
      """.stripMargin,
      Some("29import Staging._")
    )
  //Wbrightness
    Tester(
      """import Staging._
        |
        |println(hsbColors(255, 255, 255)(.1, .1, .1).brightness)
      """.stripMargin,
      Some("26import Staging._")
    )

  //W}}}
  //W
  }
}

