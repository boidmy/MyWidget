package com.mywidget.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.MainFragmentFragment2Binding
import com.mywidget.view.MainActivity
import com.mywidget.viewModel.MainFragmentViewModel
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
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, parent, savedInstanceState)
        bindView()
        messagePop()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    private fun bindView() {
        binding.viewModel = viewModel
        viewModel.apply {
            messageLeft("뿡이")
            messageRight("콩이")
            leftMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                leftString.value = it[it.size-1]
            })
            rightMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                rightString.value = it[it.size-1]
            })
            Thread(Runnable {
                selectLoveDay()
            }).start()
        }

    }

    private fun messagePop() {
        viewModel.message.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
