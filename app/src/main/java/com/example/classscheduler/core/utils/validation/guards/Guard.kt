package com.example.classscheduler.core.utils.validation.guards

interface GuardClause;

object Guard : GuardClause{
    val against: GuardClause = this;
}