package com.baktiyar11.shoppinglist.domain

import com.baktiyar11.shoppinglist.domain.utils.UNDEFINED_ID

data class ShopItem(
    var id: Int = UNDEFINED_ID,
    var name: String,
    var count: Int,
    var enabled: Boolean,
)