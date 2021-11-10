package model

private const val TYPE_LINK = "link"
private const val TYPE_AUDIO = "audio"
class RadioTimeTransformer {

    fun transform(response: RadioTimeResponse): RadioTimeState {
        return RadioTimeState.Success(
            elements = response.body?.mapNotNull { transform(it) },
            title = response.head?.title
        )
    }

    private fun transform(response: RadioTimeElementResponse): RadioTimeElement? {
        return when {
            response.type == TYPE_LINK -> response.url?.let { url ->
                RadioTimeElement.Link(
                    url = url,
                    text = response.text,
                    subtext = response.subtext,
                    image = response.image,
                )
            }
            response.type == TYPE_AUDIO -> response.url?.let { url ->
                RadioTimeElement.Audio(
                    url = url,
                    text = response.text,
                    subtext = response.subtext,
                    image = response.image,
                )
            }
            !response.children.isNullOrEmpty() ->
                RadioTimeElement.Section(
                    text = response.text,
                    elements = response.children.mapNotNull { transform(it) }
                )
            else -> null
        }
    }
}