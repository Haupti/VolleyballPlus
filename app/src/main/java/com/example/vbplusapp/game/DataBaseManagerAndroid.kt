package com.example.vbplusapp.game

import android.content.Context
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

class DataBaseManagerAndroid: DatabaseManager {

    override val path: String // Path to settings files and the games data base
    val context: Context
    override lateinit var response: DataBaseResponse //Must be set in the methods of the manager

    constructor(context: Context) {
        this.context = context
        this.path = this.context.filesDir.toString()
    }

    override fun createDatabase(filename: String): DataBaseResponse {
        TODO("Not yet implemented")
    }

    override fun saveGame(game: Game, filename: String): DataBaseResponse {
        TODO("Not yet implemented")
    }

    override fun loadAllGames(filename: String): DataBaseResponse {
        TODO("Not yet implemented")
    }

    override fun loadGame(gameId: Int, filename: String): DataBaseResponse {
        TODO("Not yet implemented")
    }

    /*
        Loads the settings file with the given file suffix templateName and returns it

        Returns:
        DataBaseResponse object containing response text, error message and state code
     */
    override fun loadGameSettings(templateName: String):DataBaseResponse {
        this.response = DataBaseResponse()
        try {
            var settingsFilePath = this.path + "settings_" + templateName + ".txt"
            this.response.responseText = File(settingsFilePath).readText()
            this.response.responseState = OK
        }catch (e: FileNotFoundException){
            this.response.responseState = FAILED
            this.response.errorMessage = e.message.toString()
        }
        return this.response
    }

    /*
        Saves the settings given as Json String to a file with the suffix templateName to the
        settings file.

        Returns:
        DataBaseResponse object
     */
    override fun saveGameSettings(templateName: String, settingsJson: String):DataBaseResponse {
        this.response= DataBaseResponse()
        try {
            var settingsFilePath = this.path + "settings_" + templateName + ".txt"
            //try to write to an existing file
            try {
                File(settingsFilePath).writeText(settingsJson)
            }
            //if not found, create one
            catch (fileNotFoundException: FileNotFoundException) {
                File(settingsFilePath).createNewFile()
                //and then write to it
                File(settingsFilePath).writeText(settingsJson)
            }
            this.response.responseState = OK
        }
        //if that also fails, other problem exists, return false as indicator of the failure
        catch (e: Exception) {
            // It failed if an Exception is raised
            this.response.responseText= e.message.toString()
            this.response.responseState = FAILED
            this.response.errorMessage = e.message.toString()
        }
        return this.response
    }
}


