package com.example.vbplusapp.game

import android.provider.ContactsContract


interface DatabaseManager{

    var response:DataBaseResponse
    val path: String
    /*
        Creates a new database if there is none.
        Use the default filename. Do not give a filename, unless you know what you are doing.

        Returns:
        DataBaseResponse object containing error message, response text and response state
     */
    fun createDatabase(filename: String = "VolleyballPlusDB.db"):DataBaseResponse


    /*
        Saves a given game (VolleyBallPlus vbplus.Game class object) to the database.
        Use the default filename. Do not give a filename, unless you know what you are doing.

        Returns:
        DataBaseResponse object containing error message, response text and response state
     */
    fun saveGame(game: Game, filename: String = "VolleyballPlusDB.db"):DataBaseResponse


    /*
        Loads all games saved in the database and returns them as a mutable list.
        Use the default filename. Do not give a filename, unless you know what you are doing.

        Returns:
        DataBaseResponse object containing error message, response text and response state
     */
    fun loadAllGames(filename: String = "VolleyballPlusDB.db"):DataBaseResponse


    /*
        Loads the game with the ID given and returns it (VolleyballPlus vbplus.Game class object).
        Use the default filename. Do not give a filename, unless you know what you are doing.

        Returns:
        DataBaseResponse object containing error message, response text and response state
     */
    fun loadGame(gameId: Int, filename: String = "VolleyballPlusDB.db"):DataBaseResponse

    /*
        Loads the settings file changed in the settings activity. This returns the data to load
        a game with.

        Parameters:
        -Template name: String - can be the name of a saved settings template. If not given loads most recent

        Returns:
        DataBaseResponse object containing error message, response text and response state
     */
    fun loadGameSettings(templateName: String = "latest"):DataBaseResponse

    /*
        Saves the game Settings as a settings file with a given suffix.
        Doesn't throw error.

        Parameters:
        -suffix name: String - suffix of the save of the game settings. Scheme will be: settings_suffixname.csv

        Returns:
        DataBaseResponse object containing error message, response text and response state
     */
    fun saveGameSettings(templateName: String = "latest", settingsJson: String):DataBaseResponse
}
