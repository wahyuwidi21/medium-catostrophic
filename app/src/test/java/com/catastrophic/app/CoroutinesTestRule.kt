package com.catastrophic.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
class CoroutinesTestRule(
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
    private val testScope: TestCoroutineScope = TestCoroutineScope()
) : TestRule {
    override fun apply(base: Statement?, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            Dispatchers.setMain(testDispatcher)

            base?.evaluate()

            Dispatchers.resetMain()
            testScope.cleanupTestCoroutines()
        }
    }

    fun runBlocking(block: suspend TestCoroutineScope.() -> Unit) {
        testScope.runBlockingTest { block() }
    }
}