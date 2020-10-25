package com.example.vbplusapp

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.vbplusapp.game.DatabaseManagerAndroid
import com.example.vbplusapp.game.GameSettings
import kotlinx.android.synthetic.main.activity_settings.*
import com.google.gson.Gson

class SettingsActivity : AppCompatActivity() {
    private lateinit var dbMan: DatabaseManagerAndroid
    private lateinit var state: AppState
    /*
        Saves the currently displayed settings to the settings database on position 0
        such that it will reload in main activity if reload is pressed
     */
    private fun saveGameSettings(name : String){
        var settings: GameSettings = GameSettings()

        //it is sufficient to only set the values if there is something typed in
        //since the values hinted are the same as the default values of the settings object
        if ( winningScoreEdit.text.toString() != "") {
            settings.winScore = winningScoreEdit.text.toString().toInt()
        }
        if ( winningSetsEdit.text.toString() != "") {
            settings.winSets = winningSetsEdit.text.toString().toInt()
        }
        if ( team1NameEdit.text.toString() != "") {
            settings.team1Name = team1NameEdit.text.toString()
        }
        if ( team2NameEdit.text.toString() != "") {
            settings.team2Name = team2NameEdit.text.toString()
        }
        settings.templateName = name
        dbMan.addSettings(settings)
    }


    fun displaySettingsPreset(){
        var settings = dbMan.getSettings(state.settingsPresetSelected)
        team1NameEdit.text = Editable.Factory.getInstance().newEditable( settings.team1Name )
        team2NameEdit.text = Editable.Factory.getInstance().newEditable( settings.team2Name )
        winningScoreEdit.text = Editable.Factory.getInstance().newEditable(settings.winScore.toString())
        winningSetsEdit.text = Editable.Factory.getInstance().newEditable( settings.winSets.toString() )
    }

    private fun getSettingsNameList() : MutableList<String> {
        var settingsList: MutableList<GameSettings> = dbMan.getSettingsDatabase()
        var namesList: MutableList<String> = MutableList(0) {""}
        settingsList.filterNotNull().forEach {
            namesList.add(it.templateName)
        }
        return namesList
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        //START init block
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        this.dbMan = DatabaseManagerAndroid(applicationContext)
        this.state = dbMan.loadState()


        //Simply go back to the MainActivity
        closeSettingsButton.setOnClickListener {
            saveGameSettings("latest")
            finish()
        }
/*
        saveSettingsButton.setOnClickListener {
        }

        saveSettingsButton.setOnLongClickListener {
            return@setOnLongClickListener true
        }
*/

        /*
            Spinner for choosing game settings to load
         */
        //first the data that will be in the spinner. we need an adapter for that
        val settingsList: MutableList<String> = getSettingsNameList()
        val adapter = ArrayAdapter<String>(
            this,
            R.layout.custom_spinner,
            settingsList
        )
        settingsPresetSelectionSpinner.adapter = adapter

        settingsPresetSelectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                state.settingsPresetSelected = id.toInt()
                dbMan.saveState(state)
                displaySettingsPreset()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

}
