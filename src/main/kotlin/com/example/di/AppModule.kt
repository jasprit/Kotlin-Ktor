package com.example.di

import com.example.features.auth.User
import com.example.repositories.UserRepositories
import com.example.repositories.UserRepositoriesImpl
import com.example.services.HashingService
import com.example.services.SHA256HashingService
import com.example.services.JwtTokenService
import com.example.services.TokenService
import com.example.services.AuthService
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.reactivestreams.KMongo

val appModule = module {

    single {
        val databaseUserName = System.getenv("MONGO_USR_NAME")
        val databasePass = System.getenv("MONGO_PWD")
        val databaseName = System.getenv("MONGO_DB_NAME")
        val connectionString =
            "mongodb+srv://$databaseUserName:$databasePass@cluster0.n3lt88l.mongodb.net/$databaseName?retryWrites=true&w=majority"
        val client = KMongo.createClient(connectionString)
        val database = client.getDatabase(databaseName)
        CoroutineDatabase(database)
    }

    // Provide CoroutineCollection<User> dependency
    single { get<CoroutineDatabase>().getCollection<User>("users") }

    single<HashingService> { SHA256HashingService() }
    single<TokenService> { JwtTokenService() }
    single<UserRepositories> { UserRepositoriesImpl(get()) }
    single<AuthService> { AuthService(get(), get(), get()) }
}