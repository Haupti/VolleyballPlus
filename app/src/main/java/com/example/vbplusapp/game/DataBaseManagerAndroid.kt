package com.example.vbplusapp.game

import android.app.AlertDialog
import android.content.Context
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import kotlin.coroutines.coroutineContext

class DataBaseManagerAndroid: DatabaseManager {

    //String codes
    val FILE_LOADING_FAILED_STRG: String = "FLF"
    val FILE_LOADING_FAILED_INT: Int = 0
    val FILE_LOADING_FAILED: Boolean = false


    override val path: String
    val context: Context

    var lastExceptionThrown: String = "none"

    constructor(context: Context) {
        this.context = context
        this.path = this.context.filesDir.toString()
    }

    override fun createDatabase(filename: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun saveGame(game: Game, filename: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun loadAllGames(filename: String): MutableList<Game> {
        TODO("Not yet implemented")
    }

    override fun loadGame(gameId: Int, filename: String): Game {
        TODO("Not yet implemented")
    }

    /*
        Loads the settings file with the given file suffix templateName and returns it

        Returns:
        settings: Json - Json String of the settings in the settings file

        Does not throw error, if loading failed it returns the fail code
     */
    override fun loadGameSettings(templateName: String): String {
        try {
            var settingsFilePath = this.path + "settings_" + templateName + ".txt"
            return File(settingsFilePath).readText()
        }catch (e: FileNotFoundException){
            return FILE_LOADING_FAILED_STRG
        }
    }

    /*
        Saves the settings given as Json String to a file with the suffix templateName to the
        settings file.

        Returns:
        True if successfull
        False if failed

        Does not throw error!
     */
    override fun saveGameSettings(templateName: String, settingsJson: String): Boolean {
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
        }
        //if that also fails, other problem exists, return false as indicator of the failure
        catch (e: Exception) {
            // It failed if an Exception is raised
            this.lastExceptionThrown = e.message.toString()
            return FILE_LOADING_FAILED
        }
        return true
    }
}


