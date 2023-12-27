package com.launchdarkly.test.init

import android.app.Application
import androidx.lifecycle.Lifecycle
import com.launchdarkly.sdk.LDContext
import com.launchdarkly.sdk.android.LDClient
import com.launchdarkly.sdk.android.LDConfig
import com.launchdarkly.test.tracking.FFsTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class LdInitializer @Inject constructor(
    private val application: Application,
    private val featureFlagsTrackerFactory: FFsTracker.Factory,
) {
    suspend fun initialise(ldContext: LDContext, lifecycle: Lifecycle): LDClient {
        val ldConfig: LDConfig = LDConfig.Builder(LDConfig.Builder.AutoEnvAttributes.Enabled)
            .mobileKey("mobileKey")
            .build()
        val ldClient = getLDClient(application, ldConfig, ldContext)

        return ldClient.also {
            withContext(Dispatchers.Main) {
                trackFeatureFlagChanges(ldClient, lifecycle)
                // track changes in a similar manner to feature flag changes to identify the user
                // basically in onCreate register an observer to a flow and call client.identify
            }
        }
    }

    private fun trackFeatureFlagChanges(ldClient: LDClient, lifecycle: Lifecycle) {
        lifecycle.addObserver(featureFlagsTrackerFactory.create(ldClient))
    }

    private suspend fun getLDClient(app: Application, ldConfig: LDConfig, ldContext: LDContext): LDClient = withContext(Dispatchers.IO) {
        val ldClientInstance: LDClient
        val initDuration = measureTimeMillis {
            ldClientInstance = LDClient.init(app, ldConfig, ldContext, 30) // wait to sync up to 30s
        }
        // analytics tracking of the duration
        ldClientInstance
    }
}