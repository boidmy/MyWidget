package com.mywidget.ui.main.fragment

import android.view.LayoutInflater
import com.mywidget.databinding.DeleteConfirmDialogDDayBinding
import com.mywidget.ui.main.recyclerview.MainTabMemoAdapter
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    @Provides
    fun provideDeleteDialogBinding(fragment: FragmentMemo): DeleteConfirmDialogDDayBinding {
        return DeleteConfirmDialogDDayBinding.inflate(LayoutInflater.from(fragment.requireContext()))
    }
}