package com.mywidget.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolderBase<BINDING : ViewDataBinding>(
    parent: ViewGroup,
    layout: Int,
    protected val binding: BINDING = DataBindingUtil.inflate(LayoutInflater.from(parent.context)
        , layout, parent, false)
): RecyclerView.ViewHolder(binding.root)