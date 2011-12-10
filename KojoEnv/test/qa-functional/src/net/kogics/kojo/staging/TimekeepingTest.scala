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
package net.kogics.kojo
package staging

import org.junit.Test
import org.junit.Assert._

import net.kogics.kojo.util._

class TimekeepingTest extends StagingTestBase {

  val f = SpriteCanvas.instance.figure0
  def peekPNode = f.dumpLastChild

  /* Testing manifest
   *
   * def millis = System.currentTimeMillis()
   * def second = Calendar.getInstance().get(Calendar.SECOND)
   * def minute = Calendar.getInstance().get(Calendar.MINUTE)
   * def hour   = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
   * def day    = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
   * def month  = Calendar.getInstance().get(Calendar.MONTH) + 1
   * def year   = Calendar.getInstance().get(Calendar.YEAR)
   */

  @Test
  // lalit sez: if we have more than five tests, we run out of heap space - maybe a leak in the Scala interpreter/compiler
  // subsystem. So we run (mostly) everything in one test
  def test1 = {
  //W
  //W==Timekeeping==
  //W
  //WA number of methods report the current time.
  //W
  //W
  //W{{{
  //Wmillis // milliseconds
    Tester("Staging.millis")

  //Wsecond // second of the minute
    Tester(
      "val a = Staging.second ; println(a.isInstanceOf[Int] && a >= 0 && a < 60)",
      Some("$truea: Int = \\d+")
    )

  //Wminute // minute of the hour
    Tester(
      "val a = Staging.minute ; println(a.isInstanceOf[Int] && a >= 0 && a < 60)",
      Some("$truea: Int = \\d+")
    )

  //Whour   // hour of the day
    Tester(
      "val a = Staging.hour ; println(a.isInstanceOf[Int] && a >= 0 && a < 24)",
      Some("$truea: Int = \\d+")
    )

  //Wday    // day of the month
    Tester(
      "val a = Staging.day ; println(a.isInstanceOf[Int] && a > 0 && a <= 31)",
      Some("$truea: Int = \\d+")
    )

  //Wmonth  // month of the year (1..12)
    Tester(
      "val a = Staging.month ; println(a.isInstanceOf[Int] && a > 0 && a <= 12)",
      Some("$truea: Int = \\d+")
    )

  //Wyear   // year C.E.
    Tester(
      "val a = Staging.year ; println(a.isInstanceOf[Int] && a > 2009)",
      Some("$truea: Int = \\d+")
    )
  //W}}}
  //W

    
  }
}

