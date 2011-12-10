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

object KojoTestUtil {
  import java.io.File
  
  def nbDirs = {
    initNetbeansDirs()
    System.getProperty("netbeans.dirs")
  }
  
  def userDir = nbDirs + "../testuserdir"
  
  def initNetbeansDirs() {
    if (System.getProperty("netbeans.dirs") != null)  
      return

    if (System.getProperty("os.name") == "Linux") {
      val workDir = System.getProperty("nbjunit.workdir")
      val wd = new File(workDir)
      if (!wd.exists) wd.mkdirs()
    
      val fileStr = workDir + "/../../../../../build/cluster"
      val file = new File(fileStr)
      initNbDir(file, fileStr)
      
      val userDir = workDir + "/../../../../../build/testuserdir"
      initUserDir(userDir)
    }
    else {
      val workDir = System.getProperty("nbjunit.workdir")
      val fileStr = workDir + "../../../../../../build/cluster"
      val file = new java.io.File(fileStr)
      initNbDir(file, fileStr)
      val userDir = workDir + "../../../../../../build/testuserdir"
      initUserDir(userDir)
    }
  }
  
  private def initNbDir(file: File, fileStr: String) {
    if (file.exists) {
      System.setProperty("netbeans.dirs", fileStr)
    }
    else {
      throw new RuntimeException("Bad Netbeans Dirs!")
    }
  }
  
  private def initUserDir(userDir: String) {
    val configDir = new File(userDir + File.separator + "config")
    if (!configDir.exists) configDir.mkdirs()
    System.setProperty("netbeans.user", userDir)
  }
}