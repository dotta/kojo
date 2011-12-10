/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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

import java.util.concurrent.CountDownLatch
import org.junit.Test
import org.junit.Assert._

class CodePaneCRunnerTest extends CodePaneTestBase {
  def runCode() {
    latch = new CountDownLatch(1)
    codeRunner.compileRunCode(pane.getText())
    latch.await()
  }

  @Test
  def testEvalSession = {
    pane.setText("print(12)")
    runCtx.success.set(false)
    runCode()
    assertTrue(runCtx.success.get)
    assertEquals("12",
                 stripCrLfs(runCtx.getCurrentOutput))

    pane.setText("print(13)")
    runCode()
    assertEquals(stripCrLfs(Delimiter) +
                 "12" +
                 stripCrLfs(Delimiter) +
                 "13",
                 stripCrLfs(runCtx.getCurrentOutput))



    runCtx.clearOutput
    assertEquals("", runCtx.getCurrentOutput)

    pane.setText("while (true) {println(\"42\")}")
    scheduleInterruption()
    runCode()
    Thread.sleep(500)
    println("***********Post Interruption Output: " + runCtx.getCurrentOutput)
    assertTrue(runCtx.getCurrentOutput.contains("Script Stopped."))

    runCtx.clearOutput
    assertEquals("", runCtx.getCurrentOutput)

    pane.setText("while (true) {forward(100)}")
    scheduleInterruption()
    runCode()
    Thread.sleep(500)
    assertTrue(runCtx.getCurrentOutput.contains("Script Stopped."))
  }

  @Test
  def testTwoEvalsAndAnError = {
    pane.setText("print(12)")
    runCode()
    assertTrue(runCtx.getCurrentOutput.contains("12"))

    pane.setText("print(13)")
    runCode()
    assertTrue(runCtx.getCurrentOutput.contains("13"))

    runCtx.clearOutput

    pane.setText("some junk")
    runCode()

    assertTrue(runCtx.getCurrentOutput.contains("not found: value some"))
  }
}

