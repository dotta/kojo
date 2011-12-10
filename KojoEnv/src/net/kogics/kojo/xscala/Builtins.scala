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
package xscala

import core._
import javax.swing.JComponent
import util._

object Builtins extends InitedSingleton[Builtins] {
  def initedInstance(scalaCodeRunner: ScalaCodeRunner) = synchronized {
    instanceInit()
    val ret = instance()
    ret.scalaCodeRunner = scalaCodeRunner
    ret
  }

  protected def newInstance = new Builtins
}

import java.awt.Color

class Builtins extends RepeatCommands {
  @volatile var astStopPhase = "typer"
  @volatile var scalaCodeRunner: ScalaCodeRunner = _
  lazy val tCanvas = scalaCodeRunner.tCanvas
  lazy val ctx = scalaCodeRunner.ctx
  lazy val storyTeller = story.StoryTeller.instance
  lazy val turtle0 = tCanvas.turtle0

  type Turtle = core.Turtle
  type Color = java.awt.Color
  type Point = net.kogics.kojo.core.Point

  PuzzleLoader.init()
  val Random = new java.util.Random

  val blue = Color.blue
  val red = Color.red
  val yellow = Color.yellow
  val green = Color.green
  val orange = Color.orange
  val purple = new Color(0x740f73)
  val pink = Color.pink
  val brown = new Color(0x583a0b)
  val black = Color.black
  val white = Color.white

  val Kc = new staging.KeyCodes
  
  // Turtle World
  class TwC extends TurtleMover {
    def forward() = println("Please provide the distance to move forward - e.g. forward(100)")
    override def forward(n: Double) = turtle0.forward(n)
    UserCommand("forward", List("numSteps"), "Moves the turtle forward a given number of steps.")
    
    def back() = println("Please provide the distance to move back - e.g. back(100)")
    override def back(n: Double) = turtle0.back(n)
    UserCommand("back", List("numSteps"), "Moves the turtle back a given number of steps.")

    override def home(): Unit = turtle0.home()
    UserCommand("home", Nil, "Moves the turtle to its original location, and makes it point north.")

    override def jumpTo(p: Point): Unit = turtle0.jumpTo(p.x, p.y)
    override def jumpTo(x: Double, y: Double) = turtle0.jumpTo(x, y)
    UserCommand.addCompletion("jumpTo", List("x", "y"))

    override def setPosition(p: Point): Unit = turtle0.jumpTo(p)
    override def setPosition(x: Double, y: Double) = turtle0.jumpTo(x, y)
    UserCommand("setPosition", List("x", "y"), "Sends the turtle to the point (x, y) without drawing a line. The turtle's heading is not changed.")

    override def position: Point = turtle0.position
    UserCommand.addSynopsis("position - Queries the turtle's position.")

    def moveTo() = println("Please provide the coordinates of the point that the turtle should move to - e.g. moveTo(100, 100)")
    override def moveTo(x: Double, y: Double) = turtle0.moveTo(x, y)
    override def moveTo(p: Point): Unit = turtle0.moveTo(p.x, p.y)
    UserCommand("moveTo", List("x", "y"), "Turns the turtle towards (x, y) and moves the turtle to that point. ")

    def turn() = println("Please provide the angle to turn in degrees - e.g. turn(45)")
    override def turn(angle: Double) = turtle0.turn(angle)
    UserCommand("turn", List("angle"), "Turns the turtle through a specified angle. Angles are positive for counter-clockwise turns.")

    override def right(): Unit = turtle0.right()
    UserCommand("right", Nil, "Turns the turtle 90 degrees right (clockwise).")
    override def right(angle: Double): Unit = turtle0.right(angle)
    UserCommand("right", List("angle"), "Turns the turtle angle degrees right (clockwise).")

