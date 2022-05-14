package com.iti.tiempo.network.utils

import com.iti.tiempo.base.network.DataState
import com.iti.tiempo.network.exceptions.MyException
import com.iti.tiempo.utils.NETWORK_TIMEOUT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.net.UnknownHostException

suspend fun <T> safeNetworkCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): Flow<DataState<T>> = flow {
    withTimeout(NETWORK_TIMEOUT) {
        val response = apiCall.invoke()
        emit(handleSuccess(response))
    }
}.onStart {
    emit(DataState.Loading)
}.catch {
    emit(handleError(it))
}.flowOn(dispatcher)

fun <T> handleSuccess(response: T): DataState<T> {
    return if (response != null) {
        DataState.Success(response)
    } else {
        DataState.Error(MyException.UnknownException)
    }
}

fun <T> handleError(it: Throwable): DataState<T> {
    it.printStackTrace()
    return when (it) {
        is TimeoutCancellationException -> {
            DataState.Error(MyException.TimeoutException)
        }

        is UnknownHostException -> {
            DataState.Error(MyException.ConnectionException)
        }

        is IOException -> {
            DataState.Error(MyException.UnknownException)
        }


        else -> {
            DataState.Error(MyException.UnknownException)
        }
    }
}
