package com.koresuniku.wishmaster.ui.controller.view_interface

interface IListViewAdapterCreatable {
    fun adapterIsCreated(): Boolean

    fun createListViewAdapter()
}