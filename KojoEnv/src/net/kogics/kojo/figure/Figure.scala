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
package figure

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._
import edu.umd.cs.piccolo.util._
import edu.umd.cs.piccolo.event._
import edu.umd.cs.piccolo.activities.PActivity
import edu.umd.cs.piccolo.activities.PActivity.PActivityDelegate

import javax.swing._
import java.awt.{Point => _, _}

import net.kogics.kojo.util.Utils
import core._

object Figure {
  def apply(canvas: SpriteCanvas, initX: Double = 0d, initY: Double = 0): Figure = {
    val fig = Utils.runInSwingThreadAndWait {
      new Figure(canvas, initX, initY)
    }
    fig
  }
}

class Figure private (canvas: SpriteCanvas, initX: Double, initY: Double) {
  private val bgLayer = new PLayer
  private val fgLayer = new PLayer
  private var currLayer = bgLayer

  def dumpLastChild = currLayer.getChild(currLayer.getChildrenCount - 1)

  def dumpChild(n: Int): PNode = {
    try {
      currLayer.getChild(n)
    }
    catch { case e => throw e }
  }
  
  // if fgLayer is bigger than bgLayer, (re)painting does not happen very cleanly
  // needs a better fix than the one below
  bgLayer.setBounds(-500, -500, 1000, 1000)

  private val camera = canvas.getCamera
  val DefaultColor = Color.red
  val DefaultFillColor: Color = null
  val DefaultStroke = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
  @volatile private var listener: SpriteListener = NoopSpriteListener

  private var figAnimation: PActivity = _
  private var _lineColor: Color = _
  private var _fillColor: Color = _
  private var _lineStroke: Stroke = _

  camera.addLayer(camera.getLayerCount-1, bgLayer)
  camera.addLayer(camera.getLayerCount-1, fgLayer)
  init()

  def init() {
    bgLayer.setOffset(initX, initY)
    fgLayer.setOffset(initX, initY)
    _lineColor = DefaultColor
    _fillColor = DefaultFillColor
    _lineStroke = DefaultStroke
  }

  def fillColor = Utils.runInSwingThreadAndWait {
    _fillColor
  }

  def lineColor = Utils.runInSwingThreadAndWait {
    _lineColor
  }

  def lineStroke = Utils.runInSwingThreadAndWait {
    _lineStroke
  }

  def repaint() {
    bgLayer.repaint()
    fgLayer.repaint()
  }

  def clear() {
    Utils.runInSwingThread {
      stop()
      bgLayer.removeAllChildren()
      fgLayer.removeAllChildren()
      init()
      repaint()
    }
  }

  def fgClear() {
    Utils.runInSwingThread {
      fgLayer.removeAllChildren()
      repaint()
    }
  }

  def remove() {
    Utils.runInSwingThread {
      camera.removeLayer(bgLayer)
      camera.removeLayer(fgLayer)
    }
  }

  def setPenColor(color: java.awt.Color) {
    Utils.runInSwingThread {
      _lineColor = color
    }
  }

  def setPenThickness(t: Double) {
    Utils.runInSwingThread {
      _lineStroke = new BasicStroke(t.toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
    }
  }

  def setLineStroke(st: Stroke) {
    Utils.runInSwingThread {
      _lineStroke = st
    }
  }

  def setFillColor(color: java.awt.Color) {
    Utils.runInSwingThread {
      _fillColor = color
    }
  }

  def addPnode(node: PNode) = Utils.runInSwingThread {
    pnode(node)
  }

  def pnode(node: PNode) = {
    // needs to be called on swing thread
    if (node.isInstanceOf[PPath]) {
      val p = node.asInstanceOf[PPath]
      p.setPaint(_fillColor)
      p.setStroke(_lineStroke)
      p.setStrokePaint(_lineColor)
    }
    else if (node.isInstanceOf[PText]) {
      val t = node.asInstanceOf[PText]
      t.setTextPaint(_lineColor)
    }
    currLayer.addChild(node)
    currLayer.repaint
    node
  }
  
  def removePnode(node: PNode): Unit = Utils.runInSwingThread {
    currLayer.removeChild(node)
    currLayer.repaint
  }


  def refresh(fn: => Unit) {
    
    Utils.runInSwingThread {
      if (figAnimation == null ) {
        // need to extend this to allow multiple animations
        figAnimation = new PActivity(-1) {
          override def activityStep(elapsedTime: Long) {
            currLayer = fgLayer
            try {
              staging.Inputs.activityStep
              fn
              if (isStepping) {
                listener.hasPendingCommands()
              }
            }
            catch {
              case t: Throwable =>
                canvas.outputFn("Problem: " + t.toString())
                stop()
            }
            finally {
              repaint()
              currLayer = bgLayer
            }
          }
        }

        figAnimation.setDelegate(new PActivityDelegate {
            override def activityStarted(activity: PActivity) {}
            override def activityStepped(activity: PActivity) {}
            override def activityFinished(activity: PActivity) {
              listener.pendingCommandsDone()
            }
          })

        canvas.getRoot.addActivity(figAnimation)
      }
    }
  }

  def stopRefresh() = stop()

  def stop() {
    Utils.runInSwingThread {
      if (figAnimation != null) {
        figAnimation.terminate(PActivity.TERMINATE_AND_FINISH)
        figAnimation = null
      }
    }
  }

  def onMouseMove(fn: (Double, Double) => Unit) {
    canvas.addInputEventListener(new PBasicInputEventHandler {
        override def mouseMoved(e: PInputEvent) {
          val pos = e.getPosition
          fn(pos.getX, pos.getY)
          currLayer.repaint()
        }
      })
  }

  private [kojo] def setSpriteListener(l: SpriteListener) {
    listener = l
  }
}

