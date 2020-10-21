package com.example.vbplusapp.game

import android.content.Context

class DatabaseManagerAndroid(_context: Context) : DatabaseManager {
    override val context: Context = _context
    override val path: String
        get() = context.filesDir.absolutePath

    override fun createDatabase(): DatabaseResponse {
        TODO("Not yet implemented")
    }

    override fun readGameDatabaseJSON(): DatabaseResponse {
        TODO("Not yet implemented")
    }

    override fun saveGameDatabaseJSON(gamesList: String): DatabaseResponse {
        TODO("Not yet implemented")
    }

    override fun getGame(index: Int): Game {
        TODO("Not yet implemented")
    }

    override fun addGame(game: Game): DatabaseResponse {
        TODO("Not yet implemented")
    }

    override fun getSettings(index: Int): GameSettings {
        TODO("Not yet implemented")
    }

    override fun addSettings(settings: GameSettings): DatabaseResponse {
        TODO("Not yet implemented")
    }
}