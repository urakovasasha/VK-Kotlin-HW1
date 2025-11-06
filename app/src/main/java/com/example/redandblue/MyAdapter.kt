package com.example.redandblue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyHolder>() {

    private val items = ArrayList<Item>()
    private var screenWidth: Int = 0
    private var spanCount: Int = 3

    fun setScreenWidth(width: Int) {
        screenWidth = width
    }

    fun setSpanCount(spanCnt: Int) {
        spanCount = spanCnt
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHolder {
        return MyHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: MyHolder,
        position: Int
    ) {
        val item = items[position]
        holder.bind(item)
        holder.setCardViewSize(screenWidth / spanCount)
        holder.setCardViewColor(item.color)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun setItems(items: List<Item>) {
        this.items.clear()
        this.items.addAll(items)
    }

    fun update(items: List<Item>) {
        val differ = MainDifferCallback(this.items, items)
        val result = DiffUtil.calculateDiff(differ)
        setItems(items)
        result.dispatchUpdatesTo(this)
    }

    class MyHolder (view: View): RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.textView)
        private val frameLayout = view.findViewById<FrameLayout>(R.id.itemFrameLayout)
        private val cardView = view.findViewById<CardView>(R.id.itemCardView)

        fun bind(item: Item) {
            title.text = item.ind.toString()
        }

        fun setCardViewSize(width: Int) {
            val layoutParams = frameLayout.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.width = width
            layoutParams.height = width
            frameLayout.layoutParams = layoutParams
        }

        fun setCardViewColor(colorId: Int) {
            val color = itemView.context.getColor(colorId)
            cardView.setBackgroundColor(color)
        }
    }
}

class MainDifferCallback(val oldItems: List<Item>, val newItems: List<Item>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].ind == newItems[newItemPosition].ind
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].color == newItems[newItemPosition].color
    }

}