package com.baktiyar11.shoppinglist.domain

import com.baktiyar11.shoppinglist.domain.utils.UNDEFINED_ID
import java.io.Serializable

data class ShopItem(
    var name: String,
    var count: Int,
    var enabled: Boolean,
    var id: Int = UNDEFINED_ID,
)