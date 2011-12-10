// This story will Auto-Run in a moment (if it is loaded via the Stories Menu)
//
// =================================================================
//
// The source for the story is provided below

val pageStyle = "background-color:#99CCFF; margin:15px;font-size:small;"
val centerStyle = "text-align:center;"
val headerStyle = "text-align:center;font-size:110%;color:maroon;"
val codeStyle = "font-size:90%;"

def pgHeader(hdr: String) =
    <p style={headerStyle}>
        {xml.Unparsed(hdr)}
        <hr/>
        <br/>
    </p>


val examples = List(
    """
def p(size: Int) = pict { t =>
    import t._
    setAnimationDelay(0)
    repeat (4) {
        forward(size)
        right()
    }
    right(45)
    forward(math.sqrt(2 * size * size))
}

def p1 = p(40)
""",
    """
clear()
invisible()
show(p1)
""",
    """
clear()
invisible()
val pic = HPics(
    p1,
    p1
)
show(pic)
""",
    """
clear()
invisible()
val pic = HPics(
    p1,
    p1,
    p1,
    p1
)
show(pic)
""",
    """
clear()
invisible()
val pic = HPics(
    p1,
    p1,
    VPics(
        p1,
        p1,
        p1
    ),
    p1
)
show(pic)
""",
    """
clear()
invisible()
val pic = col(row(p1, 5), 3)
show(pic)
""",
    """
clear()
invisible()
val pic = HPics(
    p1,
    fillColor(blue) * rot(20) * scale(1.5) * trans(0, 10) -> p1,
    VPics(
        p1,
        p1,
        p1
    ),
    p1
)
show(pic)
""",
    """
val pic = reflect(200) * spin(5) -> HPics(
    p1,
    scale(0.9) -> p1,
    scale(0.8) * rot(30) -> HPics(
        fillColor(green) -> p1,
        fillColor(yellow) -> p1,
        scale(0.7) * rot(30) -> HPics(
            p1,
            p1,
            fillColor(blue) * rot(45) -> p1
        )
    )
)

clear()
show(pic)
""",
    """
def two(fn: => Picture) = HPics(fn, fn)
def four(fn: => Picture) = VPics(two(fn), two(fn))
def checker(p1: => Picture, p2: => Picture) = VPics(
    HPics(p1, p2),
    HPics(p2, p1)
)
def pc(color: Color) = FillColor(color)(p1)

clear()
invisible()
val pic = four(four(checker(pc(blue), pc(black))))
show(pic)
""",
    """
def two(fn: => Picture) = HPics(fn, fn)
def four(fn: => Picture) = VPics(two(fn), two(fn))
def checker(p1: => Picture, p2: => Picture) = VPics(
    HPics(p1, p2),
    HPics(p2, p1)
)
def checkerBoard(size: Int) = four(checker(pc(size, blue), pc(size, black)))
def series(fn: Int => Picture) = HPics(fn(20), fn(40), fn(60), fn(80))
def pc(size: Int, color: Color) = FillColor(color)(p(size))

clear()
invisible()
val pic = series(checkerBoard)
show(pic)
""",
    """
// This is a slightly advanced example
// Note the use of the call-by-name Picture input/argument below
def rowm(p: => Picture, n: Int)(deco: (Picture, Int) => Picture): HPics = {
    val lb = collection.mutable.ListBuffer[Picture]()
    for (i <- 1 to n) {
        lb += deco(p, i) // need to use p.copy if p is not call-by-name
    }
    HPics(lb.toList)
}

clear()
invisible()
val pic2 = rowm(p1, 5) { (p, i) =>
    if (i == 2) FillColor(blue)(p) else p
}
show(pic2)
""",
    """
def growTurnBy(f: Double, a: Double, n: Int, p: => Picture): Picture = {
    if (n==1) {
        rot(a) * scale(f) -> HPics(p)
    }
    else {
        rot(a) * scale(f) -> HPics(p, growTurnBy(f, a, n-1, p))
    }
}

clear()
show(reflect(200) * spin(5) -> growTurnBy(0.8, -14, 12, p1))
""",
    """
clear()
invisible()
val pics = List(p(40), p(50), p(60), p(70))
val pic = VPics(
    HPics(pics),
    HPics(pics.map {p => rot(10) * fillColor(green) -> p.copy})
)
show(pic)
""",
    """
case class Pattern(size: Int)(p: Painter) extends Pic(p) {
    override def copy: Pattern = Pattern(size)(p)
}

def p(size: Int) = Pattern(size) { t =>
    import t._
    setAnimationDelay(0)
    repeat (4) {
        forward(size)
        right()
    }
    right(45)
    forward(math.sqrt(2 * size * size))
}

clear()
invisible()
val pics = List(p(40), p(50), p(60), p(70))
val pic = VPics(
    HPics(pics),
    HPics(pics map {p => rot(10) * fillColor(green) -> p.copy}),
    HPics(pics map {_ copy} filter {_.size > 50}),
    fillColor(blue) -> HPics(pics map {_ copy} find {_.size == 50} get)
)
show(pic)
""",
    """
val pic = reflect(200) * spin(5) -> HPics(
    p1,
    scale(0.9) -> p1,
    scale(0.8) * rot(30) -> HPics(
        fillColor(green) -> p1,
        fillColor(yellow) -> p1,
        scale(0.7) * rot(30) -> HPics(
            p1,
            p1,
            fillColor(blue) * rot(45) -> p1
        )
    )
)

clear()
show(pic)

animate {
    pic.rotate(1)
    pic.translate(-1, -0.5)
    pic.scale(0.999)
}
"""
)

