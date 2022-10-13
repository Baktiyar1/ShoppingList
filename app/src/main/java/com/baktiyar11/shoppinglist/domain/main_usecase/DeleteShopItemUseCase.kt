package com.baktiyar11.shoppinglist.domain.main_usecase

import com.baktiyar11.shoppinglist.domain.ShopItem

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun deleteShopItem(shopItem: ShopItem) = shopListRepository.deleteShopItem(shopItem = shopItem)
}