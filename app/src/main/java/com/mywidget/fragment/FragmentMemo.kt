package com.mywidget.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.adapter.MainTabRvAdapter
import kotlinx.android.synthetic.main.main_fragment_rv.view.*

class FragmentMemo : Fragment(), MainTabRvAdapter.callback {
    private var mAdapter: MainTabRvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAdapter = MainTabRvAdapter()
        if(arguments != null) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.main_fragment_rv, parent, false)
        var mRv: RecyclerView = view.fragment_rv

        mRv.layoutManager = LinearLayoutManager(view.context)
        mRv.adapter = mAdapter

        mAdapter?.setData(MainApplication.memoSelect(), this)

        return view
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
    }

    override fun notifyCall() {
        mAdapter?.setData(MainApplication.memoSelect(), this)
    }
}