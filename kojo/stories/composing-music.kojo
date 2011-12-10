// This story will Auto-Run in a moment (if it is loaded via the Stories Menu)
//
// =================================================================
//
// The source for the story is provided below

val pageStyle = "color:#1e1e1e; margin:15px;"
val centerStyle = "text-align:center;"
val headerStyle = "text-align:center;font-size:110%;color:maroon;"
val codeStyle = "font-size:90%;"
val smallNoteStyle = "color:gray;font-size:95%;"
val sublistStyle = "margin-left:60px;"

def pgHeader(hdr: String) =
    <p style={headerStyle}>
        {new xml.Unparsed(hdr)}
        <hr/>
    </p>


var pages = new collection.mutable.ListBuffer[StoryPage]

pages += Page(
    name = "",
    body =
        <body style={pageStyle+centerStyle}>
            { for (i <- 1 to 5) yield {
                    <br/>
                }
            }
            <h1>Composing and Playing Music with Kojo </h1>
            <a href="http://localpage/reference">Jump to Reference Section</a>
            { for (i <- 1 to 7) yield {
                    <br/>
                }
            }
            <p style={smallNoteStyle}>
                Please resize this window to about half your screen width, by dragging
                its right border. Also make sure that the Turtle Canvas is nice and visible.
            </p>
        </body>
)

val score1 = """
    MusicScore(
        Melody("Guitar", "C D E F G A B")
    )
"""

val score2 = """
    MusicScore(
        Melody("Acoustic_Grand", "C6q D#6q F6q G6q D#6q F6h Rq D#6q F6q G6q F6q C6q D#6h Rq"),
        Rhythm("Acoustic_Bass_Drum", "q", "o.o.o.o.o.o.o.o.")
    )
"""

pages +=  IncrPage(
    name = "home",
    style=pageStyle,
    body = List(
        Para(
            {pgHeader("Composing Music")}
        ),
        Para(
            <p>
                You compose music within Kojo by defining a <em>score</em>. The
                score can contain multiple melodies (instruments playing tunes)
                and rhythms (percussion beats).
            </p>
        ),
        Para(
            <p>
                Here's a simple example of a score with one melody:
                <pre style={codeStyle}>
                    {xml.Unparsed(score1)}
                </pre>
            </p>
        ),
        Para(
            <p>
                And here's a more complex score with a melody and a rythm:
                <pre style={codeStyle}>
                    {xml.Unparsed(score2)}
                </pre>
            </p>
        ),
        Para(
            <p>
                Some things to note:
                <ul>
                    <li>You specify an instrument for a melody. The Reference section at the end of this story provides a list of available instruments.</li>
                    <li>Each of the notes within the above melody specifies an octave and a duration. The Reference section at the end of this story provides a list of available durations.</li>
                    <li>You specify a percussion instrument for a rhythm. The Reference section at the end of this story provides a list of available percussion instruments.</li>
                    <li>You specify a duration for a rhythm (<em>q</em> in this particular rhythm).</li>
                    <li>The 'o' (more generally, the non '.') character in a Rhythm specifies the timing of the beat for the rhythm.</li>
                </ul>
            </p>
        )

    )
)

val code1 = """
    val score = MusicScore(
        Melody("Acoustic_Grand", "C6q D#6q F6q G6q D#6q F6h Rq D#6q F6q G6q F6q C6q D#6h Rq"),
        Rhythm("Acoustic_Bass_Drum", "q", "o.o.o.o.o.o.o.o.")
    )

    playMusic(score)
"""

pages += IncrPage(
    name = "",
    style=pageStyle,
    body = List(
        Para(
            <p>
                {pgHeader("Playing Music")}
            </p>
        ),
        Para(
            <p>
                Once you have a musical score, you can just play it!
            </p>
        ),
        Para(
            <p>
                Try playing the following (by copying the code into the script
                editor and running it):
                <pre style={codeStyle}>
                    {xml.Unparsed(code1)}
                </pre>
            </p>
        ),
        Para(
            <p>
                Are you starting to see how this works?
            </p>,
            code = {}
        )

    )
)

pages += Page(
    name = "reference",
    body =
        <body style={pageStyle}>
            <p>
                {pgHeader("Reference Section")}
            </p>
            <p>
                <strong>Durations</strong>: <br/>
                w - whole <br/>
                h - half <br/>
                q - quarter <br/>
                i - eighth <br/>
                s - sixteenth <br/>
                t - thirty-second <br/>
                x - sixty-fourth <br/>
                o - one-twenty-eighth <br/>
            </p>
            <p>
                <strong>Some Instruments</strong>: <br/>
                Guitar <br/>
                Pan_Flute <br/>
                Piano <br/>
                Flute <br/>
                Acoustic_Grand <br/>
                Xylophone <br/>
                Accordian <br/>
                Acoustic_Bass <br/>
                Violin <br/>
                Trumpet <br/>
            </p>
            <p>
                <strong>Some Percussion Instruments</strong>: <br/>
                Acoustic_Base_Drum <br/>
                Hand_Clap <br/>
                Bass_Drum <br/>
                Hi_Bongo <br/>
                Low_Bongo <br/>
            </p>
            <p>
                Much more detailed reference information is available in the following
                (JFugue) document: <br/>
                <a href="http://www.jfugue.org/jfugue-chapter2.pdf">Chapter 2 of <em>The Complete Guide to JFugue</em></a>
            </p>
        </body>
)

val story = Story(pages: _*)
stClear()
stPlayStory(story)
