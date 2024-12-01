package com.themrfill.iloveittest.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.themrfill.iloveittest.vm.ProductViewModel

val vmModule = module {
    viewModelOf(::ProductViewModel)
}