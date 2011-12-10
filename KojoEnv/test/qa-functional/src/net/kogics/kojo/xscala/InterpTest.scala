/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.kojo
package xscala

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter._

@RunWith(classOf[JUnitRunner])
class InterpTest extends FunSuite with ShouldMatchers {
  
  val jarsDir = KojoTestUtil.nbDirs
  val jars = util.Utils.kojoJars.filter(j => j.contains("scala") || j.contains("kojo"))
  
  def fixture = new {
    val settings = new Settings()
    settings.classpath.append(jars.map {"%s/%s%s" format(jarsDir, _, java.io.File.pathSeparator)}.mkString)
    val interp = new IMain(settings) {
      override protected def parentClassLoader = classOf[InterpTest].getClassLoader
    }
    interp.setContextClassLoader()
    val completer = new JLineCompletion(interp)
  }
  
  test("interpreter reset") {
    val context = fixture
    context.interp.interpret("""def twice(n: Int) = 2*n""")
    
    val result1 = context.interp.interpret("""twice(5)""")
    result1 should be(Results.Success)
    context.interp.unqualifiedIds should contain("twice")
    
    context.interp.reset()
    
    val result2 = context.interp.interpret("""twice(5)""")
    result2 should be(Results.Error)
    context.interp.unqualifiedIds should contain("twice") // should not contain
  }  

  test("interpreter imports") {
    val context = fixture
    
    val code = """
object builtins {
  object Tw {
    def clear() {}
    def undo() {}
  }
    
  object TSCanvas {
    def zoom() {}
  }
}
"""
    
    context.interp.interpret(code)
    context.interp.interpret("""import builtins._""")
    context.interp.interpret("""import TSCanvas._;import Tw._""")
    context.interp.unqualifiedIds should contain("zoom")
  }  

  test("interpreter imports (real)") {
    val context = fixture
    SpriteCanvas.initedInstance(KojoCtx.instance())
    story.StoryTeller.initedInstance(KojoCtx.instance())

    KojoTestUtil.initNetbeansDirs()
    
    val ce = CodeExecutionSupport.initedInstance(new javax.swing.JEditorPane(), new org.openide.awt.UndoRedo.Manager)
    ce.codeRunner.asInstanceOf[core.ProxyCodeRunner].latch.await()
    context.interp.bind("predef", "net.kogics.kojo.xscala.ScalaCodeRunner", ce.codeRunner.asInstanceOf[core.ProxyCodeRunner].codeRunner.asInstanceOf[ScalaCodeRunner])
    context.interp.interpret("val builtins = predef.builtins")
    context.interp.interpret("import builtins._")
    println(context.interp.unqualifiedIds)
    context.interp.interpret("""import TSCanvas._;import Tw._""")
    context.interp.unqualifiedIds should contain("zoom")
  }  
}
