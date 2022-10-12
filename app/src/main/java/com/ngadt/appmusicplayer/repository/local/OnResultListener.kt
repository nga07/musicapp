package com.ngadt.appmusicplayer.repository.local

import java.lang.Exception

interface OnResultListener<T> {
    fun onSuccess(data: T)
    fun onError()
}
