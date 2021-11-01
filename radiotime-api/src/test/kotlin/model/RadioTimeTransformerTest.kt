package model

import org.junit.Assert
import org.junit.Test

class RadioTimeTransformerTest {

    private fun transformer(): RadioTimeTransformer = RadioTimeTransformer()

    @Test
    fun `can transform page with links`() {
        val response = RadioTimeResponse(
            head = RadioTimeHeadResponse(title = "Browse"),
            body = listOf(
                RadioTimeElementResponse(
                    type = "link",
                    text = "Local Radio",
                    url = "http://my.wesite.com",
                    image = "http://my.wesite.com/img.png",
                ),
                // item with null url should not be included
                RadioTimeElementResponse(
                    type = "link",
                    text = "Podcasts",
                    url = null,
                ),
                RadioTimeElementResponse(
                    type = "link",
                    text = "Music",
                    url = "http://my.wesite.com/music",
                ),
            )
        )

        val expectedResponse = RadioTimeState.Success(
            title = "Browse",
            elements = listOf(
                RadioTimeElement.Link(
                    url = "http://my.wesite.com",
                    text = "Local Radio",
                    image = "http://my.wesite.com/img.png",
                ),
                RadioTimeElement.Link(
                    url = "http://my.wesite.com/music",
                    text = "Music",
                ),
            )
        )

        Assert.assertEquals(
            expectedResponse,
            transformer().transform(response)
        )
    }

    @Test
    fun `can transform page with audio`() {
        val response = RadioTimeResponse(
            head = RadioTimeHeadResponse(title = "Local Radio"),
            body = listOf(
                RadioTimeElementResponse(
                    type = "audio",
                    text = "97.1 MyFM",
                    url = "http://my.wesite.com/audio.mp3",
                    image = "http://my.wesite.com/img.png",
                    subtext = "Today's top hits",
                ),
                // item with null url should not be included
                RadioTimeElementResponse(
                    type = "audio",
                    text = "102.5 Classic Rock",
                    url = null,
                ),
            )
        )

        val expectedResponse = RadioTimeState.Success(
            title = "Local Radio",
            elements = listOf(
                RadioTimeElement.Audio(
                    url = "http://my.wesite.com/audio.mp3",
                    text = "97.1 MyFM",
                    image = "http://my.wesite.com/img.png",
                    subtext = "Today's top hits",
                ),
            )
        )

        Assert.assertEquals(
            expectedResponse,
            transformer().transform(response)
        )
    }

    @Test
    fun `can transform page with sections`() {
        val response = RadioTimeResponse(
            head = RadioTimeHeadResponse(title = "Local Radio"),
            body = listOf(
                RadioTimeElementResponse(
                    text = "FM",
                    children = listOf(
                        RadioTimeElementResponse(
                            type = "audio",
                            text = "97.1 MyFM",
                            url = "http://my.wesite.com/audio.mp3",
                            image = "http://my.wesite.com/img.png",
                            subtext = "Today's top hits",
                        ),
                        // item with null url should not be included
                        RadioTimeElementResponse(
                            type = "audio",
                            text = "102.5 Classic Rock",
                            url = null,
                        ),
                    )
                )
            )
        )

        val expectedResponse = RadioTimeState.Success(
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

        Assert.assertEquals(
            expectedResponse,
            transformer().transform(response)
        )
    }
}