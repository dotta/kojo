// The Math World (Mw) stuff is under development
// The Mw programming interface is likely to change
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

// Make Triangle

// Make first vertex on the first line
val A = Mw.point("A", L1, 4, 3)
A.hideLabel()
// Make other two vertices on the second line
val B = Mw.point("B", L2, 3, 1)
B.hideLabel()
val C = Mw.point("C", L2, 6, 1)
C.hideLabel()

val c = Mw.lineSegment("c",A,B)
c.hideLabel()
val a = Mw.lineSegment("a",B,C)
a.hideLabel()
val b = Mw.lineSegment("b",C,A)
b.hideLabel()

val color1 = color(0, 0, 102)
val color2 = color(153, 0, 0)

val X = Mw.angle("X", B,A,C)
X.showNameInLabel()
val Y = Mw.angle("Y", C,B,A)
Y.setColor(color1)
Y.showNameInLabel()
val Z = Mw.angle("Z", A,C,B)
Z.setColor(color2)
Z.showNameInLabel()

val Yp = Mw.angle("Y'", P1,A,B)
Yp.setColor(color1)
Yp.showNameInLabel()
val Zp = Mw.angle("Z'", C,A,P2)
Zp.setColor(color2)
Zp.showNameInLabel()

val t1 = """
"X is: " + X + "
Y is: " + Y + "
Z is: " + Z + "
The sum of X, Y, and Z is: " + (X+Y+Z)
"""
Mw.text(t1,5,4.5)


val t2 = """
This figure shows a triangle inscribed between two parallel lines.

The sum of the angles X, Y', and Z' is 180 degrees (why?)
Y = Y' (relates to a property of parallel lines cut by a transversal)
Z = Z' (same as above)

Can you see why the sum of the angles of a triangle is 180 degrees?
Play with the figure by moving the vertices of the triangle
"""
Mw.text(t2, 4,0.5)