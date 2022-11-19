package com.baktiyar11.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baktiyar11.shoppinglist.domain.ShopItem
import com.baktiyar11.shoppinglist.domain.main_usecase.ShopListRepository
import com.baktiyar11.shoppinglist.domain.utils.AUTO_INCREMENT_ID
import com.baktiyar11.shoppinglist.domain.utils.UNDEFINED_ID
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {

    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = AUTO_INCREMENT_ID

    init {
        for (i in 0 until 10) {
            val item = ShopItem(name = "Name $i", count = i, enabled = Random.nextBoolean())
            addShopItem(shopItem = item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == UNDEFINED_ID) shopItem.id = autoIncrementId++
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItemId = shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem = shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem = shopList.find { it.id == shopItemId }
        ?: throw RuntimeException("Element with id $shopItemId not found")

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    private fun updateList() {
        shopListLD.value = shopList.toList()
    }
}