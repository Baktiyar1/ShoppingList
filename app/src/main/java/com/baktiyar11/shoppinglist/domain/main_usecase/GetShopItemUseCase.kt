package com.baktiyar11.shoppinglist.domain.main_usecase

import com.baktiyar11.shoppinglist.domain.ShopItem

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopItem(shopItemId: Int): ShopItem = shopListRepository.getShopItem(shopItemId = shopItemId)
}