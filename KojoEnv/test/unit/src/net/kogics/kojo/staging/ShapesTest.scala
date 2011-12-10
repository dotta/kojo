package net.kogics.kojo
package staging

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.junit.ShouldMatchersForJUnit._


@RunWith(classOf[JUnitRunner])
class ShapesTest extends KojoTestBase with FunSuite {
  val S = API
  import S._
  test("circleTranslation") {
    val c = circle(0, 0, 20)
    c.origin should equal(point(0, 0))
    c.radiusX should equal(20)
  }  
}
