package com.mywidget.view.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.adapter.MainTabRvAdapter
import com.mywidget.data.room.Memo
import com.mywidget.data.room.MemoDB
import com.mywidget.databinding.MainFragmentRvBinding
import com.mywidget.viewModel.MainViewModel

class FragmentMemo : Fragment() {
    private var mAdapter: MainTabRvAdapter? = null
    private var application: Application? = null
    lateinit var binding: MainFragmentRvBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = activity?.application
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.main_fragment_rv, parent, false)
        bindView()
        return binding.root
    }

    private fun bindView() {
        mAdapter = MainTabRvAdapter()
        mAdapter?.setViewModel(binding.viewModel)
        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        binding.lifecycleOwner = this
        binding.fragmentRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.fragmentRv.adapter = mAdapter
        binding.viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        binding.viewModel?.memoDB = MemoDB.getInstance(requireActivity().application)
        Thread(Runnable {
            selectCall()
        }).start()
    }

    companion object {
        @BindingAdapter("items")
        @JvmStatic
        fun adapter(recyclerView: RecyclerView?, data: MutableLiveData<List<Memo>>) {
            val adapter: MainTabRvAdapter = recyclerView?.adapter as MainTabRvAdapter
            adapter.setData(data.value)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewModel?.rxClear()
    }

    fun notifyCall(memo: String, date: String) {
        binding.viewModel?.insertMemo(memo, date)
    }

    private fun selectCall() {
        binding.viewModel?.selectMemo()
    }
}