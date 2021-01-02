package com.mywidget.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.MainFragmentLovedayBinding
import com.mywidget.ui.base.BaseFragment
import com.mywidget.ui.main.MainFragmentViewModel
import javax.inject.Inject

class FragmentLoveDay : BaseFragment<MainFragmentLovedayBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    val viewModel by activityViewModels<MainFragmentViewModel> { factory }

    override fun getLayout(): Int {
        return R.layout.main_fragment_loveday
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
