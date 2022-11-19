package com.baktiyar11.shoppinglist.presentation.shopItem

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.baktiyar11.shoppinglist.R
import com.baktiyar11.shoppinglist.databinding.FragmentShopItemBinding
import com.baktiyar11.shoppinglist.domain.utils.UNDEFINED_ID

class ShopItemFragment : Fragment() {

    private val binding: FragmentShopItemBinding by lazy {
        FragmentShopItemBinding.inflate(layoutInflater)
    }
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener
    private val viewModel: ShopItemViewModel by lazy {
        ViewModelProvider(this)[ShopItemViewModel::class.java]
    }
    private var mode: String = MODE_UNKNOWN
    private var shopItemId: Int = UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) onEditingFinishedListener = context
        else throw RuntimeException("Activity must implement OnEditingFinishedListener")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextChangeListener()
        launchRightMode()
        observeViewModel()
    }

    private fun parseParams() {
        val args = requireArguments()
        if (args.containsKey(SCREEN_MODE)) {
            val screenMode = args.getString(SCREEN_MODE)
            if (screenMode != MODE_ADD && screenMode != MODE_EDIT) throw RuntimeException("Unknown screen mode $screenMode")
            else mode = args.getString(SCREEN_MODE) as String
        } else throw RuntimeException("Params screen mode is absent")

        if (mode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) throw RuntimeException("Param shop item id is absent")
            else shopItemId = args.getInt(SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }

    private fun observeViewModel() = viewModel.apply {
        errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) getString(R.string.error_input_count)
            else null
            binding.tilCount.error = message
        }
        errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) getString(R.string.error_input_name)
            else null
            binding.tilName.error = message
        }
        shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
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
        shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonAdd.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
            requireActivity().onBackPressed()
        }
    }

    private fun launchAddMode() = binding.apply {
        buttonAdd.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}