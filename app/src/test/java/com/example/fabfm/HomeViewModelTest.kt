package com.example.fabfm

import com.fabfm.ui.home.HomeViewModel
import com.fabfm.ui.home.model.BrowseElement
import com.fabfm.ui.home.model.BrowseState
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val rule = TrampolineSchedulerRule()

    fun buildViewModel(
        apiBlock: FakeRadioTimeService.() -> Unit = { /* no-op default */ }
    ): HomeViewModel {
        return HomeViewModel(FakeRadioTimeService(apiBlock))
    }

    @Test
    fun `can load data successfully`() {

        val expectedContent = listOf(
            BrowseElement.SectionHeader("FM"),
            BrowseElement.Audio(
                url = "http://my.wesite.com/audio.mp3",
                text = "97.1 MyFM",
                image = "http://my.wesite.com/img.png",
                subtext = "Today's top hits",
            ),
            BrowseElement.Link(
                url = "http://my.wesite.com",
                text = "Local Radio",
                image = "http://my.wesite.com/img2.png",
                subtext = "Classic Rock for all ages"
            ),
        )

        val viewModel = buildViewModel { responseSuccess() }
        viewModel.state()
            .test()
            .apply { viewModel.loadData() }
            .assertValueCount(2)
            .assertValueAt(0, BrowseState.Loading)
            .assertValueAt(1, BrowseState.Success(expectedContent, "Local Radio"))
    }

    @Test
    fun `can emit error state`() {
        val viewModel = buildViewModel { responseError() }
        viewModel.state()
            .test()
            .apply { viewModel.loadData() }
            .assertValueCount(2)
            .assertValueAt(0, BrowseState.Loading)
            .assertValueAt(1, BrowseState.Error)
    }

    @Test
    fun `can emit empty state`() {
        val viewModel = buildViewModel { responseEmpty() }
        viewModel.state()
            .test()
            .apply { viewModel.loadData() }
            .assertValueCount(2)
            .assertValueAt(0, BrowseState.Loading)
            .assertValueAt(1, BrowseState.Empty)
    }
}