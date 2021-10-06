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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentLoveDay : BaseFragment<MainFragmentLovedayBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    val viewModel by activityViewModels<MainFragmentViewModel> { factory }
    private val job = Job()

    override fun getLayout(): Int {
        return R.layout.main_fragment_loveday
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, parent, savedInstanceState)
        bindView()
        return binding.root
    }

    private fun bindView() {
        binding.viewModel = viewModel

        CoroutineScope(Dispatchers.Main+job).launch {
            viewModel.selectLoveDay()
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
