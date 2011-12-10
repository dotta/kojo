// A simple clock. Based on the Processing Clock Example.

val S = Staging
// To get code completion for Staging, run the above line of code.
// Then, S followed by a period (S.) will trigger code completion.

val center = 200
val len = 160

S.reset()
S.screenSize(400, 400)
S.background(black)
val grayShade = S.grayColors(255)
S.stroke(grayShade(228))
S.fill(grayShade(80))
S.ellipse(center, center, len, len)

S.loop {
    S.wipe()
    val s = S.map(S.second, 0, 60, 0, S.TWO_PI) - S.HALF_PI
    val m = S.map(S.minute + S.norm(S.second, 0, 60), 0, 60, 0, S.TWO_PI) - S.HALF_PI
    val h = S.map(S.hour + S.norm(S.minute, 0, 60), 0, 24, 0, 2 * S.TWO_PI) - S.HALF_PI

    S.strokeWidth(1)
    S.line(center, center, center + S.cos(-s) * len , center + S.sin(-s) * len)
    S.strokeWidth(2)
    S.line(center, center, center + S.cos(-m) * 0.9 * len, center + S.sin(-m) * 0.9 * len)
    S.strokeWidth(4)
    S.line(center, center, center + S.cos(-h) * 0.7 * len, center + S.sin(-h) * 0.7 * len)

    S.strokeWidth(5)

    for (a <- 0 until 360; if a % 6 == 0) {
        val x = center + ( S.cos(S.radians(a)) * len )
        val y = center + ( S.sin(S.radians(a)) * len )
        S.dot(x, y)
    }
}