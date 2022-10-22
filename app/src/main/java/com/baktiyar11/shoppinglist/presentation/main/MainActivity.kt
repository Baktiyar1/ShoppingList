package com.baktiyar11.shoppinglist.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.baktiyar11.shoppinglist.R
import com.baktiyar11.shoppinglist.databinding.ActivityMainBinding
import com.baktiyar11.shoppinglist.domain.utils.MAX_POOL_SIZE
import com.baktiyar11.shoppinglist.domain.utils.VIEW_TYPE_DISABLED
import com.baktiyar11.shoppinglist.domain.utils.VIEW_TYPE_ENABLED
import com.baktiyar11.shoppinglist.domain.utils.toast
import com.baktiyar11.shoppinglist.presentation.main.adapter.ShopListAdapter
import com.baktiyar11.shoppinglist.presentation.shopItem.ShopItemActivity
import com.baktiyar11.shoppinglist.presentation.shopItem.ShopItemFragment

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val shopListAdapter: ShopListAdapter by lazy {
        ShopListAdapter()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
    }

    private fun setupRecyclerView() = binding.apply {
        viewModel.shopList.observe(this@MainActivity) {
            shopListAdapter.submitList(it)
        }
        rvShopItem.apply {
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_ENABLED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_DISABLED, MAX_POOL_SIZE)
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeDeleteListener(rvShopItem)
        buttonAddShopItem.setOnClickListener {
            if (isOnePaneMode()) startActivity(ShopItemActivity.newIntentAddItem(context = this@MainActivity))
            else launchFragment(fragment = ShopItemFragment.newInstanceAddItem())
        }
    }

    override fun onEditingFinished() {
        toast("Success")
        supportFragmentManager.popBackStack()
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().replace(R.id.shop_item_container, fragment)
            .addToBackStack(null).commit()
    }

    private fun isOnePaneMode(): Boolean {
        return binding.shopItemContainer == null
    }

    private fun setupSwipeDeleteListener(rvShopItem: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteShopList(shopListAdapter.currentList[viewHolder.adapterPosition])
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(rvShopItem)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = { shopItem ->
            if (isOnePaneMode()) startActivity(ShopItemActivity.newIntentEditItem(
                context = this@MainActivity, shopItemId = shopItem.id))
            else launchFragment(ShopItemFragment.newInstanceEditItem(shopItemId = shopItem.id))
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = { shopItem ->
            viewModel.changeEnableState(shopItem = shopItem)
        }
    }
}