package com.example.vbplusapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import com.google.gson.Gson
import com.example.vbplusapp.game.DataBaseManagerAndroid as DBManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var dbMan:DBManager = DBManager(" ")

        applySettingsButton.setOnClickListener {
            var gameSettings: MutableList<String> = MutableList(0){ "" }
            gameSettings.add(team1NameEdit.text.toString())
            var success: String = dbMan.saveGameSettings(templateName = "latest", settingsJson = Gson().toJson(gameSettings)).toString()

            debugView.text = dbMan.lastExceptionThrown
        }

        closeSettingsButton.setOnClickListener {
            finish()
        }

    }
}