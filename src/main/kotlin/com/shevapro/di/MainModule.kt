package com.shevapro.di

import com.shevapro.data.repository.places.PlacesRepoImpl
import com.shevapro.data.repository.places.PlacesRepository
import com.shevapro.data.repository.users.UsersReposiroty
import com.shevapro.data.repository.users.UsersReposirotyImpl
import com.shevapro.services.JWTService
import com.shevapro.services.PlacesService
import com.shevapro.services.UserService
import org.koin.dsl.module

val mainModule = module {

    single { JWTService() }

    single<PlacesRepository> { PlacesRepoImpl() }
    single<UsersReposiroty> { UsersReposirotyImpl() }

    single { UserService(get()) }
    single { PlacesService(get()) }

}