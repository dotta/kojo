/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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
package music

import java.util.concurrent.atomic.AtomicBoolean
import scala.actors._
import scala.actors.Actor._

import org.jfugue.{Rhythm => JFRhythm, _}
import java.util.logging._

case class PlayVoice(v: core.Voice, n: Int, valid: AtomicBoolean)
case class PlayVoiceUntilDone(v: core.Voice, n: Int, valid: AtomicBoolean)
case object Done
case object MusicDefGood
case class MusicError(e: Exception)

object MusicPlayer extends Singleton[MusicPlayer] {
  protected def newInstance = new MusicPlayer
}

class MusicPlayer {
  val Log = Logger.getLogger(getClass.getName)
  @volatile private var currMusic: Option[Music] = None
  @volatile private var validBool = new AtomicBoolean(true)
  var outputFn: String => Unit = { msg =>
  }

  val asyncPlayer = actor {

    while(true) {
      receive {
        case PlayVoice(v, n, valid) =>
          if (valid.get) {
            try {
              createMusic(v, n)
              reply(MusicDefGood)
              playMusic()
            }
            catch {
              case e: Exception =>
                Log.severe(e.getMessage) // we might have replied by the time we got here. So log problem.
                reply(MusicError(e))
            }
          }
          else {
            reply(Done)
          }
        case PlayVoiceUntilDone(v, n, valid) =>
          if (valid.get) {
            try {
              createMusic(v, n)
              playMusic()
              reply(Done)
            }
            catch {
              case e: Exception =>
                reply(MusicError(e))
            }
          }
          else {
            reply(Done)
          }
      }
    }

    def createMusic(v: core.Voice, n: Int) {
      currMusic = Some(Music(v, n))
    }

    def playMusic() {
      currMusic.get.play()
    }
  }

  def playMusic(voice: core.Voice, n: Int = 1) {
    val ret = asyncPlayer !? PlayVoice(voice, n, validBool)
    ret match {
      case MusicError(e) => throw e
      case MusicDefGood =>
      case Done =>
    }
  }

  def playMusicUntilDone(voice: core.Voice, n: Int) {
    val ret = asyncPlayer !? PlayVoiceUntilDone(voice, n, validBool)
    ret match {
      case MusicError(e) => throw e
      case Done =>
    }
  }

  def stopMusic() {
    validBool.set(false)
    validBool = new AtomicBoolean(true)
    if (currMusic.isDefined) {
      currMusic.get.stop()
      currMusic = None
    }
  }
}
