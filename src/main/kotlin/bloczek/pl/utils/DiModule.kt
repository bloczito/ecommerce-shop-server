package bloczek.pl.utils

import bloczek.pl.repository.OrderElementsRepository
import bloczek.pl.repository.OrdersRepository
import bloczek.pl.repository.ProductRepository
import bloczek.pl.repository.UserRepository
import bloczek.pl.service.OrderElementsService
import bloczek.pl.service.OrdersService
import bloczek.pl.service.ProductService
import bloczek.pl.service.UserService
import org.koin.core.scope.get
import org.koin.dsl.module

val diModule = module {
    single { ProductService(get()) }
    single { ProductRepository() }

    single { UserService(get()) }
    single { UserRepository() }

    single { OrdersService(get(), get()) }
    single { OrdersRepository() }

    single { OrderElementsService(get()) }
    single { OrderElementsRepository() }

//    single { HelloServiceImpl(get()) } // get() Will resolve bloczek.pl.HelloRepository
//    single { HelloRepository() }
}
