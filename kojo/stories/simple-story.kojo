// This story will Auto-Run in a moment (if it is loaded via the Stories Menu)
//
// =================================================================
//
// The source for the story is provided below

val pg1 = Page(
    name = "",
    body =
        <body style="margin:15px;">
            This is a simple example of a story - to help you
            to get started writing stories. Look at the source code
            of the story within the <em>Script Editor</em>, and relate it
            to what you see in the Storyteller window.<br/><br/>
            <strong>This is bold text</strong><br/>
            <em>This is italic text</em><br/>
            <p>
                This is the start of a new para.<br/><br/>
                Look at the <em>code</em> section of this page within the
                <em>Script Editor</em> to see how to
                display a button to the user, and how to write code that runs
                when the button is Clicked. You write text within the body of
                the page to describe the purpose of the button, for example: <br/>
                Press the <em>Make Square</em> button below to make a square.
            </p>
        </body>,
    code = {
        stAddButton ("Make Square") {
            clear()
            repeat (4) {
                forward(100)
                right()
            }
        }
    }
)

val pg2 = Page(
    name = "",
    body =
        <body style="margin:15px;">
            This page shows you how to display a field to the user, in addition
            to displaying a button.<br/>
            The idea is that the user fills out the field, and then Clicks the
            button. Your program then does its thing based on the input provided
            by the user. You write text within the body of the page to describe
            the purpose of the field and the button, for example: <br/>
            Please enter the size of the square that you want to make in the
            <em>Size</em> field below, and then Click the <em>Make Square</em>
            button.
        </body>,
    code = {
        stAddField("Size", 100)
        stAddButton ("Make Square") {
            clear()
            val size = stFieldValue("Size", 100)
            repeat (4) {
                forward(size)
                right()
            }
        }
    }
)

val story = Story(pg1, pg2)

stClear()
stPlayStory(story)