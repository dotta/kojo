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

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert._

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{CountDownLatch, TimeUnit}

import net.kogics.kojo.core.RunContext

abstract class CodePaneTestBase extends KojoTestBase {

  initNetbeansDirs()

  val runCtx = new RunContext {
    val currOutput = new StringBuilder()
    val success = new AtomicBoolean()
    val error = new AtomicBoolean()

    def inspect(obj: AnyRef) {}
    def onInterpreterInit() {}
    def showScriptInOutput() {}
    def hideScriptInOutput() {}
    def showVerboseOutput() {}
    def hideVerboseOutput() {}
    def retainSingleLineCode() {}
    def clearSingleLineCode() {}
    def reportRunError() {}
    def readInput(prompt: String) = ""

    def kprintln(outText: String) = reportOutput(outText)
    def reportOutput(lineFragment: String) {
      currOutput.append(lineFragment)
    }

    def onInterpreterStart(code: String) {}
    def clearOutput {currOutput.clear}
    def setScript(code: String) {}
    def getCurrentOutput: String  = currOutput.toString

    def onRunError() {
      error.set(true)
      latch.countDown()
    }
    def onRunSuccess() {
      success.set(true)
      latch.countDown()
    }
    def onRunInterpError() {latch.countDown()}
    def onInternalCompilerError() {latch.countDown()}

    def reportErrorMsg(errMsg: String) {
      currOutput.append(errMsg)
    }
    def reportErrorText(errText: String) {
      currOutput.append(errText)
    }

    def reportSmartErrorText(errText: String, line: Int, column: Int, offset: Int) {
      currOutput.append(errText)
    }
    def onCompileStart() {}
    def onCompileError() {
      error.set(true)
      latch.countDown()
    }
    def onCompileSuccess() {
      success.set(true)
      latch.countDown()
    }
  }

  val codeRunner = new xscala.ScalaCodeRunner(runCtx, SpriteCanvas.instance)
  codeRunner.activateTw()
  val pane = new javax.swing.JEditorPane()
  val Delimiter = ""
  var latch: CountDownLatch = _

  def scheduleInterruption() {
    new Thread(new Runnable {
        def run() {
          Thread.sleep(1000)
          codeRunner.interruptInterpreter()
        }
      }).start()
  }

  def runCode()

  def stripCrLfs(str: String) = str.replaceAll("\r?\n", "")
}
