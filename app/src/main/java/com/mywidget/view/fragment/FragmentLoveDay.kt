package com.mywidget.view.fragment

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.data.room.LoveDayDB
import com.mywidget.databinding.MainFragmentFragment2Binding
import com.mywidget.view.MainActivity
import com.mywidget.viewModel.MainFragmentViewModel
import com.mywidget.viewModel.MyViewModelFactory
import kotlinx.android.synthetic.main.memo_list_dialog.view.*
import kotlinx.android.synthetic.main.memo_list_dialog_item.view.*
import javax.inject.Inject

class FragmentLoveDay : BaseFragment<MainFragmentViewModel, MainFragmentFragment2Binding>() {

    @Inject lateinit var application: Application

    override fun getLayout(): Int {
        return R.layout.main_fragment_fragment2
    }

    override fun getViewModel(): Class<MainFragmentViewModel> {
        return MainFragmentViewModel::class.java
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, parent, savedInstanceState)
        bindView()
        messagePop()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).mainComponent.inject(this)
    }

    private fun bindView() {
        viewModel = ViewModelProvider(requireActivity(), MyViewModelFactory(application))
                .get(MainFragmentViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.messageLeft("뿡이")
        viewModel.messageRight("콩이")
        viewModel.leftMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.leftString.value = it[it.size-1]
        })
        viewModel.rightMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.rightString.value = it[it.size-1]
        })
        Thread(Runnable {
            viewModel.selectLoveDay()
        }).start()
    }

    private fun messagePop() {
        binding.viewModel?.message?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val view: View = layoutInflater.inflate(R.layout.memo_list_dialog, null)
            val popupWindow = PopupWindow(
                view,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            for (ds in it) {
                val listItem: View = layoutInflater.inflate(R.layout.memo_list_dialog_item, null)
                listItem.memo.text = ds.memo
                listItem.date.text = ds.date
                view.memo_list_container.addView(listItem)
            }
        })
    }
}
