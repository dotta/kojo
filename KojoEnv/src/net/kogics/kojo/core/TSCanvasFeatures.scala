/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kogics.kojo.core

trait TSCanvasFeatures {
  def clearPuzzlers(): Unit
  def newTurtle(x: Int, y: Int): Turtle
  def newPuzzler(x: Int, y: Int): Turtle
  def axesOn(): Unit
  def axesOff(): Unit
  def gridOn(): Unit
  def gridOff(): Unit
  def zoom(factor: Double, cx: Double, cy: Double)
  def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double)
  def exportImage(filePrefix: String): java.io.File
  def exportThumbnail(filePrefix: String, height: Int): java.io.File
  def onKeyPress(fn: Int => Unit)  
  def onMouseClick(fn: (Double, Double) => Unit)
}
