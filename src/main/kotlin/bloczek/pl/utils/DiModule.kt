package bloczek.pl.utils

import bloczek.pl.repository.ProductRepository
import bloczek.pl.repository.UserRepository
import bloczek.pl.service.ProductService
import bloczek.pl.service.UserService
import org.koin.dsl.module

val diModule = module {
    single { ProductService(get()) }
    single { ProductRepository() }

    single { UserService(get()) }
    single { UserRepository() }

//    single { HelloServiceImpl(get()) } // get() Will resolve bloczek.pl.HelloRepository
//    single { HelloRepository() }
}
