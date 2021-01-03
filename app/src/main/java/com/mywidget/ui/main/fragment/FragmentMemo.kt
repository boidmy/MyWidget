package com.mywidget.ui.main.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.DeleteConfirmDialogDDayBinding
import com.mywidget.databinding.MainFragmentDDayBinding
import com.mywidget.ui.base.BaseFragment
import com.mywidget.ui.main.MainFragmentViewModel
import com.mywidget.ui.main.recyclerview.MainTabMemoAdapter
import util.CalendarUtil
import util.CalendarUtil.memoDateFormat
import util.Util.dpToPx
import javax.inject.Inject


class FragmentMemo : BaseFragment<MainFragmentDDayBinding>() {
    @Inject lateinit var mAdapter: MainTabMemoAdapter
    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var deleteDialogBinding: DeleteConfirmDialogDDayBinding
    @Inject lateinit var deleteDialog: Dialog
    val viewModel by activityViewModels<MainFragmentViewModel> { factory }
    var saveDDayClickSeq = Int.MAX_VALUE

    override fun getLayout(): Int {
        return R.layout.main_fragment_d_day
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, parent, savedInstanceState)
        bindView()
        deleteDialog()

        return binding.root
    }

    private fun deleteDialog() {
        deleteDialog.setContentView(deleteDialogBinding.root)
        deleteDialogBinding.viewModel = viewModel
        viewModel.deleteDDayDialogVisibility.observe(requireActivity(), Observer {
            if (it) deleteDialog.show()
            else deleteDialog.dismiss()
        })
        viewModel.deleteDDayDialog.observe(requireActivity(), Observer {
            deleteDialogBinding.seq = it
            viewModel.deleteDDayDialogVisibility(true)
        })
    }

    private fun bindView() {
        binding.fragmentRv.adapter = mAdapter
        binding.data = viewModel.memoData
        binding.viewModel = viewModel
        mAdapter.setViewModel(viewModel)

        gone()

        viewModel.dDayDetail.observe(requireActivity(), Observer {
            binding.updateMemo = it
            if (binding.dDayDetailContainer.visibility == View.VISIBLE) {
                if (it.sequence == saveDDayClickSeq) {
                    gone()
                    return@Observer
                }
            }
            visible()
            saveDDayClickSeq = it.sequence ?: Int.MAX_VALUE
        })

        selectCall()
    }

    fun visible() {
        binding.dDayDetailContainer.isVisible = true
        binding.dDayDetailContainer.animate()
            .translationY(0f)
            .setDuration(300)
            .setListener(null)
        binding.fragmentRv.animate()
            .translationY(binding.dDayDetailContainer.height.toFloat())
            .setDuration(300)
            .setListener(null)
    }

    fun gone() {
        binding.dDayDetailContainer.animate()
            .translationY(-200.dpToPx.toFloat())
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    binding.dDayDetailContainer.isVisible = false
                }
            })
        binding.fragmentRv.animate()
            .translationY(0f)
            .setDuration(300)
            .setListener(null)
    }

    private fun selectCall() {
        Thread {
            viewModel.selectMemo()
        }.start()
    }
}