// This story will Auto-Run in a moment (if it is loaded via the Stories Menu)

val S = Staging

val pageStyle = "color:white;background:green;margin:15px;"
var n1 = 1
var d1 = 1
var n2 = 1
var d2 = 1
var plcm = 1

def noInput = {n1 == 1 && d1 == 1 && n2 == 1 && d2 == 1}

def gcd(n1: Int, n2: Int): Int = {
    if (n2 == 0) n1 else gcd(n2, n1 % n2)
}

def lcm(n1: Int, n2: Int) = n1 * n2 / gcd(n1, n2)

val rad = 50

var x = -200
var y = 0

def nextCol() {
    x += rad*2 + 10
}

def nextRow() {
    x = -200
    y -= rad*2 + 40
}

def showFrac(n: Int, d: Int) {
    S.circle(x, y, rad)

    if (n <= d) {
        // proper fraction
        // first show portions not covered by the fraction
        repeati (d-(n+1)) { i =>
            S.arc(x, y, rad, rad, 360.0/d * (n+i), 360.0/d)
        }
        // then show portions covered by the fraction
        S.withStyle (green, blue, 3) {
            repeati (n) { i =>
                S.arc(x, y, rad, rad, 360.0/d * (i-1), 360.0/d)
            }
        }
        S.text("%d/%d" format(n,d), x-10, y-rad-5)
        nextCol()
    }
    else {
        showFrac(d, d)
        showFrac(n-d, d)
    }
}

val pg1 = Page(
    name = "page1",
    body =
        <body style={pageStyle}>
            <p>
                <p style="text-align:center">
                    <strong>Adding Two Fractions</strong>
                </p>
                <br/>
                In the fields below, put in the values of the fractions
                that you want to add.
                <ul>
                    <li><em>n1</em> is the numerator of the first fraction</li>
                    <li><em>d1</em> is the denominator of the first fraction</li>
                    <li><em>n2</em> is the numerator of the second fraction</li>
                    <li><em>d2</em> is the demonimator of the second fraction</li>
                </ul>

                Click on the <strong>Proceed </strong> button after inputing the fractions.
                <br/>
                <br/>
                For mixed fractions, add the whole numbers separately, and input
                just the fractional parts of the two fractions.

            </p>
        </body>,
    code = {
        n1 = 1; d1 = 1; n2 = 1; d2 = 1; plcm = 1
        S.clear()
        stAddField("n1", "")
        stAddField("d1", "")
        stAddField("n2", "")
        stAddField("d2", "")
        stAddButton ("Proceed") {
            n1 = stFieldValue("n1", 1)
            d1 = stFieldValue("d1", 1)
            n2 = stFieldValue("n2", 1)
            d2 = stFieldValue("d2", 1)
            plcm = lcm(d1, d2)
            stNext()
        }
    }
)


def init() {
    S.clear()
    S.setPenColor(new Color(128, 128, 128))
    x = -200
    y = 0
}

def fracs1() {
    showFrac(n1, d1)
    showFrac(n2, d2)
}

def fracs2() {
    showFrac(n1*plcm/d1, plcm)
    showFrac(n2*plcm/d2, plcm)
}

val pg2 = Page(
    name = "",
    body =
        if (noInput)
            <body style={pageStyle}>
            <p>
                Looks like you forgot to click on the Proceed button!
                <br/>
                <br/>
                Go back, input your fractions, and make sure you click the Proceed
                button to move forward.
            </p>
            </body>
    else
        <body style={pageStyle}>
            <p>
                You want to calculate:
                {stFormula("""\frac{%s}{%s} + \frac{%s}{%s}""" format(n1, d1, n2, d2))}
                <br/>
                The turtle canvas shows you a visual representation of the
                fractions that you want to add.
            </p>
        </body>,
    code = {
        init()
        zoom(1, -150, 0)
        fracs1()
    }
)

val pg3 = IncrPage(
    name = "",
    style = pageStyle,
    body = List(
        Para(
            <p>
                {stFormula("""\frac{%s}{%s} + \frac{%s}{%s}""" format(n1, d1, n2, d2))}
                <br/>
                In adding the two given fractions, the first step is to determine
                the LCM of the denominators (<em>d1</em> = {d1} and <em>d2</em> = {d2})
                - so that we can convert the fractions to equivalent fractions with the
                same denominator.
                <br/>
                <br/>
                Calculate the LCM of {d1} and {d2} yourself before moving on.
            </p>
        ),
        Para(
            <p>
                The LCM of {d1} and {d2} is {plcm}
            </p>
        )
    )
)

val pg4 = Page(
    name = "",
    body =
        <body style={pageStyle}>
            <p>
                {stFormula("""\frac{%s}{%s} + \frac{%s}{%s}""" format(n1, d1, n2, d2))}
                <br/>
                Next, we convert the two given fractions to equivalent fractions,
                with the LCM as the common denominator.
                <br/>
                <br/>
                Do this conversion on your own before moving on.
                <br/>
                <br/>
                From the previous page, the LCM of {d1} and {d2} is {plcm}
            </p>
        </body>,
    code = {}
)

val pg5 = Page(
    name = "",
    body =
        <body style={pageStyle}>
            <p>
                The first fraction - {"%s/%s" format(n1, d1)} gets converted to {"%s/%s" format(n1*plcm/d1, plcm)}<br/>
                The second fraction - {"%s/%s" format(n2, d2)} gets converted to {"%s/%s" format(n2*plcm/d2, plcm)}<br/>
                <br/>
                The turtle canvas (second row) shows you a visual representation of these two new
                fractions.
            </p>
        </body>,
    code = {
        init()
        zoom(1, -150, -150)
        fracs1()
        nextRow()
        fracs2()
    }
)

val pg6 = Page(
    name = "",
    body =
        <body style={pageStyle}>
            Now, we just add the numerators of the converted fractions to get the answer.
            {stFormula("""\frac{%s}{%s} + \frac{%s}{%s} = \frac{%s}{%s}"""
                       format(n1*plcm/d1, plcm,
                              n2*plcm/d2, plcm,
                              n1*plcm/d1+n2*plcm/d2, plcm))}
            <br/>
            <br/>
            The turtle canvas (third row) shows you a visual representation of the answer.
            <br/>
            <br/>
            <br/>
            <br/>
            <p style="text-align:center;">
                <a style="color:white;" href="http://localpage/page1">Add more fractions...</a>
            </p>

        </body>,
    code = {
        init()
        zoom(1, -150, -300)
        fracs1()
        nextRow()
        fracs2()
        nextRow()
        showFrac(n1*plcm/d1+n2*plcm/d2, plcm)
    }
)

val st = Story(pg1, pg2, pg3, pg4, pg5, pg6)
stClear()
stPlayStory(st)
