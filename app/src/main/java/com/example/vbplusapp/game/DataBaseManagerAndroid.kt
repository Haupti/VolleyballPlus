package com.example.vbplusapp.game

import android.content.Context
import android.provider.ContactsContract
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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
            var settingsFilePath = this.path + "settings_$templateName.json"
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
            var settingsFilePath = this.path + "settings_$templateName.json"
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

    override fun getSettingsTemplatesJSON(): DataBaseResponse {
        var settingsTemplateList: MutableList<String> =
            Gson().fromJson(
                File(path + "templates.json").readText(),
                Array<String>::class.java
            ).toMutableList()

        var response = DataBaseResponse()
        response.responseState = OK
        response.responseText = Gson().toJson(settingsTemplateList, Array<String>::class.java)

        return response
    }

    override fun addTemplateToList(templateName: String, settingsJson: String):DataBaseResponse {
        var templatesFilePath = path + "templates.json"
        var response = DataBaseResponse()
        lateinit var settingsList: MutableList<String>

        //first need to read the file content to add an entry
        try {
            settingsList =
                Gson().fromJson(
                    getSettingsTemplatesJSON().responseText,
                    Array<String>::class.java
                ).toMutableList()
            response.responseState = OK

            var isListed: Boolean = false
            for (sElement in settingsList){
                if(sElement == templateName){ isListed = true }
            }
            if(!isListed){
                 settingsList.add("settings_$templateName.json")
            }
        }
        //Only a file not found exception should be handled here
        //others result in the response to be set to FAILED state
        catch (e: Exception){
            when(e){
                is FileNotFoundException -> {
                    File(templatesFilePath).createNewFile()
                    settingsList.add("templates.json")
                    File(templatesFilePath).writeText(
                        Gson().toJson(
                            settingsList,
                            Array<String>::class.java
                        )
                    )
                    response.responseState = OK
                }
                else -> {
                    response.errorMessage = e.message.toString()
                    response.responseState = FAILED
                    return response
                }
            }

        }
        this.saveGameSettings(templateName,settingsJson)
        return response



    }
}


