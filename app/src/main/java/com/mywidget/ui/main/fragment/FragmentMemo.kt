package com.mywidget.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import com.mywidget.R
import com.mywidget.databinding.DeleteConfirmDialogDDayBinding
import com.mywidget.databinding.MainFragmentDDayBinding
import com.mywidget.ui.base.BaseFragment
import com.mywidget.ui.main.MainFragmentViewModel
import com.mywidget.ui.main.recyclerview.MainTabMemoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class FragmentMemo : BaseFragment<MainFragmentDDayBinding>() {
    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var deleteDialogBinding: DeleteConfirmDialogDDayBinding
    @Inject lateinit var deleteDialog: Dialog
    val viewModel by activityViewModels<MainFragmentViewModel> { factory }

    override fun getLayout(): Int {
        return R.layout.main_fragment_d_day
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, parent, savedInstanceState)
        bind()

        return binding.root
    }

    private fun bind() {
        binding.data = viewModel.memoData
        binding.viewModel = viewModel

        binding.fragmentRv.apply {
            adapter = MainTabMemoAdapter(viewModel)
            val animator = itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }

        setObserve()
        selectCall()
        deleteDialog()
    }

    private fun setObserve() {
        viewModel.dDayDetail.observe(requireActivity(), Observer {
            (binding.fragmentRv.adapter as MainTabMemoAdapter).detail(it)
        })
    }

    private fun selectCall() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.selectMemo()
        }
    }

    private fun deleteDialog() {
        deleteDialog.setContentView(deleteDialogBinding.root)
        deleteDialogBinding.viewModel = viewModel
        viewModel.deleteDDayDialogVisibility.observe(requireActivity(), Observer {
            if (it) deleteDialog.show()
            else deleteDialog.dismiss()
        })
        viewModel.deleteDDayDialog.observe(requireActivity(), Observer {
            deleteDialogBinding.data = it
            viewModel.deleteDDayDialogVisibility(true)
        })
    }
}