// This story will Auto-Run in a moment (if it is loaded via the Stories Menu)
//
// =================================================================
//
// The source for the story is provided below

val cellStyle = "padding: 5px;"

val colorSample = """
    color(75, 132, 125)     // a medium teal-ish color
    color(0x4b, 0x84, 0x7d) // the same color (values are in hexadecimal notation)
    color(0x4b847d)         // also the same color (concatenating the hex values)
    color(4949117)          // still the same color (back to decimal notation)
"""

val s = Story(
    Page(
        name = "",
        body =
            <body style="padding:10px;color:white;background:green">
                <h1>Basic Turtle commands</h1>
                This page describes a basic set of turtle commands.
                <br/>
                <br/>
                <h2>Turtle Movement</h2>
                <table border="1" style="font-size:95%;">
                    <tbody><tr><td style={cellStyle}> <strong>Method</strong>            </td><td style={cellStyle}> <strong>Corresponding Logo command</strong>   </td><td style={cellStyle}> <strong>Description</strong>                                    </td></tr> <tr><td style={cellStyle}> <tt>forward(steps)</tt>    </td><td style={cellStyle}> <tt>FORWARD n</tt>        </td><td style={cellStyle}> Moves the turtle forward a given number of steps. </td></tr> <tr><td style={cellStyle}> <tt>back(steps)</tt>       </td><td style={cellStyle}> <tt>BACKWARD n</tt>       </td><td style={cellStyle}> Moves the turtle back a given number of steps.    </td></tr> <tr><td style={cellStyle}> <tt>home()</tt>            </td><td style={cellStyle}>                    </td><td style={cellStyle}> Moves the turtle to its original location, and makes it point "north". </td></tr> <tr><td style={cellStyle}> <tt>jumpTo(x, y)</tt>      </td><td style={cellStyle}>                    </td><td style={cellStyle}> Sends the turtle to the point (<i>x</i>, <i>y</i>) without drawing a line. The turtle's heading is not changed. </td></tr> <tr><td style={cellStyle}> <tt>setPosition(x, y)</tt> </td><td style={cellStyle}>                    </td><td style={cellStyle}> Sends the turtle to the point (<i>x</i>, <i>y</i>) without drawing a line. The turtle's heading is not changed. </td></tr> <tr><td style={cellStyle}> <tt>position</tt>          </td><td style={cellStyle}> <tt>POS</tt>              </td><td style={cellStyle}> Queries the turtle's position. </td></tr> <tr><td style={cellStyle}> <tt>moveTo(x, y)</tt>      </td><td style={cellStyle}> <tt>SETPOS [n n]</tt>     </td><td style={cellStyle}> Turns the turtle towards (<i>x</i>, <i>y</i>) and moves the turtle to that point. </td></tr> <tr><td style={cellStyle}> <tt>turn(angle)</tt>       </td><td style={cellStyle}>                    </td><td style={cellStyle}> Turns the turtle through a specified angle. Angles are positive for counter-clockwise turns. </td></tr> <tr><td style={cellStyle}> <tt>right()</tt>           </td><td style={cellStyle}> <tt>RIGHT 90</tt>         </td><td style={cellStyle}> Turns the turtle 90 degrees right (clockwise).      </td></tr> <tr><td style={cellStyle}> <tt>right(angle)</tt>      </td><td style={cellStyle}> <tt>RIGHT n</tt>          </td><td style={cellStyle}> Turns the turtle <i>angle</i> degrees right (clockwise). </td></tr> <tr><td style={cellStyle}> <tt>left()</tt>            </td><td style={cellStyle}> <tt>LEFT 90</tt>          </td><td style={cellStyle}> Turns the turtle 90 degrees left (counter-clockwise).      </td></tr> <tr><td style={cellStyle}> <tt>left(angle)</tt>       </td><td style={cellStyle}> <tt>LEFT n</tt>           </td><td style={cellStyle}> Turns the turtle <i>angle</i> degrees left (counter-clockwise). </td></tr> <tr><td style={cellStyle}> <tt>towards(x, y)</tt>     </td><td style={cellStyle}>                    </td><td style={cellStyle}> Turns the turtle towards the point (<i>x</i>, <i>y</i>). </td></tr> <tr><td style={cellStyle}> <tt>setHeading(angle)</tt> </td><td style={cellStyle}> <tt>SETHEADING n</tt>     </td><td style={cellStyle}> Turns the turtle to <i>angle</i> (0 is towards the right side of the screen ("east"), 90 is up ("north")). </td></tr> <tr><td style={cellStyle}> <tt>heading</tt>           </td><td style={cellStyle}>                    </td><td style={cellStyle}> Queries the turtle's heading (0 is towards the right side of the screen ("east"), 90 is up ("north")). </td></tr> </tbody>
                </table>
                <br/>
                <h2>Pen Control</h2>
                <table border="1" style="font-size:95%;">
                    <tbody><tr><td style={cellStyle}> <strong>Method</strong>              </td><td style={cellStyle}> <strong>Corresponding Logo command</strong>      </td><td style={cellStyle}> <strong>Description</strong>                                    </td></tr> <tr><td style={cellStyle}> <tt>penDown()</tt>           </td><td style={cellStyle}> <tt>PENDOWN</tt>             </td><td style={cellStyle}> Makes the turtle draw lines as it moves (the default setting). </td></tr> <tr><td style={cellStyle}> <tt>penUp()</tt>             </td><td style={cellStyle}> <tt>PENUP</tt>               </td><td style={cellStyle}> Makes the turtle <i>not</i> draw lines as it moves. </td></tr> <tr><td style={cellStyle}> <tt>setPenColor(color)</tt>  </td><td style={cellStyle}> <tt>SETPENCOLOR [n n n]</tt> </td><td style={cellStyle}> Specifies the color of the pen that the turtle draws with. </td></tr> <tr><td style={cellStyle}> <tt>setFillColor(color)</tt> </td><td style={cellStyle}>                       </td><td style={cellStyle}> Specifies the fill color of the figures drawn by the turtle. </td></tr> <tr><td style={cellStyle}> <tt>setPenThickness(n)</tt>  </td><td style={cellStyle}>                       </td><td style={cellStyle}> Specifies the width of the pen that the turtle draws with. </td></tr> <tr><td style={cellStyle}> <tt>saveStyle()</tt>         </td><td style={cellStyle}>                       </td><td style={cellStyle}> Makes Kojo remember the current style. </td></tr> <tr><td style={cellStyle}> <tt>restoreStyle()</tt>      </td><td style={cellStyle}>                       </td><td style={cellStyle}> Brings back a saved style. </td></tr> </tbody>
                </table>
                <br/>
                <h2>Colors</h2>
                <div style="font-size:95%;">
                <p>The <tt>setPenColor</tt> and <tt>setFillColor</tt> commands expect a color value as argument.  The following colors are pre-defined: <tt>blue</tt>, <tt>red</tt>, <tt>yellow</tt>, <tt>green</tt>, <tt>orange</tt>, <tt>purple</tt>, <tt>pink</tt>, <tt>brown</tt>, <tt>black</tt>, and <tt>white</tt>.  To get other colors, use either the utility functions <tt>color(r, g, b)</tt> or <tt>color(value)</tt>.  The former takes three integers representing the amounts of red, green, and blue in the desired color, and the latter takes a single integer that is a combination of these components.  Example: </p>
                <pre>{xml.Unparsed(colorSample)}
                </pre>
                </div>
                <br/>
                <h2>Misc</h2>
                <table border="1" style="font-size:95%;">
                    <tbody><tr><td style={cellStyle}> <strong>Method</strong>              </td><td style={cellStyle}> <strong>Corresponding Logo command</strong>      </td><td style={cellStyle}> <strong>Description</strong>                                    </td></tr> <tr><td style={cellStyle}> <tt>beamsOn()</tt>           </td><td style={cellStyle}>                       </td><td style={cellStyle}> Shows crossbeams centered on the turtle - to help with solving puzzles. </td></tr> <tr><td style={cellStyle}> <tt>beamsOff()</tt>          </td><td style={cellStyle}>                       </td><td style={cellStyle}> Hides crossbeams. </td></tr> <tr><td style={cellStyle}> <tt>invisible()</tt>         </td><td style={cellStyle}> <tt>HIDETURTLE</tt>          </td><td style={cellStyle}> Hides the turtle. </td></tr> <tr><td style={cellStyle}> <tt>visible()</tt>           </td><td style={cellStyle}> <tt>SHOWTURTLE</tt>          </td><td style={cellStyle}> Makes the turtle visible again. </td></tr> <tr><td style={cellStyle}> <tt>write(text)</tt>         </td><td style={cellStyle}>                       </td><td style={cellStyle}> Makes the turtle write the specified object as a string at its current location. </td></tr> <tr><td style={cellStyle}> <tt>setAnimationDelay(d)</tt></td><td style={cellStyle}>                       </td><td style={cellStyle}> Sets the turtle's speed. The specified delay <i>d</i> is the amount of time (in milliseconds) taken by the turtle to move through a distance of one hundred steps. </td></tr> <tr><td style={cellStyle}> <tt>animationDelay</tt>      </td><td style={cellStyle}>                       </td><td style={cellStyle}> Queries the turtle's delay setting. </td></tr> </tbody>
                </table>
            </body>
    )
)

stClear()
stPlayStory(s)