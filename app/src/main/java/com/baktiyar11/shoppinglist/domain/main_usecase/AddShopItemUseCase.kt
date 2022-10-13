package com.baktiyar11.shoppinglist.domain.main_usecase

import com.baktiyar11.shoppinglist.domain.ShopItem

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun addShopItem(shopItem: ShopItem) = shopListRepository.addShopItem(shopItem = shopItem)
}