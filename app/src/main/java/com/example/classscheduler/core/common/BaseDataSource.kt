package com.example.classscheduler.core.common

import com.example.classscheduler.domain.primitives.Error

abstract class BaseDataSource(
    tag: String
){
    protected val TAG: String = tag;

    abstract fun  getErrorFromException(exception: Exception): Error;
}