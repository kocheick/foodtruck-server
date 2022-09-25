package com.shevapro.data.repository.db

import com.shevapro.data.models.Places
import com.shevapro.data.models.PlacesHours
import com.shevapro.data.models.Users
import com.shevapro.data.models.UsersPlaces
import com.shevapro.toUUID
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object DatabaseFactory {

    fun init() {
        val dataSource = hikari()

//        val flyway = Flyway.configure().dataSource(dataSource).baselineOnMigrate(true).load()

        Database.connect(dataSource)

        transaction {
//            resetTables()
//            flyway.migrate()
            SchemaUtils.createMissingTablesAndColumns(Places,Users,UsersPlaces,PlacesHours)

        }

    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = System.getenv("JDBC_DATABASE_URL")
            maximumPoolSize = 6
            password = System.getenv("PASSWORD")
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        return HikariDataSource(config)
    }

    fun resetTables(){
        transaction { SchemaUtils.drop(Places,Users,UsersPlaces,PlacesHours) }
    }

    fun recreateTables(){
        transaction { SchemaUtils.create(Places,Users,UsersPlaces,PlacesHours) }
    }


}    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction {
//            addLogger(StdOutSqlLogger)
            block()
        }
    }