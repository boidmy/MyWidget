package com.mywidget.view.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.adapter.MainTabRvAdapter
import com.mywidget.adapter.UserAdapter
import com.mywidget.data.room.Memo
import com.mywidget.data.room.MemoDB
import com.mywidget.data.room.User
import com.mywidget.databinding.MainFragmentRvBinding
import com.mywidget.viewModel.MainFragmentRvViewModel
import com.mywidget.viewModel.MainViewModel
import kotlinx.android.synthetic.main.main_fragment_rv.view.*

class FragmentMemo : Fragment(), MainTabRvAdapter.callback {
    private var mAdapter: MainTabRvAdapter? = null
    private var application: Application? = null
    private var viewModel: MainViewModel? = null
    private var memoDb: MemoDB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = activity?.application
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: MainFragmentRvBinding = DataBindingUtil.inflate(inflater,
                R.layout.main_fragment_rv, parent, false)

        bindView(binding)
        return binding.root
    }

    private fun bindView(binding: MainFragmentRvBinding) {
        binding.lifecycleOwner = this
        mAdapter = MainTabRvAdapter()
        binding.fragmentRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.fragmentRv.adapter = mAdapter
        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application!!)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        memoDb = MemoDB.getInstance(application!!)
        viewModel?.memoDB = memoDb
        mAdapter?.setViewModel(viewModel)
        Thread(Runnable {
            selectCall()
        }).start()
    }

    companion object {
        private val ARG_TAB_POS = "tab_position"

        fun newInstance(position: Int): FragmentMemo {
            val fragment = FragmentMemo()
            val args = Bundle()
            args.putInt(ARG_TAB_POS, position)
            fragment.arguments = args
            return fragment
        }

        @BindingAdapter("items")
        @JvmStatic
        fun adapter(recyclerView: RecyclerView?, data: MutableLiveData<List<Memo>>) {
            val adapter: MainTabRvAdapter = recyclerView?.adapter as MainTabRvAdapter
            adapter.setData(data.value)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        memoDb?.destroyInstance()
    }

    override fun notifyCall(memo: String, date: String) {
        viewModel?.insertMemo(memo, date)
        selectCall()
    }

    private fun selectCall() {
        viewModel?.selectMemo()
    }
}