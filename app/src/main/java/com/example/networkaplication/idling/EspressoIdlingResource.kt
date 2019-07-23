package com.example.networkaplication.idling


import androidx.test.espresso.IdlingResource

object EspressoIdlingResource {
    private val RESOURCE = "GLOBAL"

    private val mCountingIdlingResource = SimpleCountingIdlingResource(RESOURCE)

    val idlingResource: IdlingResource
        get() = mCountingIdlingResource

    @JvmStatic
    fun increment() {
        mCountingIdlingResource.increment()
    }

    @JvmStatic
    fun decrement() {
        mCountingIdlingResource.decrement()
    }
}
