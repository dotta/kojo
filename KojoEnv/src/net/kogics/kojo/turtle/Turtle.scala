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
package turtle

import javax.swing._
import java.awt.{Point => _, _}
import java.awt.geom._
import java.awt.event._
import java.util.logging._

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._
import edu.umd.cs.piccolo.util._
import edu.umd.cs.piccolo.activities.PActivity
import edu.umd.cs.piccolo.activities.PActivity.PActivityDelegate

import scala.collection._
import scala.actors._
import scala.actors.Actor._
import scala.{math => Math}

import net.kogics.kojo._
import net.kogics.kojo.util._
import net.kogics.kojo.kgeom._
import net.kogics.kojo.core._

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{CountDownLatch, TimeUnit}

class Turtle(canvas: SpriteCanvas, fname: String, initX: Double = 0d,
             initY: Double = 0, bottomLayer: Boolean = false) extends core.Turtle {

  import TurtleHelper._

  private val Log = Logger.getLogger(getClass.getName)
//  Log.info("Turtle being created in thread: " + Thread.currentThread.getName)

  private val layer = new PLayer
  def tlayer: PLayer = layer
  private val camera = canvas.getCamera
  // the zeroth layer is for the grid etc
  // bottom sprite layer is at index 1
  if (bottomLayer) camera.addLayer(1, layer) else camera.addLayer(camera.getLayerCount-1, layer)
  @volatile private [turtle] var _animationDelay = 0l

  private val turtleImage = new PImage(Utils.loadImage(fname))
  private val turtle = new PNode
//  turtleImage.getTransformReference(true).setToScale(1, -1)
  turtleImage.setOffset(-16, -16)

  private val xBeam = PPath.createLine(0, 30, 0, -30)
  xBeam.setStrokePaint(Color.gray)
  private val yBeam = PPath.createLine(-20, 0, 50, 0)
  yBeam.setStrokePaint(Color.gray)

  private [turtle] val penPaths = new mutable.ArrayBuffer[PolyLine]
  @volatile private var lineColor: Color = _
  @volatile private var fillColor: Color = _
  @volatile private var lineStroke: Stroke = _
  @volatile private var font: Font = _

  private val pens = makePens
  private val DownPen = pens._1
  private val UpPen = pens._2
  @volatile private[kojo] var pen: Pen = _

  @volatile private var _position: Point2D.Double = _
  @volatile private var theta: Double = _
  @volatile private var removed: Boolean = false

  private val CommandActor = makeCommandProcessor()
  @volatile private var geomObj: DynamicShape = _
  private val history = new mutable.Stack[UndoCommand]
  private val savedStyles = new mutable.Stack[Style]
  @volatile private var isVisible: Boolean = _
  @volatile private var areBeamsOn: Boolean = _

  private [turtle] def changePos(x: Double, y: Double) {
    _position = new Point2D.Double(x, y)
    turtle.setOffset(x, y)
  }

  private def changeHeading(newTheta: Double) {
    theta = newTheta
    turtle.setRotation(theta)
  }

  def distanceTo(x: Double, y: Double): Double = {
    distance(_position.x, _position.y, x, y)
  }

  private def towardsHelper(x: Double, y: Double): Double = {
    thetaTowards(_position.x, _position.y, x, y, theta)
  }

  def delayFor(dist: Double): Long = {
    if (_animationDelay < 1) {
      return _animationDelay
    }
    
    // _animationDelay is delay for 100 steps;
    // Here we calculate delay for specified distance
    val speed = 100f / _animationDelay
    val delay = Math.abs(dist) / speed
    delay.round
  }

  def dumpState() {
    Utils.runInSwingThread {
      val output = canvas.outputFn
      val cIter = layer.getChildrenReference.iterator
      output("Turtle Layer (%d children):\n" format(layer.getChildrenReference.size))
      while (cIter.hasNext) {
        val node = cIter.next.asInstanceOf[PNode]
        output(stringRep(node))
      }
    }
  }

  private def stringRep(node: PNode): String = node match {
    case l: PolyLine =>
      new StringBuilder().append("  Polyline:\n").append("    Points: %s\n" format l.points).toString
    case n: PNode =>
      new StringBuilder().append("  PNode:\n").append("    Children: %s\n" format n.getChildrenReference).toString
  }

  private def clearHistory() = history.clear()

  private def pushHistory(cmd: UndoCommand) {
    canvas.pushHistory(this)
    history.push(cmd)
  }

  private def popHistory(): UndoCommand = {
    canvas.popHistory()
    history.pop()
  }

  private [turtle] def init() {
    _animationDelay = 1000l
    clearHistory()
    changePos(initX, initY)
    layer.addChild(turtle)

    pen = DownPen
    pen.init()
    resetRotation()

    showWorker()
    beamsOffWorker()
  }

  init

  @volatile private var cmdBool = new AtomicBoolean(true)
  @volatile private var listener: TurtleListener = NoopTurtleListener

  private def thetaDegrees = Utils.rad2degrees(theta)
  private def thetaRadians = theta

  private def enqueueCommand(cmd: Command) {
    if (removed) return
    listener.hasPendingCommands
    listener.commandStarted(cmd)
    CommandActor ! cmd
    Throttler.throttle()
  }

  def syncUndo() {
    undo()
    // wait for undo to get done by reading animation delay value synchronously
    animationDelay
  }

  def undo() = enqueueCommand(Undo)
  def forward(n: Double) = enqueueCommand(Forward(n, cmdBool))
  def turn(angle: Double) = enqueueCommand(Turn(angle, cmdBool))
  def clear() = enqueueCommand(Clear(cmdBool))
  def penUp() = enqueueCommand(PenUp(cmdBool))
  def penDown() = enqueueCommand(PenDown(cmdBool))
  def towards(x: Double, y: Double) = enqueueCommand(Towards(x, y, cmdBool))
  def jumpTo(x: Double, y: Double) = enqueueCommand(JumpTo(x, y, cmdBool))
  def moveTo(x: Double, y: Double) = enqueueCommand(MoveTo(x, y, cmdBool))
  def setPenColor(color: Color) = enqueueCommand(SetPenColor(color, cmdBool))
  def setFillColor(color: Color) = enqueueCommand(SetFillColor(color, cmdBool))
  def saveStyle() = enqueueCommand(SaveStyle(cmdBool))
  def restoreStyle() = enqueueCommand(RestoreStyle(cmdBool))
  def beamsOn() = enqueueCommand(BeamsOn(cmdBool))
  def beamsOff() = enqueueCommand(BeamsOff(cmdBool))
  def write(text: String) = enqueueCommand(Write(text, cmdBool))
  def visible() = enqueueCommand(Show(cmdBool))
  def invisible() = enqueueCommand(Hide(cmdBool))
  def playSound(voice: Voice) = enqueueCommand(PlaySound(voice, cmdBool))
  def setAnimationDelay(d: Long) = {
    if (d < 0) {
      throw new IllegalArgumentException("Negative delay not allowed")
    }
    enqueueCommand(SetAnimationDelay(d, cmdBool))
  }
  def setPenThickness(t: Double) = {
    if (t < 0) {
      throw new IllegalArgumentException("Negative thickness not allowed")
    }
    enqueueCommand(SetPenThickness(t, cmdBool))
  }
  def setPenFontSize(n: Int) = {
    if (n < 0) {
      throw new IllegalArgumentException("Negative font size not allowed")
    }
    enqueueCommand(SetFontSize(n, cmdBool))
  }

  def remove() = {
    enqueueCommand(Remove(cmdBool))
    removed = true
  }

  private def getWorker(action: Symbol) {
    if (Utils.inSwingThread) {
      throw new RuntimeException("Can't read %s from Swing Thread\n" format(action.toString))
    }
    val latch = new CountDownLatch(1)
    val cmd = action match {
      case 'animationDelay => GetAnimationDelay(latch, cmdBool)
      case 'position => GetPosition(latch, cmdBool)
      case 'heading => GetHeading(latch, cmdBool)
      case 'state => GetState(latch, cmdBool)
      case 'style => GetStyle(latch, cmdBool)
    }

    enqueueCommand(cmd)

    var done = latch.await(10, TimeUnit.MILLISECONDS)
    while(!done) {
      listener.hasPendingCommands
      done = latch.await(10, TimeUnit.MILLISECONDS)
    }

    listener.pendingCommandsDone
  }

  def animationDelay: Long = {
    getWorker('animationDelay)
    _animationDelay
  }

  def position: Point = {
    getWorker('position)
    new Point(_position.getX, _position.getY)
  }

  def heading: Double = {
    getWorker('heading)
    thetaDegrees
  }

  def style: Style = {
    getWorker('style)
    currStyle
  }

  private def currStyle = Style(pen.getColor, pen.getThickness, pen.getFillColor, pen.getFontSize)

  def state: SpriteState = {
    def textNodes: scala.List[PText] = {
      var nodes: scala.List[PText] = Nil
      val iter = layer.getChildrenIterator
      while (iter.hasNext) {
        val child = iter.next
        if (child.isInstanceOf[PText]) {
          nodes = child.asInstanceOf[PText] :: nodes
        }
      }
      nodes
    }

    import java.lang.{Double => JDouble}

    getWorker('state)
    SpriteState(JDouble.doubleToLongBits(_position.x), JDouble.doubleToLongBits(_position.y),
                JDouble.doubleToLongBits(thetaDegrees),
                pen.getColor, JDouble.doubleToLongBits(pen.getThickness), pen.getFillColor,
                pen,
                textNodes,
                isVisible, areBeamsOn)
  }

  // invoke fn in GUI thread, supplying it doneFn to call at the end,
  // and wait for doneFn to get called
  // Kinda like SwingUtilities.invokeAndWait, except that it uses actor messages
  // and blocks inside actor.receive() - to give the actor thread pool a chance
  // to grow with the number of turtles
  private def realWorker(fn: (() => Unit) => Unit) {
    Utils.runInSwingThread {
      fn {() => workDone()}
    }
    waitForDoneMsg()
  }

  // invoke fn in GUI thread, and call doneFn after it is done
  private def realWorker2(fn:  => Unit) {
    realWorker { doneFn =>
      try {
        fn
      }
      finally {
        doneFn()
      }
    }
  }

  private def workDone() {
    CommandActor ! CommandDone
  }

  private def waitForDoneMsg() {
    CommandActor.receive {
      case CommandDone =>
    }
  }

  private def realWorker3(cmd: Command)(fn: (() => Unit) => Unit) {
    Utils.runInSwingThread {
      fn {() => asyncCmdDone(cmd)}
    }
  }

  private def realWorker4(cmd: Command)(fn:  => Unit) {
    realWorker3 (cmd) { doneFn =>
      try {
        fn
      }
      finally {
        doneFn()
      }
    }
  }

  private def asyncCmdDone(cmd: Command) {
    listener.commandDone(cmd)
  }

  // real* methods are called in the Agent thread
  // realWorker allows them to do work in the GUI thread
  // but they need to call doneFn() at the end to carry on
  // after the GUI thread is done - because the processCommand method
  // waits for a CommandDone msg after calling the work funtion -
  // and doneFn sends this msg to the actor from the GUI thread
  // In an earlier version of the code, a latch was used to synchronize between the
  // GUI and actor threads. But after a certain Scala 2.8.0 nightly build, this
  // resulted in thread starvation in the Actor thread-pool
  
  private def realForwardCustom(n: Double, cmd: Command, saveUndoInfo: Boolean = true) {

    def maybeSaveUndoCmd() {
      if (saveUndoInfo) {
        pushHistory(UndoChangeInPos((_position.x, _position.y)))
      }
    }

    def newPoint = {
      val p0 = _position
      val p1 = posAfterForward(p0.x, p0.y, theta, n)
      new Point2D.Double(p1._1, p1._2)
    }

    def endMove(pf: Point2D.Double) {
      pen.endMove(pf.x, pf.y)
      changePos(pf.x, pf.y)
      turtle.repaint()
    }

    if (Utils.doublesEqual(n, 0, 0.001)) {
      asyncCmdDone(cmd)
      return
    }

    val aDelay = delayFor(n)

    if (aDelay < 10) {
      if (aDelay > 1) {
        Thread.sleep(aDelay)
      }

      realWorker4(cmd) {
        maybeSaveUndoCmd()
        val pf = newPoint
        endMove(pf)
      }
    }
    else {
      realWorker { doneFn =>
        def manualEndMove(pt: Point2D.Double) {
          endMove(pt)
          asyncCmdDone(cmd)
          doneFn()
        }
        maybeSaveUndoCmd()
        val p0 = _position
        var pf = newPoint
        pen.startMove(p0.x, p0.y)

        val lineAnimation = new PActivity(aDelay) {
          override def activityStep(elapsedTime: Long) {
            val frac = elapsedTime.toDouble / aDelay
            val currX = p0.x * (1-frac) + pf.x * frac
            val currY = p0.y * (1-frac) + pf.y * frac
            if (cmd.valid.get) {
              pen.move(currX, currY)
              turtle.setOffset(currX, currY)
              turtle.repaint()
            }
            else {
              pf = new Point2D.Double(currX, currY)
              terminate()
            }
          }
        }

        lineAnimation.setDelegate(new PActivityDelegate {
            override def activityStarted(activity: PActivity) {}
            override def activityStepped(activity: PActivity) {}
            override def activityFinished(activity: PActivity) {
              manualEndMove(pf)
            }
          })

        canvas.getRoot.addActivity(lineAnimation)
      }
    }
  }

  private def realTurn(angle: Double, cmd: Command) {
    pushHistory(UndoChangeInHeading(theta))
    val newTheta = thetaAfterTurn(angle, theta)
    changeHeading(newTheta)
    turtle.repaint()
  }

  private def realClear() {
    pen.clear()
    layer.removeAllChildren() // get rid of stuff not written by pen, like text nodes
    init()
    // showWorker() - if we want an invisible turtle to show itself after a clear
    turtle.repaint()
    canvas.afterClear()
  }

  private def realRemove() {
    pen.clear
    layer.removeChild(turtle)
    camera.removeLayer(layer)
  }

  private def realPenUp(cmd: Command) {
    pushHistory(UndoPenState(pen))
    pen = UpPen
  }

  private def realPenDown(cmd: Command) {
    if (pen != DownPen) {
      pushHistory(UndoPenState(pen))
      pen = DownPen
      pen.updatePosition()
    }
  }

  private def realTowards(x: Double, y: Double, cmd: Command) {
    pushHistory(UndoChangeInHeading(theta))
    val newTheta = towardsHelper(x, y)
    changeHeading(newTheta)
    turtle.repaint()
  }

  private def realJumpTo(x: Double, y: Double, cmd: Command) {

    val undoCmd =
      if (pen == UpPen)
        UndoChangeInPos((_position.x, _position.y))
    else
      CompositeUndoCommand(
        scala.List(
          UndoPenState(pens._2),
          UndoChangeInPos((_position.x, _position.y)),
          UndoPenState(pens._1)
        )
      )
    pushHistory(undoCmd)

    changePos(x, y)
    pen.updatePosition()
    turtle.repaint()
  }

  private def realMoveToCustom(x: Double, y: Double, cmd: Command) {
    def undoCmd = CompositeUndoCommand(
      scala.List(
        UndoChangeInPos((_position.x, _position.y)),
        UndoChangeInHeading(theta)
      )
    )

    realWorker2 {
      pushHistory(undoCmd)
      val newTheta = towardsHelper(x, y)
      changeHeading(newTheta)
    }
    realForwardCustom(distanceTo(x,y), cmd, false)
  }

  private def realSetAnimationDelay(d: Long, cmd: Command) {
    _animationDelay = d
  }

  private def realGetWorker() {
  }

  private def realSetPenColor(color: Color, cmd: Command) {
    pushHistory(UndoPenAttrs(pen.getColor, pen.getThickness, pen.getFillColor, pen.getFontSize))
    pen.setColor(color)
  }

  private def realSetPenThickness(t: Double, cmd: Command) {
    pushHistory(UndoPenAttrs(pen.getColor, pen.getThickness, pen.getFillColor, pen.getFontSize))
    pen.setThickness(t)
  }

  private def realSetFontSize(n: Int, cmd: Command) {
    pushHistory(UndoPenAttrs(pen.getColor, pen.getThickness, pen.getFillColor, pen.getFontSize))
    pen.setFontSize(n)
  }

  private def realSetFillColor(color: Color, cmd: Command) {
    pushHistory(UndoPenAttrs(pen.getColor, pen.getThickness, pen.getFillColor, pen.getFontSize))
    pen.setFillColor(color)
  }

  private def realSaveStyle(cmd: Command) {
    pushHistory(UndoSaveStyle())
    savedStyles.push(currStyle)
  }

  private def realRestoreStyle(cmd: Command) {
    if (savedStyles.size == 0) {
      throw new IllegalStateException("No saved style to restore")
    }
    val style = savedStyles.pop()
    pushHistory(UndoRestoreStyle(currStyle, style))
    pen.setStyle(style)
  }

  private def beamsOnWorker() {
    if (!areBeamsOn) {
      turtle.addChild(0, xBeam)
      turtle.addChild(1, yBeam)
      turtle.repaint()
      areBeamsOn = true
    }
  }

  private def beamsOffWorker() {
    if (areBeamsOn) {
      turtle.removeChild(xBeam)
      turtle.removeChild(yBeam)
      turtle.repaint()
      areBeamsOn = false
    }
  }

  private def realBeamsOn(cmd: Command) {
    beamsOnWorker()
  }

  private def realBeamsOff(cmd: Command) {
    beamsOffWorker()
  }

  private def realWrite(text: String, cmd: Command) {
    pen.write(text)
//    turtle.repaint()
  }

  private def realHide(cmd: Command) {
    pushHistory(UndoVisibility(isVisible, areBeamsOn))
    hideWorker()
  }

  private def realShow(cmd: Command) {
    pushHistory(UndoVisibility(isVisible, areBeamsOn))
    showWorker()
  }

  private def hideWorker() {
    if (isVisible) {
      turtle.removeChild(turtleImage)
      beamsOffWorker()
      turtle.repaint()
      isVisible = false
    }
  }

  private def showWorker() {
    if (!isVisible) {
      turtle.addChild(turtleImage)
      turtle.repaint()
      isVisible = true
    }
  }

  private def realPlaySound(voice: core.Voice, cmd: Command) {
    import music._
    try {
      Music(voice).play()
    }
    catch {
      case e: Exception => canvas.outputFn("Turtle Error while playing sound:\n" + e.getMessage)
    }
  }

// undo methods are called in the GUI thread via realUndo
  private def undoChangeInPos(oldPos: (Double, Double)) {
    pen.undoMove()
    changePos(oldPos._1, oldPos._2)
    turtle.repaint()
  }

  private def undoChangeInHeading(oldHeading: Double) {
    changeHeading(oldHeading)
    turtle.repaint()
  }

  private def undoPenAttrs(color: Color, thickness: Double, fillColor: Color, fontSize: Int) {
    canvas.outputFn("Undoing Pen attribute (Color/Thickness/FillColor/FontSize) change.\n")
    pen.undoStyle(Style(color, thickness, fillColor, fontSize))
  }

  private def undoPenState(apen: Pen) {
    canvas.outputFn("Undoing Pen State (Up/Down) change.\n")
    apen match {
      case UpPen =>
        pen.undoUpdatePosition()
        pen = UpPen
      case DownPen =>
        pen = DownPen
    }
  }

  private def undoWrite(ptext: PText) {
    layer.removeChild(ptext)
  }

  private def undoVisibility(visible: Boolean, beamsOn: Boolean) {
    if (visible) showWorker()
    else hideWorker()

    if (beamsOn) beamsOnWorker()
    else beamsOffWorker()
  }

  private def undoSaveStyle() {
    savedStyles.pop()
  }

  private def undoRestoreStyle(currStyle: Style, savedStyle: Style) {
    savedStyles.push(savedStyle)
    undoPenAttrs(currStyle.penColor, currStyle.penThickness, currStyle.fillColor, currStyle.fontSize)
  }

  private def resetRotation() {
    changeHeading(Utils.deg2radians(90))
  }

  private [kojo] def stop() {
    cmdBool.set(false)
    cmdBool = new AtomicBoolean(true)
  }

  private [kojo] def setTurtleListener(l: TurtleListener) {
//    if (listener != NoOpListener) throw new RuntimeException("Cannot re-set Turtle listener")
    listener = l
  }

  private def makePens(): (Pen, Pen) = {
    val downPen = new DownPen()
    val upPen = new UpPen()
    (downPen, upPen)
  }

  private def makeCommandProcessor() = actor {

    val throttler = new Throttler(1)

    def processGetCommand(cmd: Command, latch: CountDownLatch)(fn: => Unit) {
      processCommandSync(cmd)(fn)
      latch.countDown()
    }

    def processCommandSync(cmd: Command)(fn: => Unit) {
//      Log.info("Command Being Processed: %s." format(cmd))
      if (cmd.valid.get) {
        throttler.throttle()

        try {
          realWorker2 {
            fn
          }
        }
        finally {
          listener.commandDone(cmd)
        }
      }
      else {
        listener.commandDiscarded(cmd)
      }
//      Log.info("Command Handled: %s. Mailbox size: %d" format(cmd, mailboxSize))
      if (mailboxSize == 0) listener.pendingCommandsDone
    }

    def processCommand(cmd: Command)(fn: => Unit) {
      if (cmd.valid.get) {
        throttler.throttle()

        realWorker4(cmd) {
          fn
        }
      }
      else {
        listener.commandDiscarded(cmd)
      }
    }

    def processCommandCustom(cmd: Command)(fn: => Unit) {
      if (cmd.valid.get) {
        throttler.throttle()

        fn
      }
      else {
        listener.commandDiscarded(cmd)
      }
    }

    def undoHandler: PartialFunction[UndoCommand, Unit] = {
      case cmd @ UndoChangeInPos((x, y)) =>
        undoChangeInPos((x, y))
      case cmd @ UndoChangeInHeading(oldHeading) =>
        undoChangeInHeading(oldHeading)
      case cmd @ UndoPenAttrs(color, thickness, fillColor, fontSize) =>
        undoPenAttrs(color, thickness, fillColor, fontSize)
      case cmd @ UndoPenState(apen) =>
        undoPenState(apen)
      case cmd @ UndoWrite(ptext) =>
        undoWrite(ptext)
      case cmd @ UndoVisibility(visible, areBeamsOn) =>
        undoVisibility(visible, areBeamsOn)
      case cmd @ CompositeUndoCommand(cmds) =>
        handleCompositeCommand(cmds)
      case cmd @ UndoSaveStyle() =>
        undoSaveStyle()
      case cmd @ UndoRestoreStyle(currStyle: Style, savedStyle: Style) =>
        undoRestoreStyle(currStyle, savedStyle)
    }

    def realUndo(undoCmd: Command) {
      if (!history.isEmpty) {
        val cmd = popHistory()
        undoHandler(cmd)
      }
    }

    def handleCompositeCommand(cmds: scala.List[UndoCommand]) {
      cmds.foreach {cmd => undoHandler(cmd)}
    }

    loop {
      react {
        case cmd @ Forward(n, b) =>
          processCommandCustom(cmd) {
            realForwardCustom(n, cmd)
          }
        case cmd @ Turn(angle, b) =>
          processCommand(cmd) {
            realTurn(angle, cmd)
          }
        case cmd @ Clear(b) =>
          processCommandSync(cmd) {
            realClear
          }
        case cmd @ Remove(b) =>
          processCommandSync(cmd) {
            realRemove
          }
          exit()
        case cmd @ PenUp(b) =>
          processCommand(cmd) {
            realPenUp(cmd)
          }
        case cmd @ PenDown(b) =>
          processCommand(cmd) {
            realPenDown(cmd)
          }
        case cmd @ Towards(x, y, b) =>
          processCommand(cmd) {
            realTowards(x, y, cmd)
          }
        case cmd @ JumpTo(x, y, b) =>
          processCommand(cmd) {
            realJumpTo(x, y, cmd)
          }
        case cmd @ MoveTo(x, y, b) =>
          processCommandCustom(cmd) {
            realMoveToCustom(x, y, cmd)
          }
        case cmd @ SetAnimationDelay(d, b) =>
          // block till delay is set to avoid race condition
          // in functions like realForward which look at
          // animation delay in the actor thread before deciding what to do
          processCommandSync(cmd) {
            realSetAnimationDelay(d, cmd)
          }
        case cmd @ GetAnimationDelay(l, b) =>
          processGetCommand(cmd, l) {
            realGetWorker()
          }
        case cmd @ GetPosition(l, b) =>
          processGetCommand(cmd, l) {
            realGetWorker()
          }
        case cmd @ GetHeading(l, b) =>
          processGetCommand(cmd, l) {
            realGetWorker()
          }
        case cmd @ GetStyle(l, b) =>
          processGetCommand(cmd, l) {
            realGetWorker()
          }
        case cmd @ SetPenColor(color, b) =>
          processCommand(cmd) {
            realSetPenColor(color, cmd)
          }
        case cmd @ SetPenThickness(t, b) =>
          processCommand(cmd) {
            realSetPenThickness(t, cmd)
          }
        case cmd @ SetFontSize(n, b) =>
          processCommand(cmd) {
            realSetFontSize(n, cmd)
          }
        case cmd @ SetFillColor(color, b) =>
          processCommand(cmd) {
            realSetFillColor(color, cmd)
          }
        case cmd @ SaveStyle(b) =>
          processCommand(cmd) {
            realSaveStyle(cmd)
          }
        case cmd @ RestoreStyle(b) =>
          processCommand(cmd) {
            realRestoreStyle(cmd)
          }
        case cmd @ BeamsOn(b) =>
          processCommand(cmd) {
            realBeamsOn(cmd)
          }
        case cmd @ BeamsOff(b) =>
          processCommand(cmd) {
            realBeamsOff(cmd)
          }
        case cmd @ Write(text, b) =>
          processCommand(cmd) {
            realWrite(text, cmd)
          }
        case cmd @ Show(b) =>
          processCommand(cmd) {
            realShow(cmd)
          }
        case cmd @ Hide(b) =>
          processCommand(cmd) {
            realHide(cmd)
          }
        case cmd @ PlaySound(score, b) =>
          processCommand(cmd) {
            realPlaySound(score, cmd)
          }
        case cmd @ Undo =>
          processCommand(cmd) {
            realUndo(cmd)
          }
        case cmd @ GetState(l, b) =>
          processGetCommand(cmd, l) {
            realGetWorker()
          }
      }
    }
  }

  abstract class AbstractPen extends Pen {
    val Log = Logger.getLogger(getClass.getName);

    val turtle = Turtle.this
    val CapThick = BasicStroke.CAP_ROUND
    val CapThin = BasicStroke.CAP_BUTT
    val JoinThick = BasicStroke.JOIN_ROUND
    val JoinThin = BasicStroke.JOIN_BEVEL
    val DefaultColor = Color.red
    val DefaultFillColor = null
    val DefaultStroke = new BasicStroke(2, CapThick, JoinThick)
    val DefaultFont = new Font(new PText().getFont.getName, Font.PLAIN, 18)

    def init() {
      lineColor = DefaultColor
      fillColor = DefaultFillColor
      lineStroke = DefaultStroke
      font = DefaultFont
      addNewPath()
    }

    def newPath(): PolyLine = {
      val penPath = new PolyLine()
      penPath.addPoint(turtle._position.x, turtle._position.y)
      penPath.setStroke(lineStroke)
      penPath.setStrokePaint(lineColor)
      penPath.setPaint(fillColor)
      penPath
    }

    protected def addNewPath() {
      val penPath = newPath()
      penPaths += penPath
      layer.addChild(layer.getChildrenCount-1, penPath)
    }

    protected def removeLastPath() {
      val penPath = penPaths.last
      penPaths.remove(penPaths.size-1)
      layer.removeChild(penPath)
    }

    def getColor = lineColor
    def getFillColor = fillColor
    def getThickness = lineStroke.asInstanceOf[BasicStroke].getLineWidth
    def getFontSize = font.getSize

    private def rawSetAttrs(color: Color, thickness: Double, fColor: Color, fontSize: Int) {
      lineColor = color
      val Cap = if (thickness < 1) CapThin else CapThick
      val Join = if (thickness < 1) JoinThin else JoinThick
      lineStroke = new BasicStroke(thickness.toFloat, Cap, Join)
      fillColor = fColor
      font = new Font(new PText().getFont.getName, Font.PLAIN, fontSize)
    }

    def setColor(color: Color) {
      lineColor = color
      addNewPath()
    }

    def setThickness(t: Double) {
      val Cap = if (t < 1) CapThin else CapThick
      val Join = if (t < 1) JoinThin else JoinThick
      lineStroke = new BasicStroke(t.toFloat, Cap, Join)
      addNewPath()
    }

    def setFontSize(n: Int) {
      font = new Font(new PText().getFont.getName, Font.PLAIN, n)
      addNewPath()
    }

    def setFillColor(color: Color) {
      fillColor = color
      addNewPath()
    }

    def setStyle(style: Style) {
      rawSetAttrs(style.penColor, style.penThickness, style.fillColor, style.fontSize)
      addNewPath()
    }

    def undoStyle(oldStyle: Style) {
      rawSetAttrs(oldStyle.penColor, oldStyle.penThickness, oldStyle.fillColor, oldStyle.fontSize)
      removeLastPath()
    }

    def addToLayer() = {
      penPaths.foreach {penPath => layer.addChild(layer.getChildrenCount-1, penPath)}
    }

    def clear() = {
      penPaths.foreach { penPath =>
        penPath.reset()
        layer.removeChild(penPath)
      }
      penPaths.clear()
    }
  }

  class UpPen extends AbstractPen {
    def startMove(x: Double, y: Double) {}
    def move(x: Double, y: Double) {}
    def endMove(x: Double, y: Double) {}
    def updatePosition() {}
    def undoUpdatePosition() {}
    def undoMove() {}
    def write(text: String) {}
  }

  class DownPen extends AbstractPen {
    var tempLine = new PPath
    val lineAnimationColor = Color.orange

    def startMove(x: Double, y: Double) {
      tempLine.setStroke(lineStroke)
      tempLine.setStrokePaint(lineAnimationColor)
      tempLine.moveTo(x.toFloat, y.toFloat)
      layer.addChild(layer.getChildrenCount-1, tempLine)
    }
    def move(x: Double, y: Double) {
      tempLine.lineTo(x.toFloat, y.toFloat)
      tempLine.repaint()
    }
    def endMove(x: Double, y: Double) {
      layer.removeChild(tempLine)
      tempLine.reset
      penPaths.last.lineTo(x, y)
      penPaths.last.repaint()
    }

    def updatePosition() {
      addNewPath()
    }

    def undoUpdatePosition() {
      removeLastPath()
    }

    def undoMove() {
      penPaths.last.removeLastPoint()
      penPaths.last.repaint()
    }

    def write(text: String) {
      val ptext = Utils.textNode(text, _position.x, _position.y)
      pushHistory(UndoWrite(ptext))
      ptext.setFont(font)
      ptext.setTextPaint(pen.getColor)
      layer.addChild(layer.getChildrenCount-1, ptext)
      ptext.repaint()
    }
  }
}