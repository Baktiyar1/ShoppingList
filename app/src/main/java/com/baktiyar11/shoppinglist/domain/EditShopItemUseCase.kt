package com.baktiyar11.shoppinglist.domain

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun editShopItem(shopItem: ShopItem) = shopListRepository.editShopItem(shopItem)
}