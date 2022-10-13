package com.baktiyar11.shoppinglist.domain.main_usecase

import com.baktiyar11.shoppinglist.domain.ShopItem

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun editShopItem(shopItem: ShopItem) = shopListRepository.editShopItem(shopItem = shopItem)
}