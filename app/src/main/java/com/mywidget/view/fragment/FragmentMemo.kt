package com.mywidget.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.adapter.MainTabMemoAdapter
import com.mywidget.data.room.Memo
import com.mywidget.databinding.MainFragmentRvBinding
import com.mywidget.viewModel.MainFragmentViewModel

class FragmentMemo : BaseFragment<MainFragmentViewModel, MainFragmentRvBinding>() {
    private var mAdapter: MainTabMemoAdapter? = null

    override fun getLayout(): Int {
        return R.layout.main_fragment_rv
    }

    override fun getViewModel(): Class<MainFragmentViewModel> {
        return MainFragmentViewModel::class.java
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
        viewModel = ViewModelProvider(requireActivity())
                .get(MainFragmentViewModel::class.java)
        binding.data = viewModel.memoData
        mAdapter?.setViewModel(viewModel)
        selectCall()
    }

    companion object {
        @BindingAdapter("items")
        @JvmStatic
        fun adapter(recyclerView: RecyclerView?, data: MutableLiveData<List<Memo>>?) {
            val adapter: MainTabMemoAdapter = recyclerView?.adapter as MainTabMemoAdapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun selectCall() {
        Thread(Runnable {
            viewModel.selectMemo()
        }).start()
    }
}