package com.ryl.securedcamera.utils.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultDispatcherProvider :
    DispatcherProvider {

    override val main : CoroutineDispatcher = Dispatchers.Main
    override val default : CoroutineDispatcher = Dispatchers.Default
    override val io : CoroutineDispatcher = Dispatchers.IO
    override val unconfined : CoroutineDispatcher = Dispatchers.Unconfined
}