def runLink(n: Int) = "http://runhandler/example/" + n
def code(n: Int) = {
    <div style="background-color:CCFFFF;margin-top:10px"> 
        <hr/>
        <pre><code><a href={runLink(n)} style="text-decoration: none;font-size:x-small;">
                    {xml.Unparsed(examples(n))}</a></code></pre>
        <hr/>
    </div>
}

val pg = Page(
    name = "",
    body =
        <body style={pageStyle}>
            {pgHeader("Pictures in Kojo")}
            <div style="color:2E2E2E;margin:15px">
                This story has many examples that you can run by simply clicking on them; 
                they will be copied to the Kojo Script Editor and run for you. 
                Within the Editor, you can modify the example code and run it again - 
                by clicking on the <em>Run</em> button in the editor toolbar. 
            </div>
            <h4 id="toc0"><span>The Basics</span></h4>
            <p>You start by creating a seed pattern. This can be anything that you can get the turtle to make (Staging shapes will come later). Here's a simple example:</p>
            
            {code(0)}
            
            <p>Now you have a couple of functions to create seed patterns for you (make sure that you click on the code above).</p>
            <p>You draw your pattern on the screen like this:</p>
            {code(1)}
            <p>Let's make a richer picture.</p>
            {code(2)}
            <p>This gives you two instances of your pattern, tiled in the horizontal direction.</p>
            <p><em>Note - your seed patterns (like the ones made by p and p1 above) should normally be drawn to the top and right of the turtle's starting position. If you go below or to the left of this position, you will overlap other patterns in your tiled picture. If you deliberately want this overlap, you are free to draw in any direction.</em></p>
            <p>If you want more patterns in a row, just add them in:</p>
            {code(3)}
            <p>Now you have four of your pattern blocks in a row.</p>
            <p>What if you want the third block in the row to have two more blocks on top of it?</p>
            <p>That's easy, just use VPics:</p>
            {code(4)}
            <p>You can keep putting VPics inside HPics, and HPics inside VPics, for as long as you want!</p>
            <p>You can also conveniently lay out your pictures in terms of rows and columns using the predefined <em>row</em> and <em>col</em> functions</p>
            {code(5)}
            <h4 id="toc1"><span>Transformations</span></h4>
            <p>You can modify the appearance of the pattern blocks in your picture with transformations. You do this by applying a series of transformations to a block. Here's an example:</p>
            {code(6)}
            <p>You can also use function call notation with your transformations:</p>
            <p><tt>FillColor(blue) (Rot (20) (scale(1.5) (trans(0, 10) (p1)))),</tt></p>
            <p>The available transformations are:</p>
            <ul>
                <li>rot</li>
                <li>scale</li>
                <li>trans</li>
                <li>flip</li>
                <li>fillColor</li>
                <li>penColor</li>
                <li>penWidth</li>
            </ul>
            <p>Code completion within Kojo will help you with the inputs/args to the transformations.<br />
                Note - With function call notation, the transformation names begin with capital letters. The examples above should make this clear.</p>
            <h4 id="toc2"><span>Effects</span></h4>
            <p>Effects build upon transformations to provide richer, uhm, effects. Generally, transformations modify a picture, while effects make multiple copies of a picture and put them together in interesting ways.</p>
            <p>The following effects are available:</p>
            <ul>
                <li>spin</li>
                <li>reflect</li>
            </ul>
            <p>Again, code completion within Kojo will help you to use these effects.</p>
            <p>Here's an example:</p>
            {code(7)}
            <p>You can also accomplish something similar using functions, as described at the end of the next section.</p>
            <h4 id="toc3"><span>Using Functions</span></h4>
            <p><em>Parts of this section are inspired by:</em> <a href="http://docs.racket-lang.org/quick">http://docs.racket-lang.org/quick</a></p>
            <p>You just saw how you can lay out your pictures declaratively using HPics and VPics. You can also tap into the power of functions to help with this:</p>
            {code(8)}
            <p>Or</p>
            {code(9)}
            <p>You saw in an earlier section how you can lay out your pictures in terms of rows and columns. But what if you wanna modify one of the pictures in a row?</p>
            <p>No problem. Functions to the rescue again:</p>
            {code(10)}
            <p>And here's an effect similar to the one that you saw in the last section, built using a function (as opposed to being manually laid out using HPics):</p>
            {code(11)}
            <h4 id="toc4"><span>Working with Lists of Pictures</span></h4>
            <p>You can do interesting things with Lists of Pictures:</p>
            {code(12)}
            <p>To be able to use the list methods like filter, find, etc, you need to do some extra work - to get access to fields of interest within your pictures:</p>
            {code(13)}
            <h4><span>Animating Pictures</span></h4>
            <p>Once you have a picture that you have drawn on the screen (via the show command), you can easily animate it:</p>
            {code(14)}
        </body>,
    code = {}
)

val story = Story(pg)
stClear()
stAddLinkHandler("example", story) {idx: Int =>
    stSetScript(examples(idx))
    stClickRunButton()
}
stPlayStory(story)
