package com.baktiyar11.shoppinglist.domain.main_usecase

import androidx.lifecycle.LiveData
import com.baktiyar11.shoppinglist.domain.ShopItem

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopList(): LiveData<List<ShopItem>> = shopListRepository.getShopList()
}