package com.baktiyar11.shoppinglist.presentation.shopItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baktiyar11.shoppinglist.data.ShopListRepositoryImpl
import com.baktiyar11.shoppinglist.domain.ShopItem
import com.baktiyar11.shoppinglist.domain.main_usecase.AddShopItemUseCase
import com.baktiyar11.shoppinglist.domain.main_usecase.EditShopItemUseCase
import com.baktiyar11.shoppinglist.domain.main_usecase.GetShopItemUseCase

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean> get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean> get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem> get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit> get() = _shouldCloseScreen

    fun getShopItem(shopItemId: Int) {
        _shopItem.value = GetShopItemUseCase(repository).getShopItem(shopItemId = shopItemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        if (validateInput(name, count)) {
            _shopItem.value?.let {
                AddShopItemUseCase(repository)
                    .addShopItem(shopItem = it.copy(name = name, count = count))
                finishWork()
            }
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName = inputName)
        val count = parseCount(inputCount = inputCount)
        if (validateInput(name, count)) {
            _shopItem.value?.let {
                EditShopItemUseCase(shopListRepository = repository)
                    .editShopItem(shopItem = it.copy(name = name, count = count))
                finishWork()
            }
        }
    }

    private fun parseName(inputName: String?): String = inputName?.trim() ?: ""

    private fun parseCount(inputCount: String?): Int = try {
        inputCount?.trim()?.toInt() ?: 0
    } catch (e: Exception) {
        0
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() = run { _errorInputName.value = false }

    fun resetErrorInputCount() = run { _errorInputCount.value = false }

    private fun finishWork() = run { _shouldCloseScreen.value = Unit }
}