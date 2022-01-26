package com.minimal.contact.di

import android.app.Application
import com.minimal.contact.util.ContactHelper
import com.minimal.contact.util.ContactQuery
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContactHelper(application: Application): ContactHelper {
        return ContactHelper(
            contentResolver = application.contentResolver,
            contactQuery = ContactQuery()
        )
    }
}