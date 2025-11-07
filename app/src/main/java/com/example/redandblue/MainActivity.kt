package com.example.redandblue

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var button: ImageButton
    private val adapter: MyAdapter = MyAdapter()
    private var currentOrientation: Int = Configuration.ORIENTATION_PORTRAIT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        currentOrientation = resources.configuration.orientation

        val currentWidth = getScreenWidth()

        adapter.setScreenWidth(currentWidth)

        recyclerView = findViewById(R.id.rcView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        setupLayoutManager()

        viewModel.itemMutableLiveData.observe(this) { items ->
            adapter.update(items)
        }

        setupAddButton()
    }
    private fun setupAddButton() {button = findViewById(R.id.button)

        button.setOnClickListener {

            var newItem: Item
            if (adapter.itemCount % 2 == 0) {
                newItem = Item(adapter.itemCount, R.color.my_red)
            } else {
                newItem = Item(adapter.itemCount, R.color.my_blue)
            }

            viewModel.addItem(newItem)
        }
    }

    private fun setupLayoutManager() {
        val spanCount = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            4
        } else {
            3
        }

        recyclerView.layoutManager = GridLayoutManager(this, spanCount)
        adapter.setSpanCount(spanCount)

        if (::recyclerView.isInitialized) {
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation != currentOrientation) {
            currentOrientation = newConfig.orientation

            val newScreenWidth = getScreenWidth()
            adapter.setScreenWidth(newScreenWidth)

            setupLayoutManager()
            adapter.notifyDataSetChanged()
        }
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            return windowMetrics.bounds.width()
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }
    }
}