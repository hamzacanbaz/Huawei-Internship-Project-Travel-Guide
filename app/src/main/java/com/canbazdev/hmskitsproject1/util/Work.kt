package com.canbazdev.hmskitsproject1.util

/*
*   Created by hamzacanbaz on 7/21/2022
*/
class Work<T> {
    private var successListener: ((result: T) -> Unit)? = null
    private var failureListener: ((error: Exception) -> Unit)? = null
    private var canceledListener: (() -> Unit)? = null

    fun onSuccess(result: T) {
        successListener?.run {
            this(result)
            successListener = null
        }
    }

    fun onFailure(error: Exception) {
        failureListener?.run {
            this(error)
            failureListener = null
        }
    }

    fun onCanceled() {
        canceledListener?.run {
            this()
            canceledListener = null
        }
    }

    fun addOnSuccessListener(listener: (result: T) -> Unit): Work<T> {
        successListener = listener
        return this
    }

    fun addOnFailureListener(listener: (error: Exception) -> Unit): Work<T> {
        failureListener = listener
        return this
    }

    fun addOnCanceledListener(listener: () -> Unit): Work<T> {
        canceledListener = listener
        return this
    }
}