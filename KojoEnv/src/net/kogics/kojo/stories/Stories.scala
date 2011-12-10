/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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
package stories

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io._
import util.Utils
import util.Utils.BundleMessage

class Stories extends ActionListener {
  def actionPerformed(e: ActionEvent) {
    val ces = CodeExecutionSupport.instance()
    try {
      ces.closeFileAndClrEditor()
      ces.codePane.setText(getCode(e).trim())
      ces.codePane.setCaretPosition(0)
      CodeEditorTopComponent.findInstance().requestActive()
      Utils.schedule(0.1) {
        ces.runCode()
      }
    }
    catch {
      case e: RuntimeException => // user cancelled
    }
  }

  def getCode(e: ActionEvent) = {
    val klass = classOf[Stories]
    val KojoOverview = BundleMessage(klass, "CTL_KojoOverview")
    val ScalaTut = BundleMessage(klass, "CTL_ScalaTut")
    val Pictures = BundleMessage(klass, "CTL_Pictures")
    val Snippets = BundleMessage(klass, "CTL_Snippets")
    val AddFrac = BundleMessage(klass, "CTL_AddFrac")
    val Pi = BundleMessage(klass, "CTL_Pi")
    val SimpleStory = BundleMessage(klass, "CTL_Simple")
    val LearnMore = BundleMessage(klass, "CTL_LearnMore")
    val MathworldIntro = BundleMessage(klass, "CTL_MathworldIntro")
    val ComposingMusic = BundleMessage(klass, "CTL_ComposingMusic")
    val TurtleCommands = BundleMessage(klass, "CTL_TurtleCommands")
    val ScalatestSetup = BundleMessage(klass, "CTL_ScalaTestSetup")

    e.getActionCommand match {
      case KojoOverview(_) => Utils.readFile(storyStream("kojo-overview.kojo"))
      case ScalaTut(_) => Utils.readFile(storyStream("scala-tutorial.kojo"))
      case Pictures(_) => Utils.readFile(storyStream("pictures.kojo"))
      case Snippets(_) => Utils.readFile(storyStream("code-snippets.kojo"))
      case AddFrac(_) => Utils.readFile(storyStream("adding-fractions.kojo"))
      case Pi(_) => Utils.readFile(storyStream("archimedes-pi.kojo"))
      case SimpleStory(_) => Utils.readFile(storyStream("simple-story.kojo"))
      case LearnMore(_) => Utils.readFile(storyStream("learn.kojo"))
      case MathworldIntro(_) => Utils.readFile(storyStream("mathworld-intro.kojo"))
      case ComposingMusic(_) => Utils.readFile(storyStream("composing-music.kojo"))
      case  TurtleCommands(_) => Utils.readFile(storyStream("turtle-commands.kojo"))
      case  ScalatestSetup(_) => Utils.readFile(storyStream("scalatest-setup.kojo"))
    }
  }

  def storyStream(fname: String) = {
    val base = Utils.installDir + File.separator + "../stories"
    CodeEditorTopComponent.findInstance().setLastLoadStoreDir(base)
    new FileInputStream(base + File.separator + fname)
  }
}
