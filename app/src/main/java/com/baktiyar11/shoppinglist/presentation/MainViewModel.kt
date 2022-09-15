package com.baktiyar11.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.baktiyar11.shoppinglist.data.ShopListRepositoryImpl
import com.baktiyar11.shoppinglist.domain.DeleteShopItemUseCase
import com.baktiyar11.shoppinglist.domain.EditShopItemUseCase
import com.baktiyar11.shoppinglist.domain.GetShopListUseCase
import com.baktiyar11.shoppinglist.domain.ShopItem

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    val shopList = GetShopListUseCase(repository).getShopList()

    fun deleteShopList(shopItem: ShopItem) =
        DeleteShopItemUseCase(repository).deleteShopItem(shopItem)

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        EditShopItemUseCase(repository).editShopItem(newItem)
    }
}