    override def left(): Unit = turtle0.left()
    UserCommand("left", Nil, "Turns the turtle 90 degrees left (counter-clockwise).")
    override def left(angle: Double): Unit = turtle0.left(angle)
    UserCommand("left", List("angle"), "Turns the turtle angle degrees left (counter-clockwise). ")

    def towards() = println("Please provide the coordinates of the point that the turtle should turn towards - e.g. towards(100, 100)")
    override def towards(p: Point): Unit = turtle0.towards(p.x, p.y)
    override def towards(x: Double, y: Double) = turtle0.towards(x, y)
    UserCommand("towards", List("x", "y"), "Turns the turtle towards the point (x, y).")

    override def setHeading(angle: Double) = turtle0.setHeading(angle)
    UserCommand("setHeading", List("angle"), "Sets the turtle's heading to angle (0 is towards the right side of the screen ('east'), 90 is up ('north')).")

    override def heading: Double = turtle0.heading
    UserCommand.addSynopsis("heading - Queries the turtle's heading (0 is towards the right side of the screen ('east'), 90 is up ('north')).")
    UserCommand.addSynopsisSeparator()

    override def penDown() = turtle0.penDown()
    UserCommand("penDown", Nil, "Makes the turtle draw lines as it moves (the default setting). ")

    override def penUp() = turtle0.penUp()
    UserCommand("penUp", Nil, "Makes the turtle not draw lines as it moves.")

    def setPenColor() = println("Please provide the color of the pen that the turtle should draw with - e.g setPenColor(blue)")
    override def setPenColor(color: Color) = turtle0.setPenColor(color)
    UserCommand("setPenColor", List("color"), "Specifies the color of the pen that the turtle draws with.")

    def setFillColor() = println("Please provide the fill color for the areas drawn by the turtle - e.g setFillColor(yellow)")
    override def setFillColor(color: Color) = turtle0.setFillColor(color)
    UserCommand("setFillColor", List("color"), "Specifies the fill color of the figures drawn by the turtle.")
    UserCommand.addSynopsisSeparator()

    def setPenThickness() = println("Please provide the thickness of the pen that the turtle should draw with - e.g setPenThickness(1)")
    override def setPenThickness(t: Double) = turtle0.setPenThickness(t)
    UserCommand("setPenThickness", List("thickness"), "Specifies the width of the pen that the turtle draws with.")

    override def setPenFontSize(n: Int) = turtle0.setPenFontSize(n)
    UserCommand("setPenFontSize", List("n"), "Specifies the font size of the pen that the turtle writes with.")

    override def saveStyle() = turtle0.saveStyle()
    UserCommand.addCompletion("saveStyle", Nil)

    override def restoreStyle() = turtle0.restoreStyle()
    UserCommand.addCompletion("restoreStyle", Nil)

    override def beamsOn() = turtle0.beamsOn()
    UserCommand("beamsOn", Nil, "Shows crossbeams centered on the turtle - to help with solving puzzles.")

    override def beamsOff() = turtle0.beamsOff()
    UserCommand("beamsOff", Nil, "Hides the turtle crossbeams.")

    override def invisible() = turtle0.invisible()
    UserCommand("invisible", Nil, "Hides the turtle.")

    override def visible() = turtle0.visible()
    UserCommand("visible", Nil, "Makes the hidden turtle visible again.")
    UserCommand.addSynopsisSeparator()

    override def write(obj: Any): Unit = turtle0.write(obj)
    override def write(text: String) = turtle0.write(text)
    UserCommand("write", List("obj"), "Makes the turtle write the specified object as a string at its current location.")

    override def setAnimationDelay(d: Long) = turtle0.setAnimationDelay(d)
    UserCommand("setAnimationDelay", List("delay"), "Sets the turtle's speed. The specified delay is the amount of time (in milliseconds) taken by the turtle to move through a distance of one hundred steps.")

    override def animationDelay = turtle0.animationDelay
    UserCommand.addSynopsis("animationDelay - Queries the turtle's delay setting.")
    UserCommand.addSynopsisSeparator()

    override def playSound(voice: Voice) = turtle0.playSound(voice)
    UserCommand("playSound", List("voice"), "Makes the turtle play the specified melody, rhythm, or score.")

    override def undo() = tCanvas.undo()
    UserCommand("undo", Nil, "Undoes the last turtle command.")

    override def clear() = tCanvas.clear()
    UserCommand("clear", Nil, "Clears the screen, and brings the turtle to the center of the window.")

    override def style: Style = turtle0.style
  }
  // Define class and then define value - to get around:
  // Problem finding completions: assertion failed: fatal: <refinement> has non-class owner value Tw after flatten.
  val Tw = new TwC()
  
  // Turtle and Staging Canvas
  class TSCanvasC extends TSCanvasFeatures {
    
    override def zoom(factor: Double, cx: Double, cy: Double) = tCanvas.zoom(factor, cx, cy)
    UserCommand("zoom", List("factor", "cx", "cy"), "Zooms in by the given factor, and positions (cx, cy) at the center of the turtle canvas.")
    UserCommand.addSynopsisSeparator()

    def listPuzzles() = println(PuzzleLoader.listPuzzles)
    UserCommand("listPuzzles", Nil, "Shows the names of the puzzles available in the system.")

    def loadPuzzle(name: String) {
      val oPuzzleFn = PuzzleLoader.readPuzzle(name)
      if (oPuzzleFn.isDefined) {
        val code = oPuzzleFn.get + """

          def go() {
            val pTurtle = newPuzzler(0,0)
            puzzle(pTurtle)
          }
          go()
      """
        scalaCodeRunner.runCode(code)

        val code2 = """
          clearOutput()
          println("Puzzle Description")
        """
        scalaCodeRunner.runCode(code2)
      }
      else {
        println("Puzzle not available: " + name)
      }
    }
    UserCommand("loadPuzzle", List("name"), "Loads the named puzzle.")

    override def clearPuzzlers() = tCanvas.clearPuzzlers()
    UserCommand("clearPuzzlers", Nil, "Clears out the puzzler turtles and the puzzles from the screen.")
    UserCommand.addSynopsisSeparator()

    override def gridOn() = tCanvas.gridOn()
    UserCommand("gridOn", Nil, "Shows a grid on the canvas.")

    override def gridOff() = tCanvas.gridOff()
    UserCommand("gridOff", Nil, "Hides the grid.")

    override def axesOn() = tCanvas.axesOn()
    UserCommand("axesOn", Nil, "Shows the X and Y axes on the canvas.")

    override def axesOff() = tCanvas.axesOff()
    UserCommand("axesOff", Nil, "Hides the X and Y axes.")

    def newTurtle(): Turtle = newTurtle(0, 0)
    override def newTurtle(x: Int, y: Int) = tCanvas.newTurtle(x, y)
    UserCommand("newTurtle", List("x", "y"), "Makes a new turtle located at the point (x, y).")

    UserCommand.addSynopsis("turtle0 - Gives you a handle to the default turtle.")
    UserCommand.addSynopsisSeparator()

    override def exportImage(filePrefix: String) = tCanvas.exportImage(filePrefix)
    override def exportThumbnail(filePrefix: String, height: Int) = tCanvas.exportThumbnail(filePrefix, height)
    override def newPuzzler(x: Int, y: Int) = tCanvas.newPuzzler(x, y)
    override def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double) =
      tCanvas.zoomXY(xfactor, yfactor, cx, cy)
    
    def onKeyPress(fn: Int => Unit) = tCanvas.onKeyPress(fn)
    def onMouseClick(fn: (Double, Double) => Unit) =  tCanvas.onMouseClick(fn)
  }
  val TSCanvas = new TSCanvasC()

  def showScriptInOutput() = ctx.showScriptInOutput()
  UserCommand("showScriptInOutput", Nil, "Enables the display of scripts in the output window when they run.")

  def hideScriptInOutput() = ctx.hideScriptInOutput()
  UserCommand("hideScriptInOutput", Nil, "Stops the display of scripts in the output window.")

  def showVerboseOutput() = ctx.showVerboseOutput()
  UserCommand("showVerboseOutput", Nil, "Enables the display of output from the Scala interpreter. By default, output from the interpreter is shown only for single line scripts.")

  def hideVerboseOutput() = ctx.hideVerboseOutput()
  UserCommand("hideVerboseOutput", Nil, "Stops the display of output from the Scala interpreter.")
  UserCommand.addSynopsisSeparator()

  def retainSingleLineCode() = ctx.retainSingleLineCode()
  UserCommand("retainSingleLineCode", Nil, "Makes Kojo retain a single line of code after running it. By default, single lines of code are cleared after running.")

  def clearSingleLineCode() = ctx.clearSingleLineCode()
  UserCommand("clearSingleLineCode", Nil, "Makes Kojo clear a single line of code after running it. This is the default behavior.")

  def version = println("Scala " + scala.tools.nsc.Properties.versionString)
  UserCommand.addSynopsis("version - Displays the version of Scala being used.")

  def print(obj: Any): Unit = println(obj)
  UserCommand.addCompletion("print", List("obj"))

  def println(obj: Any): Unit = println(if (obj == null) "null" else obj.toString)
  UserCommand.addCompletion("println", List("obj"))
  UserCommand.addSynopsis("println(obj) or print(obj) - Displays the given object as a string in the output window.")

  def readln(prompt: String): String = ctx.readInput(prompt)
  UserCommand("readln", List("promptString"), "Displays the given prompt in the output window and reads a line that the user enters.")

  def readInt(prompt: String): Int = readln(prompt).toInt
  UserCommand("readInt", List("promptString"), "Displays the given prompt in the output window and reads an Integer value that the user enters.")

  def readDouble(prompt: String): Double = readln(prompt).toDouble
  UserCommand("readDouble", List("promptString"), "Displays the given prompt in the output window and reads a Double-precision Real value that the user enters.")

  def random(upperBound: Int) = Random.nextInt(upperBound)
  UserCommand("random", List("upperBound"), "Returns a random Integer between 0 (inclusive) and upperBound (exclusive).")

  def randomDouble(upperBound: Int) = Random.nextDouble * upperBound
  UserCommand("randomDouble", List("upperBound"), "Returns a random Double-precision Real between 0 (inclusive) and upperBound (exclusive).")

  def color(r: Int, g: Int, b: Int) = new Color(r, g, b)
  UserCommand("color", List("red", "green", "blue"), "Creates a new color based on the specified red, green, and blue levels.")
  
  def inspect(obj: AnyRef) = ctx.inspect(obj)
  UserCommand("inspect", List("obj"), "Explores the internal fields of the given object.")

  def setAstStopPhase(phase: String): Unit = astStopPhase = phase
  UserCommand.addSynopsis("astStopPhase - Gets the compiler phase value for AST printing.")
  UserCommand("setAstStopPhase", List("stopBeforePhase"), "Sets the compiler phase value for AST printing.")

  def stClear() {
    storyTeller.clear()
  }
  UserCommand.addSynopsisSeparator()
  UserCommand("stClear", Nil, "Clears the Story Teller Window.")

  type Para = story.Para
  val Para = story.Para
  type Page = story.Page
  val Page = story.Page
  type IncrPage = story.IncrPage
  val IncrPage = story.IncrPage
  type Story = story.Story
  val Story = story.Story
  type StoryPage = story.Viewable
  UserCommand.addCompletion("Story", List("pages"))

  def stPlayStory(st: story.Story) {
    storyTeller.playStory(st)
  }
  UserCommand("stPlayStory", List("story"), "Play the given story.")

  def stFormula(latex: String, size: Int = 18, cssColor: String = null) =
    <div style={"text-align:center;margin:6px;"}>
      {if (cssColor != null) {
          <img src={xml.Unparsed(story.CustomHtmlEditorKit.latexPrefix + latex)}
            style={"color:%s" format(cssColor)}
            height={"%d" format(size)} />
        }
        else {
          <img src={xml.Unparsed(story.CustomHtmlEditorKit.latexPrefix + latex)}
            height={"%d" format(size)} />
        }}
    </div>
  UserCommand("stFormula", List("latex"), "Converts the supplied latex string into html that can be displayed in the Story Teller Window.")

  def playMp3(mp3File: String) {
    storyTeller.play(mp3File)
  }
  UserCommand("playMp3", List("fileName"), "Plays the specified MP3 file.")

  def playMp3InBg(mp3File: String) {
    storyTeller.playInBg(mp3File)
  }
  UserCommand("playMp3InBg", List("fileName"), "Plays the specified MP3 file in the background.")

  def stAddButton(label: String)(fn: => Unit) {
    storyTeller.addButton(label)(fn)
  }
  UserCommand.addCompletion("stAddButton", " (${label}) {\n    ${cursor}\n}")
  UserCommand.addSynopsis("stAddButton(label) {code} - Adds a button with the given label to the Story Teller Window, and runs the supplied code when the button is clicked.")

  def stAddField(label: String, default: Any) {
    storyTeller.addField(label, default)
  }
  UserCommand("stAddField", List("label", "default"), "Adds an input field with the supplied label and default value to the Story Teller Window.")

  implicit val StringRead = util.Read.StringRead
  implicit val DoubleRead = util.Read.DoubleRead
  implicit val IntRead = util.Read.IntRead
  import util.Read
  
  def stFieldValue[T](label: String, default: T)(implicit reader: Read[T]): T = {
    storyTeller.fieldValue(label, default)
  }
  UserCommand("stFieldValue", List("label", "default"), "Gets the value of the specified field.")

  def stShowStatusMsg(msg: String) {
    storyTeller.showStatusMsg(msg)
  }
  UserCommand("stShowStatusMsg", List("msg"), "Shows the specified message in the Story Teller status bar.")

  def stSetScript(code: String) = ctx.setScript(code)
  UserCommand("stSetScript", List("code"), "Copies the supplied code to the script editor.")

  def stRunCode(code: String) = interpret(code)
  UserCommand("stRunCode", List("code"), "Runs the supplied code (without copying it to the script editor).")

  def stClickRunButton() = Utils.runInSwingThread {
    CodeExecutionSupport.instance.runCode()
  }
  UserCommand("stClickRunButton", Nil, "Simulates a click of the run button")

  def stShowStatusError(msg: String) {
    storyTeller.showStatusError(msg)
  }
  UserCommand("stShowStatusError", List("msg"), "Shows the specified error message in the Story Teller status bar.")

  def stNext() = Utils.runInSwingThread {
    storyTeller.nextPage()
  }
  UserCommand("stNext", Nil, "Moves the story to the next page/view.")
  UserCommand.addSynopsisSeparator()
  
  def help() = {
    println("""You can press Ctrl-Space in the script window at any time to see available commands and functions.

Here's a partial list of the available commands:
              """ + UserCommand.synopses)
  }

  type Melody = core.Melody
  val Melody = core.Melody

  type Rhythm = core.Rhythm
  val Rhythm = core.Rhythm

  val MusicScore = core.Score

  def playMusic(voice: Voice, n: Int = 1) {
    music.MusicPlayer.instance().playMusic(voice, n)
  }
  UserCommand("playMusic", List("score"), "Plays the specified melody, rhythm, or score.")

  def playMusicUntilDone(voice: Voice, n: Int = 1) {
    music.MusicPlayer.instance().playMusicUntilDone(voice, n)
  }
  UserCommand("playMusicUntilDone", List("score"), "Plays the specified melody, rhythm, or score, and waits till the music finishes.")

  def textExtent(text: String, fontSize: Int) = {
    val tnode = Utils.textNode(text, 0, 0, fontSize)
    val b = tnode.getBounds
    new Rectangle(new Point(b.x, b.y), new Point(b.x + b.width, b.y + b.height))
  }
  UserCommand("textExtent", List("text", "fontSize"), "Determines the size/extent of the given text fragment for the given font size.")

  def runInBackground(code: => Unit) = Utils.runAsyncMonitored(code)
  UserCommand("runInBackground", List("command"), "Runs the given code in the background, concurrently with other code that follows right after this command.")

  // undocumented
  def color(r: Int, g: Int, b: Int, a: Int) = new Color(r, g, b, a)
  def color(rgbHex: Int) = new Color(rgbHex)
  def clearOutput() = ctx.clearOutput()

  def println(s: String): Unit = {
    // Runs on Actor pool (interpreter) thread
    scalaCodeRunner.kprintln(s + "\n")
    Throttler.throttle()
  }
  
  def interpret(code: String) {
    scalaCodeRunner.runCode(code)
  }
  
  // for debugging only
  def kojoInterp = scalaCodeRunner.kojointerp
  
  def reimportBuiltins() { 
    interpret("import TSCanvas._; import Tw._")
  }
  def reimportDefaults() = reimportBuiltins()
  
  import story.{HandlerHolder, IntHandlerHolder, StringHandlerHolder, VoidHandlerHolder}
  implicit def toIhm(handler: Int => Unit): HandlerHolder[Int] = new IntHandlerHolder(handler)
  implicit def toShm(handler: String => Unit): HandlerHolder[String] = new StringHandlerHolder(handler)
  implicit def toVhm(handler: () => Unit): HandlerHolder[Unit] = new VoidHandlerHolder(handler)

  def stAddLinkHandler[T](name: String, story: Story)(implicit hm: HandlerHolder[T]) {
    storyTeller.addLinkHandler(name, story)(hm)
  }
  
  def stAddUiComponent(c: JComponent) {
    storyTeller.addUiComponent(c)
  }

  private val urlHandler = new story.LinkListener(storyTeller)
  def stGotoUrl(url: String) = Utils.runInSwingThread {
    urlHandler.gotoUrl(new java.net.URL(url))
  }
  
  type Picture = picture.Picture
  type Painter = picture.Painter
  type Pic = picture.Pic
  val Pic = picture.Pic
  type HPics = picture.HPics
  val HPics = picture.HPics
  type VPics = picture.VPics
  val VPics = picture.VPics
  type GPics = picture.GPics
  val GPics = picture.GPics

  type Rot = picture.Rot
  val Rot = picture.Rot
  type Scale = picture.Scale
  val Scale = picture.Scale
  type Trans = picture.Trans
  val Trans = picture.Trans
  type Flip = picture.Flip
  val Flip = picture.Flip
  type Fill = picture.Fill
  val FillColor = picture.Fill
  type Stroke = picture.Stroke
  val PenColor = picture.Stroke
  type StrokeWidth = picture.StrokeWidth
  val PenWidth = picture.StrokeWidth
  val rot = picture.rot _
  val scale = picture.scale _
  val trans = picture.trans _
  val flip = picture.flip
  val fillColor = picture.fill _
  val penColor = picture.stroke _
  val penWidth = picture.strokeWidth _
  val deco = picture.deco _
  
  type Spin = picture.Spin
  val Spin = picture.Spin
  val spin = picture.spin _
  type Reflect = picture.Reflect
  val Reflect = picture.Reflect
  val reflect = picture.reflect _
  val row = picture.row _
  val col = picture.col _

  def pict(painter: Painter) = Pic(painter)
  def show(picture: Picture) = picture.show()
  def animate(fn: => Unit) = staging.API.loop(fn)
}
