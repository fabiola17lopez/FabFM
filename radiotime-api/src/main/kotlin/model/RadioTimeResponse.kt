package model

import com.squareup.moshi.Json

data class RadioTimeResponse(
    @field:Json(name = "head") val head: RadioTimeHeadResponse?,
    @field:Json(name = "body") val body: List<RadioTimeElementResponse>?,
)

data class RadioTimeHeadResponse(
    @field:Json(name = "title") val title: String?,
)

data class RadioTimeElementResponse(
    @field:Json(name = "type") val type: String? = null,
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "URL") val url: String? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "subtext") val subtext: String? = null,
    @field:Json(name = "children") val children: List<RadioTimeElementResponse>? = null,
)