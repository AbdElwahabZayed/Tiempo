package com.iti.tiempo.network.exceptions

sealed class MyException : Exception() {
    object UnknownException : MyException()
    object ServerException : MyException()
    object NotFoundException : MyException()
    object TimeoutException : MyException()
    object ConnectionException : MyException()
    object NoGPSPermission :MyException()
    object GPSIsDisabled :MyException()
    object AuthorizationException : MyException()
    data class CustomException(val msg: String) : MyException()
    data class NeedActiveException(val msg: String) : MyException()
}