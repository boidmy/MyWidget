package com.mywidget.di.module

import androidx.lifecycle.ViewModel
import com.mywidget.ui.widgetlist.WidgetListViewModel
import com.mywidget.di.custom.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UserViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WidgetListViewModel::class)
    internal abstract fun bindViewModel(viewModel: WidgetListViewModel): ViewModel
}