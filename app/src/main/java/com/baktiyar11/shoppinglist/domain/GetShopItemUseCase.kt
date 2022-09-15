package com.baktiyar11.shoppinglist.domain

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopItem(shopItemId: Int): ShopItem = shopListRepository.getShopItem(shopItemId)
}