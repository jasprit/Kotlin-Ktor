package com.example.di

import com.example.feature.user.User
import com.example.feature.user.UserServices
import com.example.feature.user.UserServicesImpl
import com.example.util.security.HashingService
import com.example.util.security.SHA256HashingService
import com.example.util.token.JwtTokenService
import com.example.util.token.TokenService
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
    single<UserServices> { UserServicesImpl(get()) }
}