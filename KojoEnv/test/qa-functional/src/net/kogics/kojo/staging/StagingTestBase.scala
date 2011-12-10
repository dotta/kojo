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

//import org.junit.After
//import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{CountDownLatch, TimeUnit}

import net.kogics.kojo.core.RunContext

import net.kogics.kojo.util._

/* Testing policy
 *
 * Every member of the interface shall be tested, preferably for effect but at
 * least for executability.
 *
 * The implementation is tested as needed but is generally considered to be
 * sufficiently correct if the interface works correctly.
 *
 */

// cargo coding off CodePaneTest
class StagingTestBase extends KojoTestBase {
  
  initNetbeansDirs()
  val runCtx = new TestRunContext(this)

  val codeRunner = new xscala.ScalaCodeRunner(runCtx, SpriteCanvas.instance)
  val pane = new javax.swing.JEditorPane()
  val Delimiter = ""

  var latch: CountDownLatch = _
  def runCode() {
    latch = new CountDownLatch(1)
    codeRunner.runCode(pane.getText())
    latch.await()
  }

  object Tester {
    var resCounter = 0
    var res = ""

    def postIncrCounter = {
      val c = resCounter
      resCounter += 1
      c
    }

    def isMultiLine(cmd: String) = cmd.contains("\n") || cmd.contains(" ; ")

    def outputPrefix(cmd: String) = {
      if (isMultiLine(cmd)) ""
      else "res" + postIncrCounter + ": "
    }

    def apply (cmd: String, s: Option[String] = None) = {
      runCtx.clearOutput
      pane.setText(cmd)
      runCtx.success.set(false)

      runCode()
      Utils.runInSwingThreadAndWait {  /* noop */  }

      assertTrue(runCtx.success.get)
      val output = stripCrLfs(runCtx.getCurrentOutput)
      s foreach { ss =>
        if (ss isEmpty) {
          // an empty expected string means print output
          println(output)
        } else if (ss(0) == '$') {
          val regexp = outputPrefix(cmd) + ss.tail
          assertTrue(output matches regexp)
        } else {
          val expect = outputPrefix(cmd) + ss
          assertEquals(expect, output)
        }
      }
    }
  }

  type PNode = edu.umd.cs.piccolo.PNode
  type PPath = edu.umd.cs.piccolo.nodes.PPath

  def stripCrLfs(str: String) = str.replaceAll("\r?\n", "")

  val CL = java.awt.geom.PathIterator.SEG_CLOSE   // 4
  val CT = java.awt.geom.PathIterator.SEG_CUBICTO // 3
  val LT = java.awt.geom.PathIterator.SEG_LINETO  // 1
  val MT = java.awt.geom.PathIterator.SEG_MOVETO  // 0
  val QT = java.awt.geom.PathIterator.SEG_QUADTO  // 2
  val fmt = "%g"
  def segmentToString(t: Int, coords: Array[Double]) = t match {
    case MT =>
      "M" + (fmt format coords(0)) + "," + (fmt format coords(1)) + " "
    case LT =>
      "L" + (fmt format coords(0)) + "," + (fmt format coords(1)) + " "
    case QT =>
      "Q" + (fmt format coords(0)) + "," + (fmt format coords(1)) + " " +
      (fmt format coords(2)) + "," + (fmt format coords(3)) + " "
    case CT =>
      "C" + (fmt format coords(0)) + "," + (fmt format coords(1)) + " " +
      (fmt format coords(2)) + "," + (fmt format coords(3)) + " " +
      (fmt format coords(4)) + "," + (fmt format coords(5)) + " "
    case CL =>
      "z "
  }

  def pathIteratorToString(pi: java.awt.geom.PathIterator) = {
    var res = new StringBuffer
    while (!pi.isDone) {
      pi.next
      val coords = Array[Double](0, 0, 0, 0, 0, 0)
      val t = pi.currentSegment(coords)
      res.append(segmentToString(t, coords))
    }
    res.toString
  }

  def pathReferenceToString(pr: java.awt.geom.Path2D) = {
    val at = new java.awt.geom.AffineTransform
    val pi = pr.getPathIterator(at)
    pathIteratorToString(pi)
  }

  def pathData(polyLine: kgeom.PolyLine) = {
    pathReferenceToString(polyLine.polyLinePath)
  }
  def pathData(ppath: PPath) = {
    pathReferenceToString(ppath.getPathReference)
  }

  def makeString(pnode: PNode) = {
    val x = pnode.getX.round + 1
    val y = pnode.getY.round + 1
    if (pnode.isInstanceOf[kgeom.PolyLine]) {
      "PolyLine(" + (x + 1) + "," + (y + 1) + " " +
      pathData(pnode.asInstanceOf[kgeom.PolyLine]) + ")"
    }
    else if (pnode.isInstanceOf[PPath]) {
      "PPath(" + x + "," + y + " " +
      pathData(pnode.asInstanceOf[PPath]) + ")"
    }
    else pnode.toString
  }

}

