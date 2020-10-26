package com.example.vbplusapp

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_menu_dialog.*
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat.startActivity
import com.example.vbplusapp.game.DatabaseManagerAndroid
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_menu_dialog.reloadButton

class MenuDialog(context: Context) : AppCompatDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_menu_dialog)

        var dbMan: DatabaseManagerAndroid = DatabaseManagerAndroid(context)
        var state: AppState = dbMan.loadState()


        settingsButton.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            dismiss()
            startActivity(context, intent,null)
        }

        gamesPlayedButton.setOnClickListener {
            /*
            val intent = Intent(context, GamesPlayedActivity::class.java)
            dismiss()
            startActivity(context, intent, null)
             */ dismiss()
            Toast.makeText(context, "not implemented yet", Toast.LENGTH_SHORT).show()
        }

        reloadButton.setOnClickListener {
            Toast.makeText(context, "Hold button to reload. \nThis will cause the current game to get lost",
                Toast.LENGTH_LONG).show()
        }
        reloadButton.setOnLongClickListener {
            Toast.makeText(context, "Loaded", Toast.LENGTH_SHORT).show()
            state.reloadRequested = true
            dbMan.saveState(state)
            return@setOnLongClickListener true
        }

    }

}

