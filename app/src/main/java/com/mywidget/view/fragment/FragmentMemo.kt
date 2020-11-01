package com.mywidget.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.adapter.MainTabRvAdapter
import com.mywidget.data.room.Memo
import com.mywidget.data.room.MemoDB
import com.mywidget.databinding.MainFragmentRvBinding
import com.mywidget.viewModel.MainFragmentViewModel

class FragmentMemo : BaseFragment<MainFragmentViewModel, MainFragmentRvBinding>() {
    private var mAdapter: MainTabRvAdapter? = null

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
        mAdapter = MainTabRvAdapter()
        mAdapter?.setViewModel(viewModel)
        binding.fragmentRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.fragmentRv.adapter = mAdapter
        binding.viewModel = viewModel
        Thread(Runnable {
            selectCall()
        }).start()
    }

    companion object {
        @BindingAdapter("items")
        @JvmStatic
        fun adapter(recyclerView: RecyclerView?, data: MutableLiveData<List<Memo>>?) {
            val adapter: MainTabRvAdapter = recyclerView?.adapter as MainTabRvAdapter
            data?.let {
                adapter.setData(data.value)
            }
        }
    }

    fun insertMemo(memo: String, date: String) {
        viewModel.insertMemo(memo, date)
    }

    private fun selectCall() {
        viewModel.selectMemo()
    }
}