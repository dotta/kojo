// This story will Auto-Run in a moment (if it is loaded via the Stories Menu)
//
// =================================================================
//
// The source for the story is provided below

val pageStyle = "color:#1e1e1e; margin:15px;"
val centerStyle = "text-align:center;"
val headerStyle = "text-align:center;font-size:110%;color:maroon;"
val codeStyle = "font-size:90%;"

def pgHeader(hdr: String) =
    <p style={headerStyle}>
        {xml.Unparsed(hdr)}
        <hr/>
        <br/>
    </p>

def isScalaTestAvailable = {
    try {
        Class.forName("org.scalatest.FunSuite")
        true
    }
    catch {
        case e: Throwable => false
    }
}

val pgGetSt = Page(
    name = "",
    body = 
        <body style={pageStyle}>
            {pgHeader("Setting up ScalaTest")}
            This story helps you to set up the ScalaTest library within Kojo, to enable
            easy testing of the code that you write.
            <br/>
            <br/>
            You need to do the following to get ScalaTest running within Kojo:
            <ul>
                <li>
                    Download a version of ScalaTest (from <a href="http://www.scalatest.org">www.scalatest.org</a>) that is compatible with the version 
                    of Scala running within Kojo - <em>Scala {scala.tools.nsc.Properties.versionString}</em>.
                </li>
                <li>
                    Put the ScalaTest jar file under a directory called libk within your
                    user directory - <em>{System.getProperty("netbeans.user")}</em>.
                </li>
                <li>
                    Restart Kojo.
                </li>
                <li>
                    Come back here to this Story.
                </li>
            </ul>
        </body>,
    code = {}
)

val sampleTest = """
def sum(n1: Int, n2: Int) = {
    n1 + n2
}

test("sum of positives") {
    sum(1,1) should equal(2)
}

// ignore test for now
ignore("sum of negatives") {
    sum(-1,-1) should equal(-2)
}
"""


val pgStGood = Page(
    name = "",
    body = 
        <body style={pageStyle}>
            {pgHeader("Setting up ScalaTest")}
            You have the ScalaTest library available for use within Kojo. Congratulations!
            <br/>
            <br/>
            You can use all of the functionality of ScalaTest within Kojo.
            <br/>
            <br/>
            Also, Kojo defines some helper functions for ScalaTest - to make it very easy for 
            you to write simple tests within Kojo. These helper functions allow you to write tests 
            like this:
            <pre style={codeStyle}>
                {xml.Unparsed(sampleTest)}
            </pre>
            To play with this code (and run it), copy it into the Script Editor by clicking 
            on the button below.
        </body>,
    code = {
        stAddButton ("Copy Code") {
            stSetScript(sampleTest)
        }
    }
)

val pg = if (isScalaTestAvailable) pgStGood else pgGetSt


stClear()
stPlayStory(Story(pg))
