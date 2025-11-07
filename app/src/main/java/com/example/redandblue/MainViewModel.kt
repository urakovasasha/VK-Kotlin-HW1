package com.example.redandblue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel : ViewModel () {
    var itemMutableLiveData : MutableLiveData<ArrayList<Item>> = MutableLiveData()
    var itemList : ArrayList<Item> = ArrayList()

    init {
        itemMutableLiveData.value = ArrayList()
    }

    fun addItem(item: Item) {
        itemList.add(item)
        itemMutableLiveData.value = itemList
    }

}