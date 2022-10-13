package com.baktiyar11.shoppinglist.presentation.shopItem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.baktiyar11.shoppinglist.R
import com.baktiyar11.shoppinglist.databinding.ActivityShopItemBinding
import com.baktiyar11.shoppinglist.domain.utils.UNDEFINED_ID

class ShopItemActivity : AppCompatActivity() {

    private val binding: ActivityShopItemBinding by lazy {
        ActivityShopItemBinding.inflate(layoutInflater)
    }
    private val viewModel: ShopItemViewModel by lazy {
        ViewModelProvider(this)[ShopItemViewModel::class.java]
    }
    private val mode: String by lazy {
        intent.getStringExtra(EXTRA_SCREEN_MODE).toString()
    }
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseIntent()
        addTextChangeListener()
        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel() = viewModel.apply {
        errorInputCount.observe(this@ShopItemActivity) {
            val message = if (it) getString(R.string.error_input_count)
            else null
            binding.tilCount.error = message
        }
        errorInputName.observe(this@ShopItemActivity) {
            val message = if (it) getString(R.string.error_input_name)
            else null
            binding.tilName.error = message
        }
        shouldCloseScreen.observe(this@ShopItemActivity) {
            finish()
        }
    }

    private fun launchRightMode() {
        when (mode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangeListener() = binding.apply {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun launchEditMode() = binding.apply {
        viewModel.getShopItem(shopItemId = shopItemId)
        val shopItem = viewModel.shopItem
        shopItem.observe(this@ShopItemActivity) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonAdd.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchAddMode() = binding.apply {
        buttonAdd.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) throw RuntimeException("Param screen mode is absent")
        if (mode != MODE_ADD && mode != MODE_EDIT) throw RuntimeException("Unknown screen mode $mode")
        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) throw RuntimeException("Param shop item id is absent")
            else shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, UNDEFINED_ID)
        }

    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}