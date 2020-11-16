package com.mywidget.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

abstract class BaseActivity<V : ViewModel, D : ViewDataBinding>
    : AppCompatActivity() {

    //protected lateinit var viewModel: V
    protected lateinit var binding: D
    protected abstract val layout: Int
    protected abstract fun getViewModel(): Class<V>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this
        //val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        //viewModel = ViewModelProvider(this, factory).get(getViewModel())
    }
}