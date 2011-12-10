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

import javax.swing.event.ChangeListener

import org.netbeans.modules.parsing.api._
import org.netbeans.modules.parsing.spi._
import org.netbeans.modules.csl.spi._
import org.netbeans.modules.csl.api._

class ScalaParser extends Parser {

  var snapshot: Snapshot = _

  @throws(classOf[ParseException])
  override def parse(snapshot: Snapshot, task: Task, event: SourceModificationEvent): Unit = {
    this.snapshot = snapshot
  }

  @throws(classOf[ParseException])
  override def getResult(task: Task): Parser.Result = {
    new ScalaParserResult(snapshot)
  }

  override def cancel {}
  override def addChangeListener(changeListener: ChangeListener): Unit = {}
  override def removeChangeListener(changeListener: ChangeListener): Unit = {}
}

class ScalaParserResult(snapshot: Snapshot) extends ParserResult(snapshot) {

  override protected def invalidate: Unit = {
  }

  override def getDiagnostics: java.util.List[_ <: Error] = {
    java.util.Collections.emptyList[Error]
  }
}