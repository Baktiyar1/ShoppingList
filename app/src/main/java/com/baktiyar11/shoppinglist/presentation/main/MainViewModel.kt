package com.baktiyar11.shoppinglist.presentation.main

import androidx.lifecycle.ViewModel
import com.baktiyar11.shoppinglist.data.ShopListRepositoryImpl
import com.baktiyar11.shoppinglist.domain.ShopItem
import com.baktiyar11.shoppinglist.domain.main_usecase.*

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    val shopList = GetShopListUseCase(repository).getShopList()

    fun deleteShopList(shopItem: ShopItem) =
        DeleteShopItemUseCase(repository).deleteShopItem(shopItem = shopItem)

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        EditShopItemUseCase(repository).editShopItem(shopItem = newItem)
    }
}