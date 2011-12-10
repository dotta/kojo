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
package sample

import java.io._

object SampleCode {

  def get(e: java.awt.event.ActionEvent): String = {
    e.getActionCommand match {
      // TODO: need to read the case strings from Bundle.properties
      case "Square" => Square
      case "Circle" => Circle
      case "Turtle Mania" => TurtleMania
      case "Turning Squares" => TurningSquares
      case "Another Square Pattern" => DecmoSquares
      case "Inward Eyes" => InwardEyes
      case "Orange Flower" => Flower1
      case "Green Flower" => Flower2
      case "Red Fan" => Fan
      case "Ferris Wheel" => FerrisWheel
      case "Plant" => Plant
      case "Sun, Fence, and Flower" => SunFenceFlower
      case "Rangoli" => Rangoli
      case "Snowflake" => Snowflake
      case "Tree" => Tree
      case "Dragon" => Dragon
      case "Parallel/Transversal" => ParTrans
      case "Angles of a Triangle" => TriangleAngles
      case "Inspect Object" => InspectObject
      case "Input/Output" => InputOutput
      case "Sine of an Angle" => SineAngle
      case "Clock" => Clock
      case "Difference of two Squares" => DiffSquares
    }
  }

  val Square = """
clear()
repeat(4) {
    forward(100)
    right()
}
"""

  val Circle = """
clear()
setAnimationDelay(10)
setPenThickness(2)
setPenColor(green)
setFillColor(orange)
repeat(360) {
    forward(1)
    turn(1)
}
"""

  val TurtleMania = """
clear()

def runPattern() {

    import collection.mutable.ArrayBuffer
    import java.util.Random

    def pattern(turtle: Turtle, n: Int): Unit = {
        if (n < 2) return
        turtle.forward(n)
        turtle.right()
        turtle.forward(n)
        turtle.right()
        pattern(turtle, n-5)
    }

    turn(60)
    setAnimationDelay(2000)
    forward(400)

    val turtles = new ArrayBuffer[Turtle]

    val rand = new Random

    for (i <- 0 until 5) {
        for (j <- 0 until 5) {
            val turtle = newTurtle(-400 + j*200, 400 - i*200)
            turtles += turtle
            turtle.setAnimationDelay(500 + rand.nextInt(500))
            turtle.left
            pattern(turtle, 100-5*i)
        }
    }
}

// run the function that we just defined
runPattern()
"""

  val TurningSquares = """
def squareTurn(n:Int){
    repeat(4) {
        forward(n)
        right()
    }
    turn(10)
}

def pattern(n: Int) {
    repeat(36) {
        squareTurn(n)
    }
}

clear()
setAnimationDelay(50)
setPenColor(blue)
pattern(100)
"""

  val DecmoSquares = """
def pattern(n:Int) {
    if (n <= 2) return
    forward(n+100)
    right()
    forward(n)
    right()
    forward(n)
    right()
    pattern(n-2)
}

clear()
setAnimationDelay(100)
pattern(50)
"""

  val InwardEyes = """
def drawCircle(col:Color, step:Double, angle:Int){
    setFillColor(col)
    repeat(360) {
        forward(step)
        turn(angle)
    }
}

clear()
setAnimationDelay(5)
setPenColor(black)

drawCircle(orange, 3, 1)
drawCircle(orange, 3, -1)
drawCircle(blue, 2, 1)
drawCircle(blue, 2, -1)
drawCircle(yellow, 1, 1)
drawCircle(yellow, 1, -1)
drawCircle(green, .50, 1)
drawCircle(green, .50, -1)
drawCircle(red, .25, 1)
drawCircle(red, .25, -1)
"""

  val Flower1 = """
clear()
jumpTo(0,100)
setAnimationDelay(10)
setPenColor(black)
setFillColor(orange)
repeat(4){
    right()
    repeat(90){
        turn(-2)
        forward(3)
    }
}
"""

  val Flower2 = """
clear()
jumpTo(0,100)
setAnimationDelay(20)
setPenColor(black)
setFillColor(green)
repeat(6){
    turn(-120)
    repeat(90){
        turn(-2)
        forward(3)
    }
}
"""

  val Fan = """
clear()
setFillColor(red)
repeat(4){
    turn(-30)
    forward(100)
    repeat(2){
        turn(-120)
        forward(100)
    }
}
"""

  val FerrisWheel = """
def flag(t: Turtle, c: Color, a: Double){
    t.setPenColor(c)
    t.setFillColor(c)
    t.turn(a)
    t.forward(150)
    repeat(3){
        t.right()
        t.forward(50)
    }
    t.left()
    t.forward(100)
}

clear()

val t1 = newTurtle(0,0)
val t2 = newTurtle(0,0)
val t3 = newTurtle(0,0)
val t4 = newTurtle(0,0)
val t5 = newTurtle(0,0)
val t6 = newTurtle(0,0)
val t7 = newTurtle(0,0)
val t8 = newTurtle(0,0)
val t9 = newTurtle(0,0)
val t10 = newTurtle(0,0)
val t11 = newTurtle(0,0)

flag(turtle0, red,0)
flag(t1, yellow,30)
flag(t2, blue,60)
flag(t3, green,90)
flag(t4, orange,120)
flag(t5, purple,150)
flag(t6, red,180)
flag(t7, yellow,210)
flag(t8, blue,240)
flag(t9, green,270)
flag(t10, orange,300)
flag(t11, purple,330)
"""

  val Plant = """
clear()
setAnimationDelay(20)
setPenThickness(4)

// Flower
setFillColor(red)
repeat(4){
    turn(90)
    repeat(45){
        turn(-4)
        forward(4)}
}

// Upper part of stem
turn(135)
setFillColor(null)
setPenColor(green)
repeat(30){
    turn(1)
    forward(10)
}

// First leaf
setFillColor(green)
repeat(2){
    turn(120)
    repeat(30){
        turn(2)
        forward(6)
    }
}

// Second leaf
repeat(2){
    turn(-120)
    repeat(30){
        turn(-2)
        forward(6)
    }
}

// Lower part of stem
setFillColor(null)
setPenColor(green)
repeat(20){
    turn(1)
    forward(10)
}

// Pot
setPenColor(brown)
setFillColor(brown)
turn(85)
forward(75)
turn(-110)
forward(150)
turn(-70)
forward(50)
turn(-70)
forward(150)
turn(-110)
forward(75)
"""
  val SunFenceFlower = """
clear()

// Sun
jumpTo(-400,200)
setAnimationDelay(20)
setPenColor(yellow)
setFillColor(yellow)
repeat(18) {
    right()
    forward(75)
    turn(180)
    forward(75)
    right()
    repeat(10) {
        turn(2)
        forward(2)}
}

// Flower Pot
jumpTo(200,100)
setPenColor(red)
setPenThickness(4)

// Flower
setFillColor(red)
repeat(4){
    turn(90)
    repeat(45){
        turn(-4)
        forward(1)}
}

// Upper part of stem
turn(135)
setFillColor(null)
setPenColor(green)
repeat(30){
    turn(1)
    forward(3)
}

// First leaf
setFillColor(green)
repeat(2){
    turn(120)
    repeat(30){
        turn(2)
        forward(2)
    }
}

// Second leaf
repeat(2){
    turn(-120)
    repeat(30){
        turn(-2)
        forward(2)
    }
}

// Lower part of stem
setFillColor(null)
setPenColor(green)
repeat(20){
    turn(1)
    forward(3)
}

// Pot
setPenColor(brown)
setFillColor(brown)
turn(85)
forward(25)
turn(-110)
forward(50)
turn(-70)
forward(15)
turn(-70)
forward(50)
turn(-110)
forward(25)

// Fence
def post(t: Turtle){
    t.setPenColor(black)
    t.setFillColor(white)
    t.setPenThickness(4)
    t.forward(150)
    t.turn(-30)
    t.forward(25)
    t.turn(-120)
    t.forward(25)
    t.turn(-30)
    t.forward(150)
    t.right()
    t.forward(25)
}

val t0= newTurtle (0,-100)
val t1 = newTurtle(100,-100)
val t2 = newTurtle(200,-100)
val t3 = newTurtle(300,-100)
val t4 = newTurtle(400,-100)
val t5 = newTurtle(500,-100)
val t6 = newTurtle(600,-100)
val t7 = newTurtle(-100,-100)
val t8 = newTurtle(-200,-100)
val t9 = newTurtle(-300,-100)
val t10 = newTurtle(-400,-100)
val t11 = newTurtle(-500,-100)
val t12 = newTurtle(-550,-70)
val t13 = newTurtle(-550,-10)

post(t0)
post(t1)
post(t2)
post(t3)
post(t4)
post(t5)
post(t6)
post(t7)
post(t8)
post(t9)
post(t10)
post(t11)

def rail(tt: Turtle)
{
    tt.setPenColor(black)
    tt.setFillColor(white)
    tt.setPenThickness(4)
    tt.right()
    tt.forward(1200)
    tt.turn(90)
    tt.forward(25)
    tt.turn(90)
    tt.forward(1200)
    tt.turn(90)
    tt.forward(25)
}

rail(t12)
rail(t13)
"""

  val Rangoli = """
def border(t: Turtle, a: Double) {
    t.setAnimationDelay(200)
    t.setPenColor(black)
    t.right()
    t.forward(1200)
    repeat(15){
        t.setFillColor(red)
        t.turn(a)
        t.forward(40)
        t.turn(a)
        t.forward(40)
        t.turn(a)

        t.setFillColor(blue)
        t.turn(a)
        t.forward(40)
        t.turn(a)
        t.forward(40)
        t.turn(a)
    }
}

def flower(tt:Turtle, c:Color) {
    tt.setAnimationDelay(400)
    tt.setPenColor(black)
    tt.setFillColor(c)
    repeat(4){
        tt.right()
        repeat(90){
            tt.turn(-2)
            tt.forward(2)
        }
    }
}

clear()

val t1=newTurtle(-600,-150)
val t2=newTurtle(-600, 150)

border(t1,120)
border(t2,-120)


jumpTo(-50,100)
setAnimationDelay(20)
setPenColor(black)
setFillColor(green)
repeat(6){
    turn(-120)
    repeat(90){
        turn(-2)
        forward(2)
    }
}

val t3=newTurtle(-300,100)
val t4=newTurtle(-400,0)
val t5=newTurtle(-500,100)
val t6=newTurtle(-600,0)

val t7=newTurtle(200,100)
val t8=newTurtle(300,0)
val t9=newTurtle(400,100)
val t10=newTurtle(500,0)

flower(t3, orange)
flower(t4, yellow)
flower(t5, red)
flower(t6, purple)

flower(t7, orange)
flower(t8, yellow)
flower(t9, red)
flower(t10, purple)

turtle0.invisible()
t1.invisible()
t2.invisible()
t3.invisible()
t4.invisible()
t5.invisible()
t6.invisible()
t7.invisible()
t8.invisible()
t9.invisible()
t10.invisible()
"""

  val Snowflake = """
def lines(count: Int, length: Int) {
    if (count == 1) forward(length)
    else {
        lines(count-1, length)
        left(60)
        lines(count-1, length)
        right(120)
        lines(count-1, length)
        left(60)
        lines(count-1, length)
    }
}

def koch(count: Int, length: Int) {
    right(30)
    lines(count, length)
    right(120)
    lines(count, length)
    right(120)
    lines(count, length)
}

clear()
invisible()
setPenThickness(1)
setPenColor(color(128, 128, 128))
setFillColor(color(0xC9C0BB))
setAnimationDelay(10)
penUp()
back(100)
left()
forward(150)
right()
penDown()
koch(5, 5)
  """

  val Tree = """
def tree(distance: Double) {
    if (distance > 4) {
        setPenThickness(distance/7)
        setPenColor(color(distance.toInt, math.abs(255-distance*3).toInt, 125))
        forward(distance)
        right(25)
        tree(distance*0.8-2)
        left(45)
        tree(distance-10)
        right(20)
        back(distance)
    }
}

clear()
invisible()
setAnimationDelay(10)
penUp()
back(200)
penDown()
tree(90)
  """

  val Dragon = """
// Example contributed by Łukasz Lew

def dragon (depth : Int, angle : Double) : Unit = {
    if (depth == 0) {
        forward (10)
        return;
    }

    turn (angle)
    dragon (depth-1, angle.abs)
    turn (-angle)

    turn (-angle)
    dragon (depth-1, -angle.abs)
    turn (angle)
}

clear()
setAnimationDelay(0)
setPenThickness (7)
setPenColor(color(0x365348))

dragon (10, 45)
"""

  val InspectObject = """
class BaseData {
    val baseList = List(1,2,3)
}

class Data extends BaseData {
    val someInt = 9
    val someDouble = 2.3
    val array = Array(1,2,3,4)
    val arrayList = new java.util.ArrayList[String]()
    arrayList.add("a"); arrayList.add("b"); arrayList.add("c")
}

val data = new Data()
inspect(data)
  """

  val InputOutput = """
clearOutput()
val n = readInt("How many numbers do you want to average?")
var sum = 0.0
repeat (n) {
    val num = readDouble("Number")
    sum += num
}
println("The average is: " + sum/n)
"""

  val Clock = util.Utils.readFile(getResource("clock.kojo"))
  val SineAngle = util.Utils.readFile(getResource("sine-angle.kojo"))
  val DiffSquares = util.Utils.readFile(getResource("diff-squares.kojo"))
  val ParTrans = util.Utils.readFile(getResource("par-trans.kojo"))
  val TriangleAngles = util.Utils.readFile(getResource("triangle-angles.kojo"))

  def getResource(name: String): InputStream = {
    getClass.getResourceAsStream(name)
  }
}
