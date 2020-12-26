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
import com.mywidget.databinding.MainFragmentDDayBinding
import com.mywidget.ui.main.recyclerview.MainTabMemoAdapter
import com.mywidget.ui.base.BaseFragment
import com.mywidget.ui.main.MainFragmentViewModel
import javax.inject.Inject

class FragmentMemo : BaseFragment<MainFragmentDDayBinding>() {
    @Inject lateinit var mAdapter: MainTabMemoAdapter
    @Inject lateinit var factory: ViewModelProvider.Factory
    val viewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(MainFragmentViewModel::class.java)}
    override fun getLayout(): Int {
        return R.layout.main_fragment_d_day
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, parent, savedInstanceState)
        bindView()

        return binding.root
    }

    private fun bindView() {
        mAdapter = MainTabMemoAdapter()
        binding.fragmentRv.adapter = mAdapter
        binding.data = viewModel.memoData
        mAdapter.setViewModel(viewModel)

        selectCall()
    }

    private fun selectCall() {
        Thread {
            viewModel.selectMemo()
        }.start()
    }
}