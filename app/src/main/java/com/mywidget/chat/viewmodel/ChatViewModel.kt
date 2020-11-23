package com.mywidget.chat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.chat.ChatDataModel
import com.mywidget.chat.repository.ChatRepository
import javax.inject.Inject

class ChatViewModel @Inject constructor(val repository: ChatRepository) : ViewModel() {

    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()

    fun selectChat(id: String, key: String) {
        repository.selectChat(id, key)
    }
}