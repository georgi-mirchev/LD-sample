package com.launchdarkly.test.tracking

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.launchdarkly.sdk.LDValue
import com.launchdarkly.sdk.LDValueType
import com.launchdarkly.sdk.android.LDClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FFsTracker @AssistedInject constructor(
    @Assisted private val client: LDClient,
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycleScope.launchWhenResumed {
            // track feature flags here
        }
    }


    private fun getFeatureFlagKeyValueMap(): Map<String, String> = client.allFlags().mapValues { it.value.getValue() }

    private fun LDValue.getValue(): String {
        return when (type) {
            LDValueType.BOOLEAN -> booleanValue().toString()
            LDValueType.STRING -> stringValue()
            LDValueType.NUMBER,
            LDValueType.ARRAY,
            LDValueType.OBJECT,
            -> toString()

            LDValueType.NULL,
            null,
            -> ""
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(client: LDClient): FFsTracker
    }
}
