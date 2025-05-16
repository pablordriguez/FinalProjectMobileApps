package com.example.finamarvelapp.data.model

data class Hero(
    val id: Int = 0,
    val name: String = "",
    val images: Images = Images(),
    val biography: Biography = Biography()
)

data class Images(
    val xs: String = "",
    val sm: String = "",
    val md: String = "",
    val lg: String = ""
)

data class Biography(
    val fullName: String = "",
    val publisher: String = ""
)

