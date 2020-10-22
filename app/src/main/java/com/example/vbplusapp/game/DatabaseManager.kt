package com.example.vbplusapp.game

import android.content.Context
import android.provider.ContactsContract

/*
Role of the DataBaseManager:

load settings and game "database" files
interpret the content and send back.

Should be able to handle all game package class objects
 */
interface DatabaseManager {

    val context : Context
    val path : String
    val gameDatabaseName : String
        get() = "VBP_Game.db"
    val settingsDatabaseName : String
        get() = "VBP_Settings.db"

    /*
        Creates a new database if there is none.
        Use the default filename. Do not give a filename, unless you know what you are doing.

        Returns:
        DatabaseResponse object containing error message, response text and response state
     */
    fun createDatabase():DatabaseResponse

    /*
        Reads the game database file to string.

        Returns:
        DatabaseResponse object with responseText being the JSON string of the file content
     */
    fun readGameDatabaseJSON() : DatabaseResponse

    /*
        Saves the game database received as JSON string

        Parameters:
        gamesList - game database, that is a list of games, as a JSON string

        Returns:
        DataBaseResponse object containing the status code of the success or failure
     */
    fun saveGameDatabaseJSON(gamesList: String) : DatabaseResponse

    /*
        Reads the settings database file to string.

        Returns:
        DatabaseResponse object with responseText being the JSON string of the file content
     */
    fun readSettingsDatabaseJSON() : DatabaseResponse

    /*
        Saves the settings database received as JSON string

        Parameters:
        settingsList - settings database, that is a list of settings, as a JSON string

        Returns:
        DataBaseResponse object containing the status code of the success or failure
     */
    fun saveSettingsDatabaseJSON(settingsList: String) : DatabaseResponse

    /*
        Returns the Game object of the game database at position index

        Parameters:
        index of the game to be send back. Index is position in list

        Returns:
        Game object on index position
     */
    fun getGame(index: Int) : Game

    /*
        Adds a Game object to the game database

        Parameters:
        Game object - the game to add to the database

        Returns:
        DatabaseResponse object - containing status code
     */
    fun addGame(game: Game) : DatabaseResponse

    /*
        Returns the GameSettings object in the game settings database at index

        Parameters:
        index - the index of the settings object to return

        Returns:
        DatabaseResponse object - containing status code
     */
    fun getSettings(index: Int) : GameSettings

    /*
        Adds a settings preset to the settings database

        Parameters:
        GameSettings object - the settings preset to save to database

        Returns:
        DatabaseResponse object - containing status code
     */
    fun addSettings(settings: GameSettings) : DatabaseResponse

    fun getSettingsDatabase() : MutableList<GameSettings>

}
