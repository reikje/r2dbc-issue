package me.reik.r2dbc.repository

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource
import kotlin.system.measureTimeMillis

class TestPostgreSQLContainer(dockerImageName: String) : PostgreSQLContainer<TestPostgreSQLContainer>(dockerImageName)
class DisposableDataSource(val datasource: DataSource, val container: PostgreSQLContainer<TestPostgreSQLContainer>): DataSource by datasource, DisposableBean {
    override fun destroy() {
        println { "Stopping testcontainer" }
        container.stop()
    }
}

@Configuration
class TestDatabaseConfig: AbstractR2dbcConfiguration() {
    @Bean
    override fun connectionFactory(): ConnectionFactory {
        val disposableDataSource = disposableDataSource()
        val container = disposableDataSource.container
        return PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(container.containerIpAddress)
                .port(container.firstMappedPort)
                .database(container.databaseName)
                .username(container.username)
                .password(container.password)
                .build()
        )
    }

    @Bean
    fun disposableDataSource(): DisposableDataSource {
        val image = "postgres:11.1"
        val database = "dbname"
        val password = "postgres"
        val username = "postgres"

        val container: TestPostgreSQLContainer = TestPostgreSQLContainer("postgres:11.1")
            .withDatabaseName(database)
            .withUsername(username)
            .withPassword(password)

        println { "Starting testcontainer using image $image and database $database." }
        val executionTimeMillis = measureTimeMillis {  container.start() }

        println { "Testcontainer started in $executionTimeMillis ms." }

        val dataSource = PGSimpleDataSource()

        dataSource.serverName = container.containerIpAddress
        dataSource.portNumber = container.firstMappedPort
        dataSource.databaseName = database
        dataSource.user = username
        dataSource.password = password

        return DisposableDataSource(datasource = dataSource, container = container)
    }
}