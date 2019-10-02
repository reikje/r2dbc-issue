package me.reik.r2dbc.repository

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.test.annotation.DirtiesContext

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PostgresGameRepositoryTest(@Autowired private val repository: GameRepository, @Autowired private val client: DatabaseClient) {

    @BeforeEach
    fun setup() {
        client.execute("INSERT INTO games VALUES('foo', 'Game Foo')").then().block()
    }

    @Test
    fun `should fetch games`() {
        val foo = runBlocking {
            repository.find("foo")
        }
        assertThat(foo).isEqualTo(Game(id = "foo", title = "Game Foo"))
    }
}