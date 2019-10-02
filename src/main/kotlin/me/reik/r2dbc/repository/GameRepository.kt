package me.reik.r2dbc.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.asType
import org.springframework.data.r2dbc.core.awaitOneOrNull
import org.springframework.stereotype.Repository

interface GameRepository {
    fun list(): Flow<Game>
    suspend fun find(id: String): Game?
}

@Repository
@FlowPreview
class PostgresGameRepository(
    private val client: DatabaseClient
): GameRepository {
    override fun list(): Flow<Game> {
        return client.select().from(Game::class.java).fetch().all().asFlow()
    }

    override suspend fun find(id: String): Game? {
        return client.execute("SELECT * FROM games WHERE game_id = \$1")
            .bind(0, id)
            .asType<Game>()
            .fetch()
            .awaitOneOrNull()
    }
}