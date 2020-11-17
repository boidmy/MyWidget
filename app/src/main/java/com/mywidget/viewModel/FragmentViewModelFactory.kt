package com.mywidget.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mywidget.repository.MessageRepository


class FragmentViewModelFactory(private val messageRepository: MessageRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainFragmentViewModel(messageRepository) as T
    }

}