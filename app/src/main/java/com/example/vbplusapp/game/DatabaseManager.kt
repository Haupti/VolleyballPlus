package com.example.vbplusapp.game


interface DatabaseManager{

    val path: String
    /*
        Creates a new database if there is none.
        Use the default filename. Do not give a filename, unless you know what you are doing.

        If successful returns true
        If not returns false
     */
    fun createDatabase(filename: String = "VolleyballPlusDB.db"):Boolean


    /*
        Saves a given game (VolleyBallPlus vbplus.Game class object) to the database.
        Use the default filename. Do not give a filename, unless you know what you are doing.

        If successful returns true
        If not returns false
     */
    fun saveGame(game: Game, filename: String = "VolleyballPlusDB.db"):Boolean


    /*
        Loads all games saved in the database and returns them as a mutable list.
        Use the default filename. Do not give a filename, unless you know what you are doing.

        Returns mutable list of all games in database.
        Throws exception if it failed.
     */
    fun loadAllGames(filename: String = "VolleyballPlusDB.db"):MutableList<Game>


    /*
        Loads the game with the ID given and returns it (VolleyballPlus vbplus.Game class object).
        Use the default filename. Do not give a filename, unless you know what you are doing.

        Returns vbplus.Game object Throws exception if it failed
     */
    fun loadGame(gameId: Int, filename: String = "VolleyballPlusDB.db"): Game

    /*
        Loads the settings file changed in the settings activity. This returns the data to load
        a game with.

        Parameters:
        -Template name: String - can be the name of a saved settings template. If not given loads most recent

        Returns:
        -Game settings: String - returns file content as String
     */
    fun loadGameSettings(templateName: String = "latest"): String

    /*
        Saves the game Settings as a settings file with a given suffix.
        Doesn't throw error.

        Parameters:
        -suffix name: String - suffix of the save of the game settings. Scheme will be: settings_suffixname.csv

        Returns:
        -success: Boolean - True if it worked, false if it didn't for any reason
     */
    fun saveGameSettings(templateName: String = "latest", settingsJson: String): Boolean
}
