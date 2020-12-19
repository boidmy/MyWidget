package com.mywidget.ui.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.MainFragmentFragment2Binding
import com.mywidget.ui.base.BaseFragment
import com.mywidget.ui.main.MainFragmentViewModel
import kotlinx.android.synthetic.main.memo_list_dialog.view.*
import kotlinx.android.synthetic.main.memo_list_dialog_item.view.*
import javax.inject.Inject

class FragmentLoveDay : BaseFragment<MainFragmentFragment2Binding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    val viewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(requireActivity(), factory).get(MainFragmentViewModel::class.java)}

    override fun getLayout(): Int {
        return R.layout.main_fragment_fragment2
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, parent, savedInstanceState)
        bindView()
        favoritesObserver()
        return binding.root
    }

    private fun bindView() {
        binding.viewModel = viewModel
        favoritesResetData()

        Thread {
            viewModel.selectLoveDay()
        }.start()
    }

    private fun favoritesResetData() {
        viewModel.favoritesExistenceMyFriend()
        viewModel.favoritesResetMe()
        viewModel.favoritesResetFriend()
    }

    private fun favoritesObserver() {
        viewModel.myId.observe(viewLifecycleOwner, Observer {
            viewModel.favoritesExistenceMyFriend()
        })
        viewModel.favoritesExistenceMyFriend.observe(viewLifecycleOwner, Observer {
            viewModel.favoritesSelectMessage(it.email)
        })
    }
}
