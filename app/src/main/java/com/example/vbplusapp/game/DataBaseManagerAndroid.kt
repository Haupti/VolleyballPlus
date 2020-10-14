package com.example.vbplusapp.game

import android.app.AlertDialog
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import kotlin.coroutines.coroutineContext

class DataBaseManagerAndroid: DatabaseManager {

    override val path: String

    var lastExceptionThrown: String = "none"

    constructor(path: String) {
        this.path = path
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
        Loads the settingsfile with the given file suffix templateName and returns it

        Returns:
        settings: Json - Json String of the settings in the settings file

        This DOES throw an error!
     */
    override fun loadGameSettings(templateName: String): String {
        var settingsFilePath = this.path + "settings_" + templateName + ".txt"
        return File(settingsFilePath).readText()
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
            //try to write to an excisting file
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
            return false
        }
        return true
    }
}


