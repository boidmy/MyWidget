package com.mywidget.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {

    var data: MutableLiveData<String> = MutableLiveData()
}