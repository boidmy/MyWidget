package com.mywidget.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mywidget.R
import com.mywidget.adapter.MainTabMemoAdapter
import com.mywidget.databinding.MainFragmentRvBinding
import com.mywidget.repository.MessageRepository
import com.mywidget.view.MainActivity
import com.mywidget.viewModel.MainFragmentViewModel
import javax.inject.Inject

class FragmentMemo : BaseFragment<MainFragmentRvBinding>() {
    @Inject lateinit var mAdapter: MainTabMemoAdapter
    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var messageRepository: MessageRepository
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).mainComponent.inject(this)
    }

    private fun bindView() {
        mAdapter = MainTabMemoAdapter()
        binding.fragmentRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.fragmentRv.adapter = mAdapter
        binding.data = viewModel.memoData
        mAdapter.setViewModel(viewModel)

        selectCall()
    }

    private fun selectCall() {
        Thread(Runnable {
            viewModel.selectMemo()
        }).start()
    }
}