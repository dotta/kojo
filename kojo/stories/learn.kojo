// This story will Auto-Run in a moment (if it is loaded via the Stories Menu)
//
// =================================================================
//
// The source for the story is provided below

val pg1 = Page(
    name = "",
    body =
        <body style="margin:15px;">
            The following links will get you started learning how to
            use Kojo, and how to do interesting things with Kojo: <br/>

            <div style="margin:15px">
                <a href="http://www.kogics.net/sf:kojo-ebooks">Kojo Ebooks</a> <br/>
                <a href="http://wiki.kogics.net/sf:kojo-docs/">Kojo Documentation</a> <br/>
            </div>

            <div style="color:gray;font-size:95%;">
                Future versions of Kojo will include tutorial stories right
                within Kojo.
            </div>
        </body>
)

val story = Story(pg1)

stClear()
stPlayStory(story)