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
import com.mywidget.data.room.Memo
import com.mywidget.databinding.DeleteConfirmDialogDDayBinding
import com.mywidget.databinding.MainFragmentDDayBinding
import com.mywidget.ui.base.BaseFragment
import com.mywidget.ui.main.MainFragmentViewModel
import com.mywidget.ui.main.recyclerview.MainTabMemoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import util.observe
import javax.inject.Inject


class FragmentMemo : BaseFragment<MainFragmentDDayBinding>() {
    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var deleteDialogBinding: DeleteConfirmDialogDDayBinding
    @Inject lateinit var deleteDialog: Dialog
    val viewModel by activityViewModels<MainFragmentViewModel> { factory }

    override fun getLayout() = R.layout.main_fragment_d_day

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, parent, savedInstanceState)
        bind()

        return binding.root
    }

    private fun bind() {
        binding.apply {
            data = viewModel.memoData
            vm = viewModel
            fragmentRv.adapter = MainTabMemoAdapter(viewModel)
            val animator = fragmentRv.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }

        setObserve()
        selectCall()
        deleteDialog()
    }

    private fun setObserve() {
        with(viewModel) {
            observe(dDayDetail, ::openDetailMemo)
            observe(deleteDDayDialogVisibility, ::visibilityDeleteDialog)
            observe(deleteDDayDialog, ::setDeleteDialogInformation)
        }
    }

    private fun openDetailMemo(index: Int) {
        (binding.fragmentRv.adapter as MainTabMemoAdapter).detail(index)
    }

    private fun setDeleteDialogInformation(data: Memo) {
        deleteDialogBinding.data = data
        viewModel.deleteDDayDialogVisibility(true)
    }

    private fun visibilityDeleteDialog(flag: Boolean) {
        if (flag) deleteDialog.show()
        else deleteDialog.dismiss()
    }

    private fun deleteDialog() {
        deleteDialog.setContentView(deleteDialogBinding.root)
        deleteDialogBinding.viewModel = viewModel
    }

    private fun selectCall() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.selectMemo()
        }
    }
}