package com.mywidget.extension

import android.app.Dialog
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import com.mywidget.databinding.ChatInviteUserAddBinding
import com.mywidget.databinding.MemoDialogBinding
import com.mywidget.ui.chat.ChatViewModel
import com.mywidget.ui.main.MainFragmentViewModel

@BindingAdapter("memoTxt", "dateTxt", "viewModel")
fun memoOnclick(button: Button, memo: EditText, date: TextView, viewModel: MainFragmentViewModel) {
    button.setOnClickListener {
        AlertDialog.Builder(button.context)
            .setTitle("아싸~")
            .setMessage("♥입력됐대용♥")
            .setIcon(android.R.drawable.ic_menu_save)
            .setPositiveButton("yes") { _, _ ->
                date.tag?.let {
                    viewModel.insertMemo(memo.text.toString(), it.toString())
                    Toast.makeText(button.context, "저장했대요!!", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(button.context, "날짜를 선택해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(
                android.R.string.no
            ) { _, _ ->
                // 취소시 처리 로직
                Toast.makeText(button.context, "취소했대요ㅠㅠ.", Toast.LENGTH_SHORT).show()
            }.show()
        viewModel.dialogVisible.value = false
    }
}


@BindingAdapter("inviteUser")
fun inviteUserOnClick(imageView: ImageView, viewModel: ChatViewModel) {
    val binding = ChatInviteUserAddBinding.inflate(LayoutInflater.from(imageView.context))
    imageView.setOnClickListener {
        val dialog = Dialog(imageView.context)
        dialog.setContentView(binding.root)
        dialog.show()
    }
}