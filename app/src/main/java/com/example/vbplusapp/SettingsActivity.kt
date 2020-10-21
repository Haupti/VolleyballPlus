package com.example.vbplusapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.vbplusapp.game.DatabaseManagerAndroid
import com.example.vbplusapp.game.GameSettings
import kotlinx.android.synthetic.main.activity_settings.*
import com.google.gson.Gson

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        //START init block
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        var dbMan: DatabaseManagerAndroid = DatabaseManagerAndroid(applicationContext)


        //Simply go back to the MainActivity
        closeSettingsButton.setOnClickListener {
            finish()
        }

        /*
            Saves the currently displayed settings to the settings database on position 0
            such that it will reload in main activity if reload is pressed
         */
        fun saveTemporaryGameSettings(){
            TODO()
        }

        /*
            Saves the currently displayed settings to the settings database at the end
            such that it can be choosen from the spinner later
         */
        fun saveNewSettingsPreset(settings: GameSettings){
            TODO()
        }

        /*
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

         */


    }
}
