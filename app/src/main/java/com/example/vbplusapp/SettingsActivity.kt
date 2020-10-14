package com.example.vbplusapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        /*
            This will force the MainActivity to reload when the settingsActivity is closed
            It will use the settings applied here last
         */
        applySettingsButton.setOnClickListener {

            //We need some kind of structure (object, ideally) to parse to JSON that holds the information
            //of the settings screen. We could also create a settings class
            var gameSettings: MutableList<String> = MutableList(0){ "" }
            gameSettings.add(winningSetsEdit.text.toString())
            gameSettings.add(pointGapEdit.text.toString())
            gameSettings.add(team1NameEdit.text.toString())
            gameSettings.add(team2NameEdit.text.toString())
            gameSettings.add(winningScoreEdit.text.toString())

            //The settings are saved to a settings file with templateName suffix
            var success: String = dbMan.saveGameSettings(templateName = "latest",
                                                         settingsJson = Gson().toJson(gameSettings)).toString()

            //For debugging only
            debugView.text = dbMan.lastExceptionThrown
        }

        //Simply go back to the MainActivity
        closeSettingsButton.setOnClickListener {
            //If there are no settings applied in this activity this is false, the main activity won't need to reload
            intent.putExtra("settingsApplied", settingsApplied)
            setResult(RESULT_OK,intent)
            finish()
        }

    }
}