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

import org.netbeans.modules.csl.spi.DefaultLanguageConfig
import org.netbeans.api.lexer.{TokenId, Language}

class ScalaLanguage extends DefaultLanguageConfig {

  override
  def getDisplayName : String =  "Scala"

  override
  def getPreferredExtension : String = {
    "scala" // NOI18N
  }

  override
  def getLexerLanguage: Language[TokenId] = org.netbeans.modules.scala.core.lexer.ScalaTokenId.language

  override def getParser = new ScalaParser

  override def hasFormatter =  true
  override def getFormatter = new org.netbeans.modules.scala.editor.ScalaFormatter

  override def getKeystrokeHandler = new org.netbeans.modules.scala.editor.ScalaKeystrokeHandler

//  override def hasStructureScanner = true
//  override def getStructureScanner = new ScalaStructureScanner

  // bad package dependency below - need to fix this
  override def getCompletionHandler = new ScalaCodeCompletionHandler(net.kogics.kojo.CodeExecutionSupport.instance)
}

