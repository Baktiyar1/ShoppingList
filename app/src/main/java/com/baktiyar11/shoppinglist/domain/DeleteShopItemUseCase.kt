package com.baktiyar11.shoppinglist.domain

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun deleteShopItem(shopItem: ShopItem) = shopListRepository.deleteShopItem(shopItem)
}