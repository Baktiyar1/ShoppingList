package com.baktiyar11.shoppinglist.domain.main_usecase

import androidx.lifecycle.LiveData
import com.baktiyar11.shoppinglist.domain.ShopItem

interface ShopListRepository {
    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun editShopItem(shopItem: ShopItem)
    fun getShopItem(shopItemId: Int): ShopItem
    fun getShopList(): LiveData<List<ShopItem>>
}