package com.example.currencies.di

import android.content.Context
import androidx.room.Room
import com.example.currencies.data.db.CurrencyDataBase
import com.example.currencies.other.Constants.LIST_OF_CURRENCIES
import com.example.currencies.other.Constants.MY_LIST_DATABASE
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(ActivityComponent::class)
class di {

    @ActivityScoped
    @Provides
    @Named("MyDatabase")
    fun provideMyShoppingDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, CurrencyDataBase::class.java, MY_LIST_DATABASE)

    @ActivityScoped
    @Provides
    @Named("currency_database")
    fun provideShoppingDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, CurrencyDataBase::class.java, LIST_OF_CURRENCIES)

}