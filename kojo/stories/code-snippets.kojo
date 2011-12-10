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
// Just write the instructions one line after the other
clear()
forward(100)
right()
forward(100)
// etc
""",
    """
clear()
repeat (4) {
    // your code here
    // for example:
    forward(100)
    right()
}
""",
    """
clear()
for (i <- 1 to 4) {
    // your code here
    // the counter i is available to be used in your code;
    // it ranges from 1 to 4 in this example (as specified above)
    // for example:
    forward(100 - 10*i)
    right(90 + 5*i)
}
""",
    """
// definition of a command named square
def square(side: Int) {
    repeat(4) {
        forward(side)
        right()
    }
}
clear()
// two different calls to square command
square(100)
square(200)
""",
    """
// definition of a function named sum
def sum(n1: Int, n2: Int) = {
    n1 + n2
}
clearOutput()
// call to the sum function within print command
print(sum(3, 5))
// another call to the sum function
print(sum(20, 7))
""",
    """
// definition of a function named diagonal
def diagonal(x: Double) = {
    math.sqrt(2*x*x)
}
clearOutput()
// call to the diagonal function within print command
print(diagonal(100))

// another call to the diagonal function
print(diagonal(20))
""",
    """
clear()    
val size = 50 
if (size > 100) {
    setFillColor(blue)
}
else {
    setFillColor(green)
}
square(100)
""",
    """
val n = 100
val big = if (n > 50) true else false
clearOutput()
println(big)
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
            {pgHeader("Code Snippets")}

            <div style="color:2E2E2E;margin:15px">
                This story has many examples that you can run by simply clicking on them; 
                they will be copied to the Kojo Script Editor and run for you. 
                Within the Editor, you can modify the example code and run it again - 
                by clicking on the <em>Run</em> button in the editor toolbar. 
            </div>
            
            <p>This page contains snippets of code demonstrating ideas that are commonly used in Kojo programs. You can refer to this page as needed - while writing Kojo code.</p>
            <p>Things to remember:</p>
            <ul>
                <li>anything after // on a line is a comment</li>
                <li>You put a block of code between {{ and }}</li>
            </ul>
            <h3 id="toc0"><span>Sequence</span></h3>
            {code(0)}
            <h3 id="toc1"><span>Repetition</span></h3>
            <p>repeat:</p>
            {code(1)}
            <p>for:</p>
            {code(2)}
            <h3 id="toc2"><span>User defined Commands</span></h3>
            {code(3)}
            <h3 id="toc3"><span>User Defined Calculations (Functions)</span></h3>
            {code(4)}
            <p>Or:</p>
            {code(5)}
            <h3 id="toc4"><span>Selection</span></h3>
            <p>With commands:</p>
            {code(6)}
            <p>With expressions:</p>
            {code(7)}
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
