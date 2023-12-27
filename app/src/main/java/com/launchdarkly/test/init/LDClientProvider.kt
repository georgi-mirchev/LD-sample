package com.launchdarkly.test.init

import androidx.lifecycle.LifecycleOwner
import com.launchdarkly.sdk.android.LDClient
import com.launchdarkly.test.di.ProcessLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LDClientProvider @Inject constructor(
    private val getLdContextPayload: GetLdContextPayload,
    private val ldInitializer: LdInitializer,
    @ProcessLifecycle private val lifecycleOwner: LifecycleOwner,
) {
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val ldClientInstance: Deferred<LDClient> = coroutineScope.async(start = CoroutineStart.LAZY) {
        val ldContext = getLdContextPayload().first()
        ldInitializer.initialise(ldContext = ldContext, lifecycle = lifecycleOwner.lifecycle)
    }

    suspend fun getClient(): LDClient {
        return ldClientInstance.await()
    }
}