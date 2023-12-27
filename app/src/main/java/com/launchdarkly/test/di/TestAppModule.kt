package com.launchdarkly.test.di

import androidx.lifecycle.LifecycleOwner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import java.util.Locale

@Module()
@InstallIn(SingletonComponent::class)
interface TestAppModule {

    companion object {

        // GlobalScope used here intentionally as a process scope
        @OptIn(DelicateCoroutinesApi::class)
        @Suppress("EXPERIMENTAL_API_USAGE")
        @Provides
        @ProcessCoroutineScope
        fun provideProcessCoroutineScope(): CoroutineScope = GlobalScope

        @Provides
        @ProcessLifecycle
        fun provideProcessLifecycleOwner(): LifecycleOwner {
            return androidx.lifecycle.ProcessLifecycleOwner.get()
        }



        @Provides
        fun provideLocale(): Locale {
            return Locale.getDefault()
        }
    }
}
