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
import net.kogics.kojo.core.RunContext

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{CountDownLatch, TimeUnit}

class TestRunContext(val base: StagingTestBase) extends RunContext {
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
    base.latch.countDown()
  }
  def onRunSuccess() {
    success.set(true)
    base.latch.countDown()
  }
  def onRunInterpError() {
    base.latch.countDown()
  }
  def onInternalCompilerError() {
    base.latch.countDown()
  }

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
  def onCompileError() {}
  def onCompileSuccess() {}
}
