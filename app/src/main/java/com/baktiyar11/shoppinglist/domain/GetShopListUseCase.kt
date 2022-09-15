package com.baktiyar11.shoppinglist.domain

import androidx.lifecycle.LiveData

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopList(): LiveData<List<ShopItem>> = shopListRepository.getShopList()
}