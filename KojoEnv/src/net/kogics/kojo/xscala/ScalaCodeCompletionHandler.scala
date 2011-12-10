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

import javax.swing._
import javax.swing.text.{BadLocationException, JTextComponent}
import java.util.logging._


import org.netbeans.editor.{BaseDocument, Utilities}
import org.netbeans.modules.csl.api._
import CodeCompletionHandler._
import org.netbeans.modules.csl.spi.{DefaultCompletionResult, ParserResult}
import org.netbeans.modules.parsing.spi.indexing.support.QuerySupport
import org.openide.util.{Exceptions, NbBundle}
import org.openide.filesystems.FileObject

import net.kogics.kojo.util._
import net.kogics.kojo.core.CodeCompletionSupport

// import org.netbeans.api.language.util.ast.AstElementHandle


class ScalaCodeCompletionHandler(completionSupport: CodeCompletionSupport) extends CodeCompletionHandler {
  val Log = Logger.getLogger(getClass.getName);

  class ScalaElementHandle(name: String, offset: Int, kind: ElementKind) extends ElementHandle {
    def getFileObject: FileObject = null
    def getMimeType: String = org.netbeans.modules.scala.core.ScalaMimeResolver.MIME_TYPE
    def getName: String = name
    def getIn: String = null
    def getKind: ElementKind = kind
    def getModifiers: java.util.Set[Modifier] = java.util.Collections.emptySet[Modifier]
    def signatureEquals(handle: ElementHandle): Boolean = false;
    def getOffsetRange(result: ParserResult): OffsetRange = new OffsetRange(0,offset)
  }

  class ScalaCompletionProposal(offset: Int, proposal: String, kind: ElementKind, 
                                template: String = null,
                                icon: ImageIcon = null) extends CompletionProposal {
    val elemHandle = new ScalaElementHandle(proposal, offset, kind)
    def getAnchorOffset: Int = offset
    def getName: String = proposal
    def getInsertPrefix: String = proposal
    def getSortText: String = proposal
    def getSortPrioOverride: Int = 0
    def getElement: ElementHandle = elemHandle
    def getKind: ElementKind = kind
    def getIcon: ImageIcon = icon
    def getLhsHtml(fm: HtmlFormatter): String = {
      val kind = getKind
      fm.name(kind, true)
      fm.appendText(proposal)
      fm.name(kind, false)
      fm.getText
    }
    def getRhsHtml(fm: HtmlFormatter): String = ""
    def getModifiers: java.util.Set[Modifier] = elemHandle.getModifiers
    override def toString: String = "Proposal(%s, %s)" format(proposal, template)
    def isSmart: Boolean = false
    def getCustomInsertTemplate: String = template
  }

  val scalaImageIcon = Utils.loadIcon("/images/scala16x16.png")

  def methodTemplate(completion: String) = {
    CodeCompletionUtils.BuiltinsMethodTemplates.getOrElse(
      completion,
      CodeCompletionUtils.ExtraMethodTemplates.getOrElse(completion, null)
    )
  }

  override def complete(context: CodeCompletionContext): CodeCompletionResult = {
    if (context.getQueryType != QueryType.COMPLETION) {
      return null
    }
    
    val proposals = new java.util.ArrayList[CompletionProposal]
    val caretOffset = context.getCaretOffset

    val (methodCompletions, moffset) = completionSupport.methodCompletions(caretOffset)
    methodCompletions.foreach { completion =>
      proposals.add(new ScalaCompletionProposal(caretOffset - moffset, completion,
                                                ElementKind.METHOD,
                                                methodTemplate(completion)))
    }

    val (varCompletions, voffset) = completionSupport.varCompletions(caretOffset)
    varCompletions.foreach { completion =>
      proposals.add(new ScalaCompletionProposal(caretOffset - voffset, completion, 
                                                ElementKind.VARIABLE,
                                                methodTemplate(completion)))
    }

    val (keywordCompletions, koffset) = completionSupport.keywordCompletions(caretOffset)
    keywordCompletions.foreach { completion =>
      proposals.add(new ScalaCompletionProposal(caretOffset - koffset, completion,
                                                ElementKind.KEYWORD,
                                                CodeCompletionUtils.KeywordTemplates.getOrElse(completion, null),
                                                scalaImageIcon))
    }

    if (proposals.size > 1)
      proposals.add(new ScalaCompletionProposal(caretOffset, "               ", ElementKind.OTHER))

    val completionResult = new DefaultCompletionResult(proposals, false)
    // the line of code below seems to fix the completion filtering problem, 
    // in which more specific completions disappeared as you typed more
    completionResult.setFilterable(false) 
//    Log.info("Completion Result: " + proposals)
    return completionResult
  }

  override def document(pr: ParserResult, element: ElementHandle): String = {
    null
//    "Documentation not available yet."
  }

  override def resolveLink(link: String, elementHandle: ElementHandle): ElementHandle = {
    null
  }

  override def getPrefix(info: ParserResult, lexOffset: Int, upToOffset: Boolean): String = {
    ""
  }

  override def getAutoQuery(component: JTextComponent, typedText: String): QueryType = {
    typedText.charAt(0) match {
      // TODO - auto query on ' and " when you're in $() or $F()
      case '\n' | '(' | '[' | '{' |';' => return QueryType.STOP
      case '.' => return QueryType.COMPLETION
      case _ => return QueryType.NONE
    }
  }


  override def resolveTemplateVariable(variable: String, info: ParserResult, caretOffset: Int,
                                       name: String , parameters: java.util.Map[_, _]): String = {
    throw new UnsupportedOperationException("Not supported yet.")
  }

  override def getApplicableTemplates(doc:  javax.swing.text.Document, selectionBegin: Int, selectionEnd: Int): java.util.Set[String] = {
    java.util.Collections.emptySet[String]
  }

  override def parameters(info: ParserResult, lexOffset: Int, proposal: CompletionProposal): ParameterInfo = {
    ParameterInfo.NONE
  }
}
