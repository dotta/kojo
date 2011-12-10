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

object Throttler {
  val systemThrottler = new Throttler(1)

  def throttle() = systemThrottler.throttle()
}

class Throttler(size: Int) {

  val MaxDelay = 1000 // ms
  val ThrottleThreshold = 10 // ms
  val MaxUninterruptibleCalls = 10

  val lastCallTime = new ThreadLocal[Long] {
    override def initialValue = System.currentTimeMillis
  }

  val avgDelay = new ThreadLocal[Float] {
    override def initialValue = 1000f
  }

  val uninterruptibleCalls = new ThreadLocal[Int] {
    override def initialValue = 0
  }

  /**
   * Slow things down if stuff is happening too quickly
   * Meant to slow down runaway computation inside the interpreter, so that the
   * user can interrupt the runaway thread
   */
  def throttle() {
    val currTime = System.currentTimeMillis
    val delta =  currTime - lastCallTime.get
    lastCallTime.set(currTime)

    avgDelay.set((avgDelay.get + delta) / 2)

    if (avgDelay.get < ThrottleThreshold) {
      allowInterruption()
    }
    else {
      uninterruptibleCalls.set(uninterruptibleCalls.get + 1)
      if (avgDelay.get > MaxDelay) {
        avgDelay.set(MaxDelay)
      }
    }

    if (uninterruptibleCalls.get > MaxUninterruptibleCalls) {
      allowInterruption()
    }
  }

  def allowInterruption() {
    uninterruptibleCalls.set(0)
    Thread.sleep(size) // Throws interrupted exception if the thread has been interrupted
  }
}
