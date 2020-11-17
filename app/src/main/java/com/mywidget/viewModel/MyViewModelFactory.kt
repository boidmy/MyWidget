package com.mywidget.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.mywidget.repository.MessageRepository
import java.lang.reflect.InvocationTargetException

class MyViewModelFactory(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            val messageRepository = MessageRepository(application)
            MainFragmentViewModel(messageRepository) as T
        } else if(modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(application) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}