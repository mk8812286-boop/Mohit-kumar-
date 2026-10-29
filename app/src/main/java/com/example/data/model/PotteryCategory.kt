package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class PotteryCategory(
    val id: String,
    val nameHi: String,
    val nameEn: String,
    val iconName: String,
    val defaultIcon: ImageVector
) {
    ALL("all", "सभी बर्तन", "All Pottery", "grid_view", Icons.Default.GridView),
    MATKA("matka", "मिट्टी के मटके", "Earthen Matkas", "water_drop", Icons.Default.WaterDrop),
    KULHAD("kulhad", "मिट्टी के कुल्हड़", "Chai Kulhads", "local_cafe", Icons.Default.LocalCafe),
    HANDI("handi", "मिट्टी की हांडी", "Cooking Handis", "soup_kitchen", Icons.Default.SoupKitchen),
    SURAHI("surahi", "मिट्टी की सुराही", "Clay Surahis", "wine_bar", Icons.Default.WineBar),
    GAMLA("gamla", "मिट्टी के गमले", "Terracotta Planters", "yard", Icons.Default.Yard),
    DIYA("diya", "मिट्टी के दीये", "Handmade Diyas", "flare", Icons.Default.Flare),
    PLATE_BOWL("plate_bowl", "प्लेट व कटोरी", "Plates & Bowls", "restaurant", Icons.Default.Restaurant),
    TAWA("tawa", "मिट्टी का तवा", "Pure Clay Tawas", "radio_button_checked", Icons.Default.RadioButtonChecked),
    DECORATIVE("decorative", "सजावटी बर्तन", "Decorative Vases", "home", Icons.Default.Home),
    PUJA("puja", "पूजा के बर्तन", "Puja Earthenware", "auto_awesome", Icons.Default.AutoAwesome),
    SPECIAL_ARTISAN("special", "विशेष हस्तकला", "Artisan Specials", "star", Icons.Default.Star),
    CUSTOM("custom", "कस्टम बर्तन", "Custom Pottery", "edit", Icons.Default.Edit)
}
