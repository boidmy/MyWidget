package com.mywidget.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

abstract class BaseFragment<V : ViewModel, D : ViewDataBinding> : Fragment() {

    protected lateinit var viewModel : V
    protected lateinit var binding: D

    protected abstract fun getLayout(): Int
    protected abstract fun getViewModel(): Class<V>

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayout(), parent, false)
        binding.lifecycleOwner = this
        /*val factory = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(getViewModel())*/
        return binding.root
    }
}