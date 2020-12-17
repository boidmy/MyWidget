package com.mywidget.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mywidget.R
import com.mywidget.ui.main.recyclerview.MainTabMemoAdapter
import com.mywidget.databinding.MainFragmentRvBinding
import com.mywidget.ui.base.BaseFragment
import com.mywidget.ui.main.MainFragmentViewModel
import javax.inject.Inject

class FragmentMemo : BaseFragment<MainFragmentRvBinding>() {
    @Inject lateinit var mAdapter: MainTabMemoAdapter
    @Inject lateinit var factory: ViewModelProvider.Factory
    val viewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(MainFragmentViewModel::class.java)}
    override fun getLayout(): Int {
        return R.layout.main_fragment_rv
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, parent, savedInstanceState)
        bindView()

        return binding.root
    }

    private fun bindView() {
        mAdapter = MainTabMemoAdapter()
        binding.fragmentRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.fragmentRv.adapter = mAdapter
        binding.data = viewModel.memoData
        mAdapter.setViewModel(viewModel)

        viewModel.guidTextVisibility.observe(requireActivity(), Observer {
            binding.guidTxt.isVisible = it
        })

        selectCall()
    }

    private fun selectCall() {
        Thread {
            viewModel.selectMemo()
        }.start()
    }
}