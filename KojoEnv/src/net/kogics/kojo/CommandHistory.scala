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

import scala.collection._
import java.io._

trait HistoryListener {
  def itemAdded
  def selectionChanged(n: Int)
  def ensureVisible(n: Int)
}

class HistorySaver {
  var writer = historyWriter(true)
  var starsWriter = new BufferedWriter(new FileWriter(CommandHistory.starsFile, true))

  def historyWriter(append: Boolean) = {
    new BufferedOutputStream(new FileOutputStream(CommandHistory.historyFile, append))
  }

  def append(script: String) {
    writer.write(script.getBytes("UTF-8"))
    writer.write(CommandHistory.Separator.getBytes("UTF-8"))
    writer.flush()
  }

  def appendStar(idx: Int) {
    starsWriter.append(idx.toString)
    starsWriter.append(CommandHistory.StarSeparator)
    starsWriter.flush()
  }

  def write(items: mutable.ArrayBuffer[String]) {
    writer.close()
    writer = historyWriter(false)
    items.foreach { hItem =>
      writer.write(hItem.getBytes("UTF-8"))
      writer.write(CommandHistory.Separator.getBytes("UTF-8"))
    }
    writer.close()
    writer = historyWriter(true)
  }

  def writeStars(stars: mutable.ArrayBuffer[Int]) {
    starsWriter.close()
    starsWriter = new BufferedWriter(new FileWriter(CommandHistory.starsFile, false))
    stars.foreach { idx =>
      starsWriter.append(idx.toString)
      starsWriter.append(CommandHistory.StarSeparator)
    }
    starsWriter.close()
    starsWriter = new BufferedWriter(new FileWriter(CommandHistory.starsFile, true))
  }
}

object CommandHistory extends Singleton[CommandHistory] {
//  val Log = java.util.logging.Logger.getLogger("CommandHistoryO");
  val Separator = "---Seperator---"
  val StarSeparator = ", "
  val MaxHistorySize = 1000
  val userDir = System.getProperty("netbeans.user")

  val historyFile = {
    val fileName = userDir + File.separator + "config" + File.separator + "history.txt"
    new File(fileName)
  }

  val starsFile = {
    val fileName = userDir + File.separator + "config" + File.separator + "stars.txt"
    new File(fileName)
  }

  def loadHistory(): String = {
    if (!historyFile.exists) {
      historyFile.createNewFile()
      ""
    }
    else {
      import net.kogics.kojo.util.RichFile._
      historyFile.readAsString
    }
  }

  def loadStars(): String = {
    if (!starsFile.exists) {
      starsFile.createNewFile()
      ""
    }
    else {
      import net.kogics.kojo.util.RichFile._
      starsFile.readAsString
    }
  }

  protected def newInstance = {
    val (history, stars) = (loadHistory(), loadStars())
    val hist = new CommandHistory(new HistorySaver(), CommandHistory.MaxHistorySize)
    hist.loadFrom(history, stars)
    hist
  }
}

class CommandHistory private[kojo] (historySaver: HistorySaver, maxHistorySize: Int) {
  val Log = java.util.logging.Logger.getLogger(getClass.getName)

  val history = new mutable.ArrayBuffer[String]
  val stars = new scala.collection.mutable.HashSet[Int]

  @volatile var hIndex = 0
  @volatile var listener: Option[HistoryListener] = None

  def setListener(l: HistoryListener) {
//    if (listener.isDefined) throw new IllegalArgumentException("Listener already defined")
    listener = Some(l)
  }

  def clearListener() {
    listener = None
  }

  def internalAdd(code: String) {
    history += code
    hIndex = history.size
  }

  def add(code: String) {
    internalAdd(code)
    if(listener.isDefined) listener.get.itemAdded
    historySaver.append(code)
  }

  def hasPrevious = hIndex > 0

  def toPosition(idx: Int): Option[String] = {
    if (idx < 0 || idx > history.size) None
    else {
      if (hIndex != idx) {
        hIndex = idx
        if(listener.isDefined) listener.get.selectionChanged(hIndex)
      }
      if (hIndex == history.size) None else Some(history(hIndex))
    }
  }

  def previous: Option[String] = {
    if (hIndex == 0) None
    else {
      hIndex -= 1
      if(listener.isDefined) listener.get.selectionChanged(hIndex)
      Some(history(hIndex))
    }
  }

  def next: Option[String] = {
    if (hIndex == history.size) None
    else {
      hIndex += 1
      if(listener.isDefined) listener.get.selectionChanged(hIndex)
      if (hIndex == history.size) None else Some(history(hIndex))
    }
  }

  def size = history.size
  def apply(idx: Int) = history(idx)
  
  def clear {
    history.clear
    hIndex = -1
    listener = None
  }

  def loadFrom(stringHistory: String, stringStars: String) {
//    Log.info("Loading History from: " + stringHistory)
    if (stringHistory == null || stringHistory.trim() == "") return
    
    val itemsA = stringHistory.split(CommandHistory.Separator)
    val starsA = stringStars.split(CommandHistory.StarSeparator)

    var items = new mutable.ArrayBuffer[String](); items.appendAll(itemsA)
    var stars0 = new mutable.ArrayBuffer[String](); stars0.appendAll(starsA)

    var stars1 = stars0.filter {sidx => sidx.trim() != ""}.map {sidx => sidx.toInt}

    if (items.size > maxHistorySize) {
      val extraItems = items.size - maxHistorySize
      val gone = stars1.filter { idx => idx < extraItems}.sortWith((e1, e2) => e1 < e2)
      val droppedStarredItems = gone.map {idx => items(idx)}

      items = items.slice(extraItems, items.size)
      items.insert(0, droppedStarredItems:_*)
      historySaver.write(items)

      stars1 = stars1.filterNot {idx => idx < extraItems}.map {idx => idx - extraItems + droppedStarredItems.size}
      val gone2 = new mutable.ArrayBuffer[Int]
      var dropped = 0
      var prev = -1
      for (i <- 0 until gone.size) {
        dropped += gone(i) - prev - 1
        prev = gone(i)
        gone2 += gone(i) - dropped
      }
      stars1.insert(0, gone2:_*)
      historySaver.writeStars(stars1)
    }

    items.foreach {hItem => internalAdd(hItem)}
    stars1.foreach {idx => stars += idx}
  }

  def addStar(idx: Int) {
    stars += idx
    historySaver.appendStar(idx)
  }

  def removeStar(idx: Int) {
    stars -= idx
    val stars0 = new mutable.ArrayBuffer[Int](); stars0.appendAll(stars)
    historySaver.writeStars(stars0)
  }

  def isStarred(idx: Int) = stars.contains(idx)

  def ensureVisible(idx: Int) {
    if(listener.isDefined) listener.get.ensureVisible(idx)
  }

  def ensureLastEntryVisible() {
    ensureVisible(hIndex)
  }
}
