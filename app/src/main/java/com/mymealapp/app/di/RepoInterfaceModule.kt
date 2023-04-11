package com.mymealapp.app.di

import com.mymealapp.data.RepositoryImpl
import com.mymealapp.domain.RepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoInterfaceModule {
    @Binds
    abstract fun datasource(impl: RepositoryImpl): RepositoryInterface
}