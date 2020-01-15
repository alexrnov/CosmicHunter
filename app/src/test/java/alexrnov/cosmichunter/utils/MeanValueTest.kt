package alexrnov.cosmichunter.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MeanValueTest {
  private var meanValue = MeanValue(5)

  @Test
  fun `mean value for queue`() {
    (0..5).forEach { meanValue.add(it.toFloat()) }
    // использование библиотеки Truth: аналогично выражению assertEquals(3.2f, meanValue.add(2.0f))
    assertThat(meanValue.add(2.0f)).isEqualTo(3.2f)
  }
}