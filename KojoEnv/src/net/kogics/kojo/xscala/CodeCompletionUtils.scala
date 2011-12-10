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

package net.kogics.kojo.xscala

object CodeCompletionUtils {
  val NotIdChars = """ .(){}!%&+\-<=>?@\\^`|~#:/*""" + "\n\r\t"

  import org.netbeans.modules.scala.core.lexer.ScalaTokenId
  val Keywords = 
    ScalaTokenId.values.filter { v =>
      v.asInstanceOf[ScalaTokenId.V].primaryCategory == "keyword"
    }.map { v =>
      v.asInstanceOf[ScalaTokenId.V].fixedText
    }.toList

  val KeywordTemplates = Map(
    "for" -> "for (i <- 1 to ${n}) {\n    ${cursor}\n}",
    "while" -> "while (${condition}) {\n    ${cursor}\n}",
    "if" -> "if (${condition}) {\n    ${cursor}\n}"
  )

  // UserCommand adds to this
  val BuiltinsMethodTemplates = collection.mutable.Map(
    "switchTo" -> "switchTo()",
    "onKeyPress" -> "onKeyPress { k =>\n    k match {\n    case Kc.VK_RIGHT => ${cursor}\n       case _ => \n    }\n}",
    "onMouseClick" -> "onMouseClick { (x, y) =>\n    ${cursor}\n}",
    "stAddLinkHandler" -> "stAddLinkHandler(${handlerName}) {d: ${argType} =>\n    ${cursor}\n}",
    "VPics" -> "VPics(\n      p,\n      p\n)",
    "HPics" -> "HPics(\n      p,\n      p\n)",
    "GPics" -> "GPics(\n      p,\n      p\n)",
    // Todo - is there any commonality here with the staging templates
    "trans" -> "trans(${x}, ${y})",
    "rot" -> "rot(${angle})",
    "scale" -> "scale(${factor})",
    "fillColor" -> "fillColor(${color})",
    "penColor" -> "penColor(${color})",
    "penWidth" -> "penWidth(${n})",
    "spin" -> "spin(${n})",
    "reflect" -> "reflect(${gap})",
    "show" -> "show(${pic})",
    "animate" -> "animate {\n    ${cursor}\n}",
    "row" -> "row(${picture}, ${n})",
    "col" -> "col(${picture}, ${n})"
  )
  
  val TwMethodTemplates = Map(
    "pict" -> "pict { t =>\n    import t._\n    ${cursor}\n}",
    "Pic" -> "Pic { t =>\n    import t._\n    ${cursor}\n}"
  ) 

  val MwMethodTemplates = Map(
    "figure" -> "figure(${name})",
    "point" -> "point(${x}, ${y}, ${optionalLabel})",
    "pointOn" -> "pointOn(${line}, ${x}, ${y})",
    "line" -> "line(${point1}, ${point2})",
    "lineSegment" -> "lineSegment(${point1}, ${point2Orlength})",
    "ray" -> "ray(${point1}, ${point2})",
    "circle" -> "circle(${center}, ${radius})",
    "angle" -> "angle(${point1}, ${point2}, ${pointOrSize})",
    "intersect" -> "intersect(${shape1}, ${shape2})",
    "midpoint" -> "midpoint(${lineSegment})",
    "perpendicular" -> "perpendicular(${toLine}, ${thruPoint})",
    "parallel" -> "perpendicular(${toLine}, ${thruPoint})",
    "setLabel" -> "setLabel(${label})",
    "setColor" -> "setColor(${color})",
    "add" -> "add(${shapes})",
    "show" -> "show(${shapes})",
    "showGrid" -> "showGrid()",
    "hideGrid" -> "hideGrid()",
    "showAxes" -> "showAxes()",
    "hideAxes" -> "hideAxes()",
    "showAlgebraView" -> "showAlgebraView()",
    "hideAlgebraView" -> "hideAlgebraView()",
    "turtle" -> "turtle(${x}, ${y})",
    "labelPosition" -> "labelPosition(${label})",
    "findPoint" -> "findPoint(${label})",
    "findLine" -> "findLine(${label})",
    "findAngle" -> "findAngle(${label})",
    "beginPoly" -> "beginPoly()",
    "endPoly" -> "endPoly()",
    "showAngles" -> "showAngles()",
    "showLengths" -> "showLengths()",
    "hideLengths" -> "hideLengths()",
    "hideAngles" -> "hideAngles()",
    "showExternalAngles" -> "showExternalAngles()",
    "hideExternalAngles" -> "hideExternalAngles()"
  )

  val StagingMethodTemplates = Map(
    "point" -> "point(${x}, ${y})",
    "line" -> "line(${x0}, ${y0}, ${x1}, ${y1})",
    "rectangle" -> "rectangle(${x0}, ${y0}, ${width}, ${height})",
    "text" -> "text(${content}, ${x}, ${y})",
    "circle" -> "circle(${cx}, ${cy}, ${radius})",
    "ellipse" -> "ellipse(${cx}, ${cy}, ${width}, ${height})",
    "arc" -> "arc(${cx}, ${cy}, ${rx}, ${ry}, ${startDegree}, ${extentDegree})",
    "refresh" -> "refresh {\n    ${cursor}\n}",
    "screenSize" -> "screenSize(${width}, ${height})",
    "background" -> "background(${color})",
    "dot" -> "dot(${x}, ${y})",
    "square" -> "square(${x}, ${y}, ${side})",
    "roundRectangle" -> "roundRectangle(${x}, ${y}, ${width}, ${height}, ${rx}, ${ry})",
    "pieslice" -> "pieslice(${cx}, ${cy}, ${rx}, ${ry}, ${startDegree}, ${extentDegree})",
    "openArc" -> "openArc(${cx}, ${cy}, ${rx}, ${ry}, ${startDegree}, ${extentDegree})",
    "chord" -> "chord(${cx}, ${cy}, ${rx}, ${ry}, ${startDegree}, ${extentDegree})",
    "vector" -> "vector(${x0}, ${y0}, ${x1}, ${y1}, ${headLength})",
    "star" -> "star(${cx}, ${cy}, ${inner}, ${outer}, ${numPoints})",
    "polyline" -> "polyline(${points})",
    "polygon" -> "polygon(${points})",
    "triangle" -> "triangle(${point1}, ${point2}, ${point3})",
    "quad" -> "quad(${point1}, ${point2}, ${point3}, ${point4})",
    "svgShape" -> "svgShape(${element})",
    "grayColors" -> "grayColors(${highGrayNum})",
    "grayColorsWithAlpha" -> "grayColorsWithAlpha(${highGrayNum}, ${highAlphaNum})",
    "rgbColors" -> "rgbColors(${highRedNum}, ${highGreenNum}, ${highBlueNum})",
    "rgbColorsWithAlpha" -> "rgbColorsWithAlpha(${highRedNum}, ${highGreenNum}, ${highBlueNum}, ${highAlphaNum})",
    "hsbColors" -> "hsbColors(${highHueNum}, ${highSaturationNum}, ${highBrightnessNum})",
    "namedColor" -> "namedColor(${colorName})",
    "lerpColor" -> "lerpColor(${colorFrom}, ${colorTo}, ${amount})",
    "fill" -> "fill(${color})",
    "noFill" -> "noFill()",
    "stroke" -> "stroke(${color})",
    "noStroke" -> "noStroke()",
    "strokeWidth" -> "strokeWidth(${width})",
    "withStyle" -> "withStyle (${fillColor}, ${strokeColor}, ${strokeWidth}) {\n    ${cursor}\n}",
    "constrain" -> "constrain(${value}, ${min}, ${max})",
    "norm" -> "norm(${value}, ${low}, ${high})",
    "map" -> "map(${value}, ${min1}, ${max1}, ${min2}, ${max2})",
    "sq" -> "sq(${value})",
    "sqrt" -> "sqrt(${value})",
    "dist" -> "dist(${x0}, ${y0}, ${x1}, ${y1})",
    "mag" -> "mag(${x}, ${y})",
    "lerp" -> "lerp(${low}, ${high}, ${value})",
    "loop" -> "loop {\n    ${cursor}\n}",
    "stop" -> "stop()",
    "reset" -> "reset()",
    "wipe" -> "wipe()",
    "sprite" -> "sprite(${x}, ${y}, ${filename})",
    "translate" -> "translate(${x}, ${y})",
    "path" -> "path(${x}, ${y})",
    "lineTo" -> "lineTo(${x}, ${y})",
    "setFontSize" -> "setFontSize(${size})",
    "setContent" -> "setContent(${content})",
    "onMouseDrag" -> "onMouseDrag { (x, y) =>\n    ${cursor}\n}",
    "show" -> "show()",
    "hide" -> "hide()",
    "erase" -> "erase()"
  )

  @volatile var ExtraMethodTemplates: collection.Map[String, String] = TwMethodTemplates

  def activateTw() {
    ExtraMethodTemplates = TwMethodTemplates
  }

  def activateMw() {
    ExtraMethodTemplates = MwMethodTemplates
  }

  def activateStaging() {
    ExtraMethodTemplates = StagingMethodTemplates
  }

  val MethodDropFilter = List("turtle0")
  val VarDropFilter = List("builtins", "predef")
  val InternalVarsRe = java.util.regex.Pattern.compile("""res\d+|\p{Punct}.*""")
  val InternalMethodsRe = java.util.regex.Pattern.compile("""_.*|.*\$.*""")

  def notIdChar(c: Char): Boolean =  NotIdChars.contains(c)

  def findLastIdentifier(rstr: String): Option[String] = {
    val str = " " + rstr
    var remaining = str.length
    while(remaining > 0) {
      if (notIdChar(str(remaining-1))) return Some(str.substring(remaining))
      remaining -= 1
    }
    None
  }

  def findIdentifier(str: String): (Option[String], Option[String]) = {
    if (str.length == 0) return (None, None)

    if (str.endsWith(".")) {
      (findLastIdentifier(str.substring(0, str.length-1)), None)
    }
    else {
      val lastDot = str.lastIndexOf('.')
      if (lastDot == -1) (None, findLastIdentifier(str))
      else {
        val tPrefix = str.substring(lastDot+1)
        if (tPrefix == findLastIdentifier(tPrefix).get)
          (findLastIdentifier(str.substring(0, lastDot)), Some(tPrefix))
        else
          (None, findLastIdentifier(tPrefix))
      }
    }
  }
}
