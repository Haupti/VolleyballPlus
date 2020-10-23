package com.example.vbplusapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.vbplusapp.game.DatabaseManagerAndroid
import com.example.vbplusapp.game.GameSettings
import kotlinx.android.synthetic.main.activity_settings.*
import com.google.gson.Gson

class SettingsActivity : AppCompatActivity() {
    private lateinit var dbMan: DatabaseManagerAndroid
    /*
        Saves the currently displayed settings to the settings database on position 0
        such that it will reload in main activity if reload is pressed
     */
    private fun saveTemporaryGameSettings(){
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
        dbMan.addSettings(settings)
    }

    /*
        Saves the currently displayed settings to the settings database at the end
        such that it can be choosen from the spinner later
     */
    fun saveNewSettingsPreset(settings: GameSettings){
        TODO()
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        //START init block
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        this.dbMan = DatabaseManagerAndroid(applicationContext)


        //Simply go back to the MainActivity
        closeSettingsButton.setOnClickListener {
            finish()
        }

        saveSettingsButton.setOnClickListener {
            saveTemporaryGameSettings()
        }

        newTemplateSwitch.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if(compoundButton.isChecked){
                templateNameRow.visibility = View.VISIBLE
            }
            else if(!compoundButton.isChecked){
                templateNameRow.visibility = View.INVISIBLE
                templateNameEdit.setText("")
            }
        }

    }
}
