package com.launchdarkly.test.init

import com.launchdarkly.sdk.ContextKind
import com.launchdarkly.sdk.LDContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLdContextPayload @Inject constructor() {
    operator fun invoke(): Flow<LDContext> = flow {
        delay(5000L)
        // multiple async sources provide values here
        emit(createLDContext("Test"))
    }

    private fun createLDContext(testValue: String): LDContext {
        return LDContext.create(ContextKind.of(testValue), testValue)
    }
}