package com.mywidget.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.android.support.DaggerFragment

abstract class BaseFragment<D : ViewDataBinding> : DaggerFragment() {

    protected lateinit var binding: D

    protected abstract fun getLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayout(), parent, false)
        binding.lifecycleOwner = this
        return binding.root
    }
}