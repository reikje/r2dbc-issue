package me.reik.r2dbc.repository

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("games")
data class Game(
    @Id @Column("game_id") val id: String,
    @Column("game_title") val title: String
)