package com.example.vbplusapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import com.google.gson.Gson

class SettingsActivity :
    AppCompatActivity()
    {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //The database manager of this activity
        var dbMan: DBManager = DBManager(applicationContext)

        //To check if the MainActivity needs to reload the game with the applied settings
        var settingsApplied: Boolean = false

        //List of the settings element.
        //Order is given by the order the Game class receives the elements
        val settings: MutableList<EditText> = mutableListOf(
            winningSetsEdit, pointGapEdit,
            team1NameEdit, team2NameEdit, winningScoreEdit
        )

        fun packGameSettings(): MutableList<String> {

            //We need some kind of structure (object, ideally) to parse to JSON that holds the information
            //of the settings screen. We could also create a settings class
            var gameSettings: MutableList<String> = MutableList(0) { "" }

            //This ensures that the Game object wont be initialized with empty values
            for (editText in settings) {
                if (editText.text.toString().isEmpty()) {
                    gameSettings.add(editText.hint.toString())
                } else {
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
            var success: String = dbMan.saveGameSettings(
                templateName = "latest",
                settingsJson = Gson().toJson(gameSettings)
            ).toString()

            //For debugging only
            //debugView.text = dbMan.lastExceptionThrown
        }

        //Simply go back to the MainActivity
        closeSettingsButton.setOnClickListener {
            finish()
        }

        /*
            Definition of the settings template spinner
         */
        var settingsTemplatesList: MutableList<String> =
            Gson().fromJson(dbMan.getSettingsTemplatesJSON().responseText, Array<String>::class.java).toMutableList()
        val adapter = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            settingsTemplatesList
        )
        presetSelector.adapter = adapter

        presetSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Loads the settings selected and prints them to the display elements in the app
                var settingsList: MutableList<String> =
                    Gson().fromJson(
                        dbMan.loadGameSettings(parent?.getItemAtPosition(position).toString()).responseText,
                        Array<String>::class.java
                    ).toMutableList()
                winningSetsEdit.setText(settingsList[0])
                pointGapEdit.setText(settingsList[1])
                team1NameEdit.setText(settingsList[2])
                team2NameEdit.setText(settingsList[3])
                winningScoreEdit.setText(settingsList[4])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


    }
}
