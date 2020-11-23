package com.mywidget.chat.waiting

import androidx.lifecycle.ViewModel
import com.mywidget.chat.waiting.viewmodel.WatingRoomViewModel
import com.mywidget.viewModel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WatingViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WatingRoomViewModel::class)
    internal abstract fun bindViewModel(viewModel: WatingRoomViewModel): ViewModel
}