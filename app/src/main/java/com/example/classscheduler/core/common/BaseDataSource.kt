package com.example.classscheduler.core.common

import com.example.classscheduler.domain.primitives.Error

abstract class BaseDataSource(
    tag: String
){
    /**
    * Tag used within the logger
    * */
    protected val TAG: String = tag;

    /**
     * Converts an exception into a domain-specific [Error] object.
     * **/
    abstract fun  getErrorFromException(exception: Exception): Error;
}