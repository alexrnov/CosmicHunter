package alexrnov.cosmichunter.utils

import org.junit.After
import org.junit.Before
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.*
import org.junit.Test

class MeanValueTest {
  private var meanValue = MeanValue(5)
  @Test
  fun t() {
    (0..5).forEach { meanValue.add(it.toFloat()) }
    //println("v = ${meanValue.add(2.0f)}")
    //assertEquals(3.2f, meanValue.add(2.0f))
    val b = true
    assertThat(meanValue.add(2.0f)).isEqualTo(3.2f) // использование библиотеки Truth
  }
}