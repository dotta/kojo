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
package story

import java.awt._
import java.awt.event._
import javax.swing._
import util.Utils
import util.Read
import javazoom.jl.player.Player
import java.io._
import javax.swing.text.html.HTMLDocument
import java.util.logging._

object StoryTeller extends InitedSingleton[StoryTeller] {
  def initedInstance(kojoCtx: KojoCtx) = synchronized {
    instanceInit()
    val ret = instance()
    ret.kojoCtx = kojoCtx
    ret
  }

  protected def newInstance = new StoryTeller
}

class StoryTeller extends JPanel {
  val Log = Logger.getLogger(getClass.getName);
  val NoText = <span/>
  @volatile var kojoCtx: core.KojoCtx = _
  @volatile var mp3Player: Option[Player] = None
  @volatile var bgmp3Player: Option[Player] = None
  @volatile var currStory: Option[Story] = None
  @volatile var savedStory: Option[Story] = None

  def running = currStory.isDefined
  def story = currStory.get

  var outputFn: String => Unit = { msg =>
    Log.info(msg)
  }

  val pageFields = new collection.mutable.HashMap[String, JTextField]()
  val defaultMsg =
    <div style="text-align:center;color:#808080;font-size:15px">
      {for (idx <- 1 to 6) yield {
          <br/>
        }}
      <p>
        Run a story by loading/writing your story script within the <em>Script Editor</em>, and
        then clicking the <em>Run</em> button.
      </p>
      <p>
        You can control a running story via buttons that appear at the bottom of this pane.
      </p>
    </div>

//  setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
  setLayout(new BorderLayout())

  val ep = new JEditorPane()
  ep.setEditorKit(CustomHtmlEditorKit())

  ep.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
  ep.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20))
  ep.setEditable(false)
  ep.addHyperlinkListener(new LinkListener(this))
  ep.setBackground(Color.white)
  val sp = new JScrollPane(ep)
  sp.setBorder(BorderFactory.createEmptyBorder())
  add(sp, BorderLayout.CENTER)

  val holder = new JPanel()
  holder.setBackground(Color.white)
  holder.setLayout(new BoxLayout(holder, BoxLayout.Y_AXIS))

  val uc = new JPanel
  uc.setBackground(Color.white)
  holder.add(uc)

  val (cp, prevButton, nextButton) = makeControlPanel()
  holder.add(cp)

  val statusBar = new JLabel()
  val pageNumBar = new JLabel()
  pageNumBar.setBorder(BorderFactory.createEtchedBorder())
//  statusBar.setPreferredSize(new Dimension(100, 16))
  val sHolder = new JPanel()
  sHolder.setLayout(new BorderLayout())
  sHolder.add(statusBar, BorderLayout.CENTER)
  sHolder.add(pageNumBar, BorderLayout.EAST)

  holder.add(sHolder)
  add(holder, BorderLayout.SOUTH)
  displayContent(defaultMsg)

  def makeControlPanel(): (JPanel, JButton, JButton) = {
    val cp = new JPanel
    cp.setBackground(Color.white)
    cp.setBorder(BorderFactory.createEtchedBorder())

    val prevBut = new JButton()
    prevBut.setIcon(Utils.loadIcon("/images/back.png"))
    prevBut.setToolTipText("Previous View")
    prevBut.addActionListener(new ActionListener {
        def actionPerformed(e: ActionEvent) {
          prevPage()
        }
      })
    cp.add(prevBut)

    val stopBut = new JButton()
    stopBut.setIcon(Utils.loadIcon("/images/stop.png"))
    stopBut.setToolTipText("Stop Story")
    stopBut.addActionListener(new ActionListener {
        def actionPerformed(e: ActionEvent) {
          done()
        }
      })
    cp.add(stopBut)

    val nextBut = new JButton()
    nextBut.setIcon(Utils.loadIcon("/images/forward.png"))
    nextBut.setToolTipText("Next View")
    nextBut.addActionListener(new ActionListener {
        def actionPerformed(e: ActionEvent) {
          nextPage()
        }
      })
    cp.add(nextBut)

    cp.setVisible(false)
    (cp, prevBut, nextBut)
  }

  def ensureVisible() {
    kojoCtx.makeStoryTellerVisible()
  }

  def updateCp() {
    if (story.hasPrevView) {
      prevButton.setEnabled(true)
    }
    else {
      prevButton.setEnabled(false)
    }

    if (story.hasNextView) {
      nextButton.setEnabled(true)
    }
    else {
      nextButton.setEnabled(false)
    }
  }

  private def clearHelper() {
    // needs to run on GUI thread
    newPage()
    displayContent(NoText)
  }

  def showCurrStory() {
    Utils.runInSwingThread {
      newPage()
      displayContent(story.view)
      updateCp()
    }
  }

  private def prevPage() {
    // needs to run on GUI thread
    newPage()
    if (story.hasPrevView) {
      story.back()
      displayContent(story.view)
      updateCp()
    }
    else {
      done()
    }
  }

  def nextPage() {
    // needs to run on GUI thread
    newPage()

    if (story.hasNextView) {
      story.forward()
      displayContent(story.view)
      updateCp()
    }
    else {
      done()
    }
  }

  private def newPage() {
    // needs to run on GUI thread
    uc.removeAll()
    uc.setBorder(BorderFactory.createEmptyBorder())
    
    pageFields.clear()
//    clearStatusBar()

    kojoCtx.stopAnimation()
    repaint()
    stopMp3Player()
  }

  def clear() {
    Utils.runInSwingThread {
      clearHelper()
      ensureVisible()
      cp.setVisible(true)
      val doc = ep.getDocument.asInstanceOf[HTMLDocument]
      doc.setBase(new java.net.URL("file:///" + kojoCtx.baseDir))
      ep.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
    }
  }

  def pageNumber(name: String): Option[Int] = story.pageNumber(name)

  def viewPage(page: Int, view: Int) {
    if (story.hasView(page, view)) {
      story.goto(page, view)
      displayContent(story.view)
      updateCp()
    }
    else {
      showStatusError("Nonexistent page#view - %d#%d" format(page, view))
    }
  }

  def done() {
    Utils.runInSwingThread {
      if (savedStory.isDefined) {
        currStory = savedStory
        savedStory = None
        showCurrStory()
      }
      else {
        currStory = None
        clearHelper()
        stopBgMp3Player()
        cp.setVisible(false)
        displayContent(defaultMsg)
      }
    }
  }

  private def scrollEp() {
    if (currStory.isDefined) {
      if (story.scrollToEnd) {
        scrollToEnd()
      }
      else {
        scrollToBeginning()
      }
    }
    else {
      scrollToBeginning()
    }
  }

  private def scrollToEnd() {
    Utils.schedule(0.3) {
      val sb = sp.getVerticalScrollBar
      sb.setValue(sb.getMaximum)
    }
  }

  private def scrollToBeginning() {
    Utils.schedule(0.3) {
      val sb = sp.getVerticalScrollBar
      sb.setValue(sb.getMinimum)
    }
  }

  private def displayContent(html: xml.Node) {
    Utils.runInSwingThread {
      clearStatusBar()
      ep.setText(html.toString)
      scrollEp()
    }
  }

  def addField(label: String, deflt: Any): JTextField = {
    val l = new JLabel(label)
    val default = deflt.toString
    val tf = new JTextField(default, if (default.length < 6) 6 else default.length)

    Utils.runInSwingThread {
      uc.add(l)
      uc.add(tf)
      uc.setBorder(BorderFactory.createEtchedBorder())
      uc.revalidate()
      uc.repaint()
      pageFields += (label -> tf)
      scrollEp()
    }
    tf
  }

  def fieldValue[T](label: String, default: T)(implicit reader: Read[T]): T = {
    Utils.runInSwingThreadAndWait {
      val tf = pageFields.get(label)
      if (tf.isDefined) {
        val svalue = tf.get.getText
        if (svalue != null && svalue.trim != "") {
          try {
            reader.read(svalue)
          }
          catch {
            case ex: Exception =>
              showStatusError("Unable to convert value - %s - to required type %s" format(svalue, reader.typeName))
              throw ex
          }
        }
        else {
          tf.get.setText(default.toString)
          default
        }
      }
      else {
        showStatusError("Field with label - %s is not defined" format(label))
        throw new IllegalArgumentException("Field with label - %s is not defined" format(label))
      }
    }
  }

  def addButton(label: String)(fn: => Unit) {
    val but = new JButton(label)
    but.addActionListener(new ActionListener {
        def actionPerformed(e: ActionEvent) {
          clearStatusBar()
          Utils.stopMonitoredThreads()
          Utils.runAsyncMonitored {
            fn
          }
        }
      })

    addUiComponent(but)
  }

  def addUiComponent(c: JComponent) {
    Utils.runInSwingThread {
      uc.add(c)
      uc.setBorder(BorderFactory.createEtchedBorder())
      val numC = uc.getComponentCount
      if (numC > 4) {
        // hack to allow second row of components
        val spacing = 5
        val rowHeight = 20
        uc.setPreferredSize(new Dimension(20, rowHeight * (numC/4 + 1) + spacing))
      }
      uc.revalidate()
      uc.repaint()
      scrollEp()
    }
  }

  addComponentListener(new ComponentAdapter {
      override def componentResized(e: ComponentEvent) {
        statusBar.setPreferredSize(new Dimension(getSize().width-6, 16))
      }
    })

  def clearStatusBar() {
    Utils.runInSwingThread {
      statusBar.setForeground(Color.black)
      statusBar.setText("")
      pageNumBar.setText(storyLocation)
    }
  }

  def storyLocation = {
    if (currStory.isDefined) {
      if (savedStory.isDefined) {
        "St 2, Pg %d#%d" format(story.location._1, story.location._2)

      }
      else {
        "Pg %d#%d" format(story.location._1, story.location._2)
      }
    }
    else {
      ""
    }
  }

  def showStatusMsg(msg: String, output: Boolean = true) {
    Utils.runInSwingThread {
      statusBar.setForeground(Color.black)
      statusBar.setText(msg)
    }
    if (output) {
      outputFn("[Storyteller] %s\n" format(msg))
    }
  }

  def showStatusError(msg: String) {
    Utils.runInSwingThread {
      statusBar.setForeground(Color.red)
      statusBar.setText(msg)
    }
    outputFn("[Storyteller] %s\n" format(msg))
  }

  private def playHelper(mp3File: String)(fn: (FileInputStream) => Unit) {
    val f = new File(mp3File)
    val f2 = if (f.exists) f else new File(kojoCtx.baseDir + mp3File)

    if (f2.exists) {
      val is = new FileInputStream(f2)
      fn(is)
//      is.close() - player closes the stream
    }
    else {
      showStatusError("MP3 file - %s does not exist" format(mp3File))
    }
  }


  def play(mp3File: String) = playHelper(mp3File) {is =>
    stopMp3Player()
    Utils.runAsync {
      mp3Player = Some(new Player(is))
      mp3Player.get.play
    }
  }
  
  def playInBg(mp3File: String): Unit = playHelper(mp3File) {is =>
    if (bgmp3Player.isDefined && !bgmp3Player.get.isComplete) {
      showStatusError("Can't play second background mp3")
      return
    }

    Utils.runAsync {
      bgmp3Player = Some(new Player(is))
      bgmp3Player.get.play
      if (running) {
        // loop bg music
        playInBg(mp3File)
      }
    }
  }

  def playStory(story: Story) {
    if (savedStory.isDefined) {
      showCurrStory()
      throw new IllegalArgumentException("Can't run more than two stories. Stop the currently active story and try again.")
    }

    if (currStory.isDefined) {
      savedStory = currStory
    }
    currStory = Some(story)
    showCurrStory()
  }

  def stopMp3Player() {
    if (mp3Player.isDefined && !mp3Player.get.isComplete) {
      mp3Player.get.close()
      mp3Player = None
    }
  }

  def stopBgMp3Player() {
    if (bgmp3Player.isDefined && !bgmp3Player.get.isComplete) {
      bgmp3Player.get.close()
      bgmp3Player = None
    }
  }

  def storyComing() {
    ensureVisible()
    ep.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
  }

  def storyAborted() {
    ep.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
  }
  
  def addLinkHandler[T](name: String, story0: Story)(hm: HandlerHolder[T]) = {
    story0.addLinkHandler(name)(hm)
  }
  
  def handleLink(name: String, data: String) {
    story.handleLink(name, data)
  }
}
