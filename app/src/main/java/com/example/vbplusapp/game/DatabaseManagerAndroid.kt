package com.example.vbplusapp.game

import android.content.Context
import com.example.vbplusapp.AppState
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

class DatabaseManagerAndroid(_context: Context) : DatabaseManager {
    override val context: Context = _context
    override val path: String
        get() = context.filesDir.absolutePath

    /*
        Tries to read the database files. If that fails with
        FileNotFound exception it creates the files. Any other
        Exception and the function will simply report the fail
     */
    override fun create(): DatabaseResponse {
        var response : DatabaseResponse = DatabaseResponse()
        try {
            try {
                File(path + gameDatabaseName).readText()
            } catch (ex: FileNotFoundException) { // in case of FileNotFound exception
                File(path + gameDatabaseName).createNewFile()
            }
            try {
                File(path + settingsDatabaseName).readText()
            } catch (ex: FileNotFoundException) {
                File(path + settingsDatabaseName).createNewFile()
                File(path + settingsDatabaseName).writeText(Gson().toJson(mutableListOf(GameSettings())))
                // Here several presets are defined, that are useful
                addSettings(GameSettings("Team 1", "Team 2", 25, 3, "FIVB Indoor"))
                addSettings(GameSettings("Team 1", "Team 2", 25, 1, "Casual Indoor"))
                addSettings(GameSettings("Team 1", "Team 2", 21, 2, "Beach Standard"))
                addSettings(GameSettings("Team 1", "Team 2", 21, 1, "Casual Beach"))
                addSettings(GameSettings("Team 1", "Team 2", 15, 1, "Casual Beach Short"))
            }
        }catch (ex: Exception){ // in case of other exception
            response.responseState = FAILED
            response.errorMessage = ex.message.toString()
            return response
        }

        response.responseState = OK
        return response
    }


    override fun readGameDatabaseJSON(): DatabaseResponse {
        var response : DatabaseResponse = DatabaseResponse()
        try {
            response.responseText  = File(path + gameDatabaseName).readText()
            response.responseState = OK
        }
        catch (ex: Exception){
            response.responseState = FAILED
            response.errorMessage  = ex.message.toString()
        }
        return response
    }

    override fun saveGameDatabaseJSON(gamesList: String): DatabaseResponse {
        var response: DatabaseResponse = DatabaseResponse()
        try {
            File(path + gameDatabaseName).writeText(gamesList)
            response.responseState = OK
        }
        catch (ex: Exception){
            response.responseState = FAILED
            response.errorMessage  = ex.message.toString()
        }
        return response
    }

    override fun readSettingsDatabaseJSON(): DatabaseResponse {
        var response : DatabaseResponse = DatabaseResponse()
        try {
            response.responseText  = File(path + settingsDatabaseName).readText()
            response.responseState = OK
        }
        catch (ex: Exception){
            response.responseState = FAILED
            response.errorMessage  = ex.message.toString()
        }
        return response
    }

    override fun saveSettingsDatabaseJSON(settingsList: String): DatabaseResponse {
        var response: DatabaseResponse = DatabaseResponse()
        try {
            File(path + settingsDatabaseName).writeText(settingsList)
            response.responseState = OK
        }
        catch (ex: Exception){
            response.responseState = FAILED
            response.errorMessage  = ex.message.toString()
        }
        return response
    }



    @Throws(Exception::class)
    override fun getSettings(index: Int): GameSettings {
        var response: DatabaseResponse = readSettingsDatabaseJSON()
        if(response.responseState == FAILED){
            throw Exception("ERROR: reading file failed")
        }
        var settingsDB: MutableList<GameSettings>
                = Gson().fromJson(
                    readSettingsDatabaseJSON().responseText,
                    Array<GameSettings>::class.java
                ).toMutableList()

        if(index == -1){ return settingsDB.last() }
        return settingsDB[index]
    }

    override fun addSettings(settings: GameSettings): DatabaseResponse {
        var response: DatabaseResponse = readSettingsDatabaseJSON()
        var toSendResponse = DatabaseResponse()
        var settingsDB: MutableList<GameSettings>

        if(response.responseState == FAILED){
            toSendResponse.responseState == FAILED
            toSendResponse.errorMessage = response.errorMessage
            return toSendResponse
        }
        else {
            settingsDB = Gson().fromJson(
                readSettingsDatabaseJSON().responseText,
                Array<GameSettings>::class.java
            ).toMutableList()
        }

        if ( settings.templateName == "latest"){
            settings.templateName = "Latest"
            settingsDB[0] = settings
        }
        else {
            settingsDB.add(settings)
        }
        var settingsJSON: String = Gson().toJson(settingsDB)


        return saveSettingsDatabaseJSON(settingsJSON)
    }

    override fun getSettingsDatabase(): MutableList<GameSettings> {
        return Gson().fromJson(
            this.readSettingsDatabaseJSON().responseText,
            Array<GameSettings>::class.java
        ).toMutableList()
    }

    override fun saveState(state: AppState) : DatabaseResponse {
        var response = DatabaseResponse()
        try {
            File(path + stateFileName).writeText(
                Gson().toJson(state, AppState::class.java)
            )
        }
        catch (ex: FileNotFoundException){
            File(path + stateFileName).createNewFile()
            File(path + stateFileName).writeText(
                Gson().toJson(state, AppState::class.java)
            )
        }
        response.responseState = OK
        return response
    }

    override fun loadState() : AppState {
        try {
            return Gson().fromJson(
                File(path + stateFileName).readText(),
                AppState::class.java
            )
        }
        catch (ex: FileNotFoundException){
            saveState(AppState())
        }
        return Gson().fromJson(
            File(path + stateFileName).readText(),
            AppState::class.java
        )
    }

    override fun getGame(index: Int): Game {
        TODO("Not yet implemented")
    }

    override fun addGame(game: Game): DatabaseResponse {
        TODO("Not yet implemented")
    }
}