package com.example.vbplusapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_settings.*
import com.google.gson.Gson
import com.example.vbplusapp.game.DataBaseManagerAndroid as DBManager

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //The database manager of this activity
        var dbMan:DBManager = DBManager(applicationContext)

        //To check if the MainActivity needs to reload the game with the applied settings
        var settingsApplied: Boolean = false

        //List of the settings element.
        //Order is given by the order the Game class receives the elements
        val settings: MutableList<EditText> = mutableListOf(winningSetsEdit, pointGapEdit,
            team1NameEdit, team2NameEdit, winningScoreEdit)

        fun packGameSettings(): MutableList<String> {

            //We need some kind of structure (object, ideally) to parse to JSON that holds the information
            //of the settings screen. We could also create a settings class
            var gameSettings: MutableList<String> = MutableList(0){ "" }

            //This ensures that the Game object wont be initialized with empty values
            for(editText in settings){
                if (editText.text.toString().isEmpty()) {
                    gameSettings.add(editText.hint.toString())
                }
                else{
                    gameSettings.add(editText.text.toString())
                }
            }

            return gameSettings
        }

        /*
            This will force the MainActivity to reload when the settingsActivity is closed
            It will use the settings applied here last
         */
        applySettingsButton.setOnClickListener {


            //The settings are saved to a settings file with templateName suffix
            val gameSettings: MutableList<String> = packGameSettings()
            var success: String = dbMan.saveGameSettings(templateName = "latest",
                                                         settingsJson = Gson().toJson(gameSettings)).toString()

            //For debugging only
            debugView.text = dbMan.lastExceptionThrown
        }

        //Simply go back to the MainActivity
        closeSettingsButton.setOnClickListener {
            finish()
        }

    }
}