package com.example.classscheduler.core.utils.validation

 abstract class Validator<T> {
     protected val validations: MutableMap<String, ValidationResult> = mutableMapOf();

     abstract fun validate(value: T): Map<String, ValidationResult>;
}