package alexrnov.cosmichunter

import android.content.Context
import org.junit.Assert.*

import org.hamcrest.CoreMatchers.`is`
//import org.hamcrest.core.Is.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import com.google.common.truth.Truth.assertThat

private const val FAKE_STRING = "Cosmic hunter"

@RunWith(MockitoJUnitRunner::class)
class ClassForDemoMockTest {

  @Mock
  private lateinit var mockContext: Context

  @Test
  fun f() {
    `when`(mockContext.getString(R.string.app_name)).thenReturn(FAKE_STRING)
    val classForDemoMock = ClassForDemoMock(mockContext)
    val result: String = classForDemoMock.printApplicationInfo()
    //assertThat(result).isEqualTo(FAKE_STRING)
    assertThat(result, `is`(FAKE_STRING)) // аналогично проверке выше
  }
}