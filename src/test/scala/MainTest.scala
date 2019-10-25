package scala

import example.CubeCalculator
import org.scalatest.FunSuite

class MainTest extends FunSuite {
  test("CubeCalculator.cube") {
     assert(CubeCalculator.cube(3) === 27)
  }
}

