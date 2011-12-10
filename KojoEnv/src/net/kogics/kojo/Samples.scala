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

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import util.Utils

import sample.SampleCode

class Samples extends ActionListener {
  def actionPerformed(e: ActionEvent) {
    val ces = CodeExecutionSupport.instance()
    try {
      ces.closeFileAndClrEditor()
      ces.codePane.setText(SampleCode.get(e).trim())
      ces.codePane.setCaretPosition(0)
      CodeEditorTopComponent.findInstance().requestActive()
      Utils.schedule(0.1) {
        ces.activateTw()
        ces.runCode()
      }
    }
    catch {
      case e: RuntimeException => // user cancelled
    }
  }
}
