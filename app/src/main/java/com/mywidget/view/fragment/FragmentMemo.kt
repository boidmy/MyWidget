package com.mywidget.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mywidget.R
import com.mywidget.adapter.MainTabMemoAdapter
import com.mywidget.data.room.Memo
import com.mywidget.databinding.MainFragmentRvBinding
import com.mywidget.view.MainActivity
import com.mywidget.viewModel.MainFragmentViewModel
import javax.inject.Inject

class FragmentMemo : BaseFragment<MainFragmentRvBinding>() {
    //private var mAdapter: MainTabMemoAdapter? = null

    @Inject lateinit var mAdapter: MainTabMemoAdapter
    @Inject lateinit var factory: ViewModelProvider.NewInstanceFactory
    private var viewModel: MainFragmentViewModel? = null
    override fun getLayout(): Int {
        return R.layout.main_fragment_rv
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, parent, savedInstanceState)
        bindView()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).mainComponent.inject(this)
    }

    private fun bindView() {
        mAdapter = MainTabMemoAdapter()
        binding.fragmentRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.fragmentRv.adapter = mAdapter
        viewModel = ViewModelProvider(requireActivity(), factory)
                .get(MainFragmentViewModel::class.java)
        binding.data = viewModel?.memoData
        mAdapter.setViewModel(viewModel)
        selectCall()
    }

    private fun selectCall() {
        Thread(Runnable {
            viewModel?.selectMemo()
        }).start()
    }
}