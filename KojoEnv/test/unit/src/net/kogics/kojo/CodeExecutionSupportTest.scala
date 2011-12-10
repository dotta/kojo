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

import java.io.File
import net.kogics.kojo.util._

class CodeExecutionSupportTest extends KojoTestBase {

  initNetbeansDirs()

  try {
    val ce0 = CodeExecutionSupport.instance()
  }
  catch {
    case e: IllegalStateException => assertTrue(true)
  }

  val ce = CodeExecutionSupport.initedInstance(new javax.swing.JEditorPane(), new org.openide.awt.UndoRedo.Manager)
  val pane = ce.codePane

  val commandHistory = CommandHistory.instance
  commandHistory.setListener(new HistoryListener {
      def itemAdded {
        ce.loadCodeFromHistory(commandHistory.size)
      }

      def selectionChanged(n: Int) {
        ce.loadCodeFromHistory(n)
      }

      def ensureVisible(n: Int) {}
    })


  @Test
  def testRunAllText {
    pane.setText("val x = 10; val y = 20")
    val output = ce.runCodeWithOutputCapture()
    assertEquals("x: Int = 10y: Int = 20", stripCrLfs(output))
    Utils.runInSwingThreadAndWait {  /* noop */  }
    assertEquals("", pane.getText)
    assertTrue(pane.getSelectionStart == pane.getSelectionEnd)
  }

  @Test
  def testRunAllTextWithError {
    val code = "val x = 10; val y = 20x"
    pane.setText(code)
    val output = ce.runCodeWithOutputCapture()
    assertTrue(output.contains("error:"))
    Utils.runInSwingThreadAndWait {  /* noop */  }
    assertEquals(code, pane.getText)
    assertTrue(pane.getSelectionStart == pane.getSelectionEnd)
  }

  @Test
  def testRunSelectedText {
    val code = "val x = 10; val y = 20"
    pane.setText(code)
    pane.setSelectionStart(12)
    pane.setSelectionEnd(22)

    val output = ce.runCodeWithOutputCapture()
    assertEquals("y: Int = 20", stripCrLfs(output))
    Utils.runInSwingThreadAndWait {  /* noop */  }
    assertEquals(code, pane.getText)
    assertEquals(12, pane.getSelectionStart)
    assertEquals(22, pane.getSelectionEnd)
  }

  @Test
  def testRunSelectedTextWithError {
    val code = "val x = 10; val y = 20x"
    pane.setText(code)
    pane.setSelectionStart(12)
    pane.setSelectionEnd(23)

    val output = ce.runCodeWithOutputCapture()
    assertTrue(output.contains("error:"))
    Utils.runInSwingThreadAndWait {  /* noop */  }
    assertEquals(code, pane.getText)
    assertEquals(12, pane.getSelectionStart)
    assertEquals(23, pane.getSelectionEnd)
  }
  
  @Test
  def testRetainSingleLineCode {
    val code = "val x = 10; val y = 20"
    pane.setText(code)
    ce.retainCode = true
    val output = ce.runCodeWithOutputCapture()
    assertEquals("x: Int = 10y: Int = 20", stripCrLfs(output))
    Utils.runInSwingThreadAndWait {  /* noop */  }
    assertEquals(code, pane.getText)
    assertTrue(pane.getSelectionStart == pane.getSelectionEnd)

    ce.retainCode = false
    val output2 = ce.runCodeWithOutputCapture()
    assertEquals("x: Int = 10y: Int = 20", stripCrLfs(output2))
    Utils.runInSwingThreadAndWait {  /* noop */  }
    assertEquals("", pane.getText)
    assertTrue(pane.getSelectionStart == pane.getSelectionEnd)
  }
  
  @Test
  def testShowVerboseOutput {
    val code = "val x = 10\nval y = 20"
    pane.setText(code)
    ce.verboseOutput = true
    val output = ce.runCodeWithOutputCapture()
    assertEquals("x: Int = 10y: Int = 20", stripCrLfs(output))

    Utils.runInSwingThreadAndWait {  /* noop */  }

    pane.setText(code)
    ce.verboseOutput = false
    val output2 = ce.runCodeWithOutputCapture()
    assertEquals("", stripCrLfs(output2))
  }

  def stripCrLfs(str: String): String  = {
    val str0 = str.replaceAll("\r?\n", "")
    str0.replaceAll("---", "")
  }

  @Test
  def testIsSingleLine {
    val code = "12"
    assertTrue(ce.isSingleLine(code))
  }

  @Test
  def testIsSingleLine2 {
    val code = "12\n"
    assertTrue(ce.isSingleLine(code))
  }

  @Test
  def testIsSingleLine3 {
    val code = "12\n14"
    assertFalse(ce.isSingleLine(code))
  }

  @Test
  def testIsSingleLine4 {
    val code = sample.SampleCode.Rangoli
    assertFalse(ce.isSingleLine(code))
  }
}
