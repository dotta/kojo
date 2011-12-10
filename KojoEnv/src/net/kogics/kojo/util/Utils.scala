/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.util

import java.awt.{List => _, _}
import javax.swing._
import java.awt.event.{ActionListener, ActionEvent}
import java.io._
import scala.{math => Math}

object Utils {
  
  def loadImage(fname: String) : Image = {
    val url = getClass.getResource(fname)
    Toolkit.getDefaultToolkit.getImage(url)
  }

  def loadIcon(fname: String, desc: String = "") : ImageIcon = {
    new ImageIcon(loadImage(fname), desc)
  }

  def inSwingThread = EventQueue.isDispatchThread

  def runAsync(fn: => Unit) {
    new Thread(new Runnable {
        def run {
          fn
        }
      }).start
  }
  
  import collection.mutable.{HashSet, SynchronizedSet}
  val threads = new HashSet[Thread] with SynchronizedSet[Thread]
  
  def runAsyncMonitored(fn: => Unit) {
    lazy val t: Thread = new Thread(new Runnable {
        def run {
          try {
            fn
          }
          catch {
            case e: InterruptedException => // println("Background Thread Interrupted.")
            case t: Throwable => reportException(t)
          }
          finally {
            threads.remove(t)
          }
        }
      })
    threads.add(t)
    t.start()
  }
  
  def stopMonitoredThreads() {
    threads.foreach {t => t.interrupt()}
    threads.clear()
  }

  def runInSwingThread(fn: => Unit) {
    if(inSwingThread) {
      fn
    }
    else {
      javax.swing.SwingUtilities.invokeLater(new Runnable {
          override def run {
            fn
          }
        })
    }
  }

  def runInSwingThreadAndWait[T](fn: => T): T = {
    if(inSwingThread) {
      fn
    }
    else {
      var t: T = null.asInstanceOf[T]
      javax.swing.SwingUtilities.invokeAndWait(new Runnable {
          override def run {
            t = fn
          }
        })
      t
    }
  }

  def doublesEqual(d1: Double, d2: Double, tol: Double): Boolean = {
    if (d1 == d2) return true
    else if (Math.abs(d1 - d2) < tol) return true
    else return false
  }

  def schedule(secs: Double)(f: => Unit): Timer = {
    lazy val t: Timer = new Timer((secs * 1000).toInt, new ActionListener {
        def actionPerformed(e: ActionEvent) {
          t.stop
          f
        }
      })
    t.start
    t
  }

  def scheduleRec(secs: Double)(f: => Unit): Timer = {
    val t: Timer = new Timer((secs * 1000).toInt, new ActionListener {
        def actionPerformed(e: ActionEvent) {
          f
        }
      })
    t.start
    t
  }

  def replAssertEquals(a: Any, b: Any) {
    if (a != b) println("Not Good. First: %s, Second: %s" format (a.toString, b.toString))
    else println("Good")
  }

  // actually - the dir with the jars, one level under the actual install dir
  def installDir = {
    val dirs = System.getProperty("netbeans.dirs")
    dirs.split(File.pathSeparator).find {n =>
      new File(n, "modules/ext/scala-library.jar").exists
    }.getOrElse {throw new IllegalStateException("Unknown Install Dir")}
  }
  
  val kojoJars = List("modules/ext/scala-library.jar",
                      "modules/ext/scala-compiler.jar",
                      "modules/ext/scala-swing.jar",
                      "modules/ext/group-panel.jar",
                      "modules/net-kogics-kojo.jar",
                      "modules/ext/piccolo2d-core-1.3.1.jar",
                      "modules/ext/piccolo2d-extras-1.3.1.jar",
                      "modules/ext/geogebra_main.jar",
                      "modules/ext/geogebra_gui.jar",
                      "modules/ext/geogebra_cas.jar",
                      "modules/ext/geogebra.jar",
                      "modules/ext/jfugue-4.1.jar"
  )
  
  def readFile(is: InputStream): String = {
    val reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))
    val buf = new Array[Char](1024)
    var nbytes = reader.read(buf)
    val sb = new StringBuffer
    while (nbytes != -1) {
      sb.append(buf, 0, nbytes)
      nbytes = reader.read(buf)
    }
    reader.close()
    sb.toString
  }

  def stackTraceAsString(t: Throwable): String = {
    val result = new StringWriter()
    val printWriter = new PrintWriter(result)
    t.printStackTrace(printWriter)
    result.toString()
  }

  def deg2radians(angle: Double) = angle * Math.Pi / 180
  def rad2degrees(angle: Double) = angle * 180 / Math.Pi

  def stripCR(str: String) = str.replaceAll("\r\n", "\n")

  import org.openide.util.NbBundle

  case class BundleMessage(klass: Class[_], key: String) {
    def unapply(action: String): Option[Boolean] = {
      try {
        if (NbBundle.getMessage(klass, key) == action) Some(true) else None
      }
      catch {
        case e: Throwable => None
      }
    }
  }
  
  lazy val userDir = System.getProperty("netbeans.user")
  lazy val libDir = userDir + File.separatorChar + "libk"
  lazy val initScriptDir = userDir + File.separatorChar + "initk"

  lazy val installLibDir = installDir + File.separatorChar + "libk"
  lazy val installInitScriptDir = installDir + File.separatorChar + "initk"
  
  def filesInDir(dir: String, ext: String): List[String] = {
    val osDir = new File(dir)
    if (osDir.exists) {
      osDir.list(new FilenameFilter {
          override def accept(dir: File, name: String) = {
            name.endsWith("." + ext)
          }
        }).toList
    }
    else {
      Nil
    }
  }
  
  lazy val libJars: List[String] = filesInDir(libDir, "jar")
  lazy val initScripts: List[String] = filesInDir(initScriptDir, "kojo")
  lazy val installLibJars: List[String] = filesInDir(installLibDir, "jar")
  lazy val installInitScripts: List[String] = filesInDir(installInitScriptDir, "kojo")

  def isScalaTestAvailable = (libJars ++ installLibJars).exists { fname => fname.toLowerCase contains "scalatest"}

  val scalaTestHelperCode = """
  import org.scalatest.FunSuite
  import org.scalatest.matchers.ShouldMatchers

  class TestRun extends FunSuite {
      override def suiteName = "test"
      def register(name: String)(fn: => Unit) = test(name)(fn)
      def registerIgnored(name: String)(fn: => Unit) = ignore(name)(fn)
  }

  def test(name: String)(fn: => Unit) {
      val suite = new TestRun()
      suite.register(name)(fn)
      suite.execute()
  }

  def ignore(name: String)(fn: => Unit) {
      val suite = new TestRun()
      suite.registerIgnored(name)(fn)
      suite.execute()
  }

  import ShouldMatchers._
"""
  
  lazy val kojoInitCode0: Option[String] = codeFromScripts(initScripts, initScriptDir) 
  lazy val kojoInitCode1: Option[String] = codeFromScripts(installInitScripts, installInitScriptDir) 
  lazy val kojoInitCode = Some(kojoInitCode0.getOrElse("") + kojoInitCode1.getOrElse(""))
  
  def codeFromScripts(scripts: List[String], scriptDir: String): Option[String] = scripts match {
    case Nil => None
    case files => Some(
        files.map { file =>
          "// File: %s\n%s\n" format(file, readFile(new FileInputStream(scriptDir + File.separatorChar + file)))
        }.mkString("\n")
      )
  }
  
  
  def runAsyncQueued(fn: => Unit) {
    asyncRunner ! RunCode { () =>
      fn
    }
  }

  import edu.umd.cs.piccolo.nodes.PText
  def textNode(text: String, x: Double, y: Double): PText = {
    val tnode = new PText(text)
    tnode.getTransformReference(true).setToScale(1, -1)
    tnode.setOffset(x, y)
    tnode
  }
  
  def textNode(text: String, x: Double, y: Double, n: Int): PText = {
    val tnode = textNode(text, x, y)
    val font = new Font(tnode.getFont.getName, Font.PLAIN, n)
    tnode.setFont(font)
    tnode
  }
  
  def reportException(t: Throwable) {
    println("Problem - " + t.getMessage)
    import org.openide.ErrorManager;
    ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, t);
  }
  
  def safeProcess(fn: => Unit) {
    try {
      fn
    }
    catch {
      case t: Throwable => reportException(t)
    }
  }
  
  case class RunCode(code: () => Unit)
  import scala.actors._
  import scala.actors.Actor._
  val asyncRunner = actor {
    loop {
      react {
        case RunCode(code) => 
          safeProcess {
            code()
          }
      }
    }
  }
}
