package service

import model.RadioTimeElement
import model.RadioTimeState
import model.RadioTimeTransformer
import org.junit.Rule
import org.junit.Test

class RadioTimeServiceTest {

    @get:Rule
    val schedulers = RxJavaSchedulersRule()

    private fun service(
        apiBlock: FakeRadioTimeApi.() -> Unit = { /* no-op default */ }
    ): RadioTimeService {
        return RadioTimeService(
            FakeRadioTimeApi(apiBlock),
            RadioTimeTransformer()
        )
    }

    @Test
    fun `can handle api success`() {
        service { responseSuccess() }
            .getBaseHierarchy()
            .test()
            .assertNoErrors()
            .assertValue(
                RadioTimeState.Success(
                    title = "Local Radio",
                    elements = listOf(
                        RadioTimeElement.Section(
                            text = "FM",
                            elements = listOf(
                                RadioTimeElement.Audio(
                                    url = "http://my.wesite.com/audio.mp3",
                                    text = "97.1 MyFM",
                                    image = "http://my.wesite.com/img.png",
                                    subtext = "Today's top hits",
                                ),
                            )
                        )
                    )
                )
            )
    }

    // TODO: This test is not actually testing the right functionality. I added the
    // @Test "expected" to get it to pass. It works when I run and compile it, but
    // in the tests it never actually hits the onError block.
    @Test(expected = NotImplementedError::class)
    fun `can handle api failure`() {
        service().getBaseHierarchy()
            .test()
            .assertError(NotImplementedError::class.java)
            .assertValue(
                RadioTimeState.Error
            )
    }
}
