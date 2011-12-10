// This is an early preview of the Mathworld (Mw) module
// This stuff is under development and is likely to change
// Also, code completion for the Mathworld API is pretty rough right now

Mw.clear()
Mw.hideAxes()
// Make first line
val P1 = Mw.point("P1", -10,3)
val P2 = Mw.point("P2", 20,3)
val L1 = Mw.line("L1", P1,P2)

// Make second line, parallel to first (but free/not-dependent in this version)
val P3 = Mw.point("P3", -10,1)
val P4 = Mw.point("P4", 20,1)
val L2 = Mw.line("L2", P3,P4)

// Make transversal
val P5 = Mw.point("P5", 1,0)
val P6 = Mw.point("P6", 4,4)
val L3 = Mw.line("L3", P5,P6)

// Find intersection points of transversal with lines
val P7 = Mw.intersect("P7", L1, L3)
val P8 = Mw.intersect("P8", L2, L3)

// Show Angles that transversal makes with lines
val color1 = color(0, 102, 0)
val color2 = color(153, 0, 0)
val A1 = Mw.angle("A1", P2,P7,P6)
A1.setColor(color1)
val A2 = Mw.angle("A2", P6,P7,P1)
A2.showNameInLabel()
A2.setColor(color2)
val A3 = Mw.angle("A3", P1,P7,P5)
A3.showNameInLabel()
A3.setColor(color1)
val A4 = Mw.angle("A4", P5,P7,P2)
A4.setColor(color2)

val A5 = Mw.angle("A5", P4,P8,P6)
A5.setColor(color1)
val A6 = Mw.angle("A6", P6,P8,P3)
A6.showNameInLabel()
A6.setColor(color2)
val A7 = Mw.angle("A7", P3,P8,P5)
A7.showNameInLabel()
A7.setColor(color1)
val A8 = Mw.angle("A8", P5,P8,P4)
A8.setColor(color2)


// Hide intersection points so angles are clearly visible
P7.hide()
P8.hide()


val t1 = """
"A1 is: " + A1 + "
A4 is: " + A4 + "
The sum of A1 and A4 is: " + (A1 + A4)
"""
Mw.text(t1,5,4.5)

val t3 = """
This figure shows a pair of parallel lines cut by a transversal.
It also shows the angles made between the transveral and the parallel lines.

Equal angles in the figure have the same color.
Any two angles with different colors are supplementary (have a sum of 180 degrees)

Play with the figure by dragging points P5 and P6 to become familiar
with the properties of the angles made between a transversal and a
pair of parallel lines.
"""
Mw.text(t3,4,0.25)
