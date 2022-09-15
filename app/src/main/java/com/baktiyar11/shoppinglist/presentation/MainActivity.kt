package com.baktiyar11.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.baktiyar11.shoppinglist.databinding.ActivityMainBinding
import com.baktiyar11.shoppinglist.domain.utils.MAX_POOL_SIZE
import com.baktiyar11.shoppinglist.domain.utils.VIEW_TYPE_DISABLED
import com.baktiyar11.shoppinglist.domain.utils.VIEW_TYPE_ENABLED
import com.baktiyar11.shoppinglist.presentation.adapter.ShopListAdapter


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
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
        shopListAdapter.onShopItemClickListener = {
            Log.d("MainActivity", "Alright")
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = { shopItem ->
            viewModel.changeEnableState(shopItem = shopItem)
        }
    }
}