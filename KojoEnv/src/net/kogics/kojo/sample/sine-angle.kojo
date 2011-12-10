import Staging._

reset()

// Change this value to make the figure bigger or smaller.
val unit = 100

// Angle values in degrees get converted to radians a lot in
// this program.  Let's pre-calculate them once and for all.
val rads = for (a <- 0 to 360) yield math.toRadians(a)

// We need something that will plot a point around a circle...
object CirclePlotter {
  def apply(a: Int) =
    point(unit * math.cos(rads(a)), unit * math.sin(rads(a)))
}

// ...and along a sine wave.
object WavePlotter {
  def apply(a: Int) =
    point(2 * unit + a, unit * math.sin(rads(a)))
}

// We're going to label a few items in the diagram, and we
// want the labels to appear a little to the right of the
// place where the item is.
val labelOffset = point(4, 0)
// Some labels should also be above the item.
val labelOffsetAbove = point(4, 18)

// Define a constant for a nice dark gray color.
val darkGray = grayColors(255)(64)

// Make the default stroke a size 1 dark gray line.
stroke(darkGray)
strokeWidth(1)

// Now we're going to draw the background figures
// that don't change.

// Draw a circle and a wave with a thick green line.
withStyle(null, green, 3) {
  circle(O, unit)
  0 to 360 foreach { a => dot(WavePlotter(a)) }
}

// Using a thin dark gray line, draw...
withStyle(null, darkGray, 1) {
  // ...the baseline of the wave...
  line((2 * unit, 0), (2 * unit + 360, 0))
  // ...and the base radius of the circle
  val br = line(O, (unit, 0))

  // Label the origin of the base radius "O"
  text("O", br.origin + labelOffset)
  // Label the endpoint of the base radius "A"
  text("A", br.endpoint + labelOffset)
}

// Print an explanation of what we're going to do.
val txt = """
This animation shows how the magnitude of
an angle and its sine are defined, and how
they relate to each other.

The angle of interest is AOP. Its magnitude
is defined (in radians) as the ratio of
lengths - AP/OP.
The sine of this angle is the ratio of
lengths - MP/OP

If you consider OP to be equal to one unit
in length, the sine of AOP is equal to MP.
This magnitude is shown by P' on
the curve to the right of the circle.
"""
text(txt, -450, 120)

// This variable holds the value of the angle in question.
var theta = 0

loop {
  // Erase the figures from the previous iteration.
  wipe

  // Get plotted points for the current angle.
  val p1 = CirclePlotter(theta)
  val p2 = WavePlotter(theta)

  // Draw the plotted points with extra large red dots and label them.
  withStyle(null, red, 10) {
    dot(p1)
    text("P", p1 + labelOffsetAbove)
    dot(p2)
    text("P'", p2 + labelOffsetAbove)
  }

  // Draw the length AP as a thick blue arc.
  withStyle(null, blue, 3) {
    arc(O, (unit, unit), 0, theta)
  }

  // Draw the length MP as a thick red line perpendicular to the x axis.
  withStyle(null, red, 3) {
    line(p1, p1.onX)
    text("M", p1.onX + labelOffsetAbove)

    // Draw the same length on the wave.
    line(p2, p2.onX)
  }

  // Complete the triangle OPM by drawing a medium-sized dark gray line.
  strokeWidth(2)
  line(O, p1.onX)

  // Use a thin dark gray line to draw the angle symbol and the line
  // connecting P and P'.
  strokeWidth(1)
  arc(O, (15, 15), 0, theta)
  line(-unit, p1.y, 2 * unit + 360, p1.y)

  // Show the angle value and the sine of the angle.
  text("Angle AOP = %.2f radians = %d degrees" format(rads(theta), theta), unit + 15, -unit / 2f)
  text("Sine of angle AOP = %.2f" format(math.sin(rads(theta))), 2 * unit, 0)

  // Now for the next angle value!
  theta = if (theta < 360) theta + 1 else 0
}

// reset builtin commands/functions that we clobbered by importing Staging
reimportBuiltins()
