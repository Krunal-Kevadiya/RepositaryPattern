package com.example.ownrepositarypatternsample.utils.extension

import androidx.lifecycle.LifecycleOwner
import org.koin.androidx.scope.bindScope
import org.koin.androidx.viewmodel.ext.android.getKoin
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.scope.Scope
import org.koin.ext.getFullName

inline fun <reified T: Any> LifecycleOwner.currentScope() : Scope {
    val scopeId = T::class.getFullName() + "@" + System.identityHashCode(this)
    return getKoin().getScopeOrNull(scopeId) ?: createAndBindScope(scopeId, TypeQualifier(T::class))
}

fun LifecycleOwner.createAndBindScope(scopeId: String, qualifier: Qualifier): Scope {
    val scope = getKoin().createScope(scopeId, qualifier)
    bindScope(scope)
    return scope
}