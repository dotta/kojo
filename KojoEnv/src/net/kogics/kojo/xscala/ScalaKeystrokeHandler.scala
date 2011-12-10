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

import javax.swing.text.{BadLocationException, Caret, Document, JTextComponent}
import org.netbeans.api.lexer.{Token, TokenHierarchy, TokenId, TokenSequence}
import org.netbeans.editor.{BaseDocument, Utilities}
import org.netbeans.modules.csl.api.{EditorOptions, KeystrokeHandler, OffsetRange}
import org.netbeans.modules.csl.spi.ParserResult
import org.netbeans.modules.editor.indent.api.IndentUtils
import org.openide.util.Exceptions


class ScalaKeystrokeHandler extends KeystrokeHandler {

  @throws(classOf[BadLocationException])
  override def beforeCharInserted(document: Document, acaretOffset: Int, target: JTextComponent, c: Char): Boolean = {
    return false
  }

  @throws(classOf[BadLocationException])
  override def afterCharInserted(document: Document, dotPos: Int, target: JTextComponent, ch: Char): Boolean = {
    val doc = document.asInstanceOf[BaseDocument]
    val caret = target.getCaret

    ch match {
      case '[' | '(' | '{' | '"' | '\'' =>
        completeOpeningBracket(doc, dotPos, caret, ch)
      case _ => 
    }
    return true
  }

  @throws(classOf[BadLocationException])
  override def charBackspaced(document: Document, dotPos: Int, target: JTextComponent, ch: Char): Boolean = {
    return true
  }

  @throws(classOf[BadLocationException])
  override def beforeBreak(document: Document, aoffset: Int, target: JTextComponent): Int = {
    return -1
  }

  /** replaced by ScalaBracesMatcher#findMatching */
  override def findMatching(document: Document, aoffset: Int /*, boolean simpleSearch*/): OffsetRange = {
    OffsetRange.NONE
  }

  override def findLogicalRanges(info: ParserResult, caretOffset: Int): java.util.List[OffsetRange] = {
    return new java.util.ArrayList
  }

  override def getNextWordOffset(document: Document, offset: Int, reverse: Boolean): Int = {
    return -1
  }


  @throws(classOf[BadLocationException])
  private def completeOpeningBracket(doc: BaseDocument, dotPos: Int, caret: Caret, bracket: Char): Unit =  {
    if (isCompletablePosition(doc, dotPos + 1)) {
      val matchingBracket = "" + matching(bracket)
      doc.insertString(dotPos + 1, matchingBracket, null)
      caret.setDot(dotPos + 1)
    }
  }

  @throws(classOf[BadLocationException])
  private def isCompletablePosition(doc: BaseDocument, dotPos: Int): Boolean = true

  private def matching(bracket: Char): Char = {
    bracket match {
      case '"'  => '"'
      case '\'' => '\''
      case '('  => ')'
      case '/'  => '/'
      case '['  => ']'
      case '{'  => '}'
      case '}'  => '{'
      case '`'  => '`'
      case '<'  => '>'
      case _ => bracket
    }
  }
}