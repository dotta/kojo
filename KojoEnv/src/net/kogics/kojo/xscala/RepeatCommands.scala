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

import util.Throttler

trait RepeatCommands {
  def repeat(n: Int) (fn: => Unit) {
    for (i <- 1 to n) {
      fn
      Throttler.throttle()
    }
  }
  UserCommand.addCompletion("repeat", " (${n}) {\n    ${cursor}\n}")
  UserCommand.addSynopsis("repeat(n) {} - Repeats the commands within braces n number of times.")

  def repeati(n: Int) (fn: Int => Unit) {
    for (i <- 1 to n) {
      fn(i)
      Throttler.throttle()
    }
  }
  UserCommand.addCompletion("repeati", " (${n}) {i => \n    ${cursor}\n}")
  UserCommand.addSynopsis("repeat(n) {} - Repeats the commands within braces n number of times. The current repeat index is available within the braces.")

  def repeatWhile(cond: => Boolean) (fn: => Unit) {
    while (cond) {
      fn
      Throttler.throttle()
    }
  }
  UserCommand.addCompletion("repeatWhile", " (${condition}) {\n    ${cursor}\n}")
  UserCommand.addSynopsis("repeat(n) {} - Repeats the commands within braces while the given condition is true.")

  def repeatUntil(cond: => Boolean) (fn: => Unit) {
    while (!cond) {
      fn
      Throttler.throttle()
    }
  }
  UserCommand.addCompletion("repeatUntil", " (${condition}) {\n    ${cursor}\n}")
  UserCommand.addSynopsis("repeat(n) {} - Repeats the commands within braces until the given condition is true.")
}
