package com.launchdarkly.test

import com.launchdarkly.test.init.LDClientProvider
import javax.inject.Inject

class IsLDFeatureFlagEnabled @Inject constructor(
    private val launchDarklyClientProvider: LDClientProvider,
) {

    suspend operator fun invoke(key: String, defaultValue: Boolean = false): Boolean {
        return launchDarklyClientProvider.getClient().boolVariation(key, defaultValue)
    }
}
