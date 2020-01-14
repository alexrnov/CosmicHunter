package alexrnov.cosmichunter.utils

import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class MeanValueTest {

  private var meanValue: MeanValue? = null
  @Before
  fun setUp() {
    meanValue = MeanValue(5000)
  }

  @After
  fun tearDown() {
    meanValue = null
  }

  @Test
  fun t() {
    if (meanValue != null) {
      var v: Float = meanValue!!.add(1.0f)
      v = meanValue!!.add(2.0f)
      println("v = $v")
    }
  }
}