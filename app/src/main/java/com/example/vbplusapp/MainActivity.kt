package com.example.vbplusapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.vbplusapp.game.DataBaseManagerAndroid
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.Gson

import com.example.vbplusapp.game.Game
import com.example.vbplusapp.game.Set
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.TypeVariable
import java.util.*


class MainActivity : AppCompatActivity() {

    // Request codes
    private val REQUEST_SETTINGS_ACTIVITY = 0


    //Other activity variables or values
    lateinit var game: Game
    lateinit var dbManager: DataBaseManagerAndroid


    /*
        Initializes a new game based on the settings given

        Parameters:
        mutableList

        Returns:
        Game object
     */
    private fun loadGameSettings(gameSettings: MutableList<String>
                            = mutableListOf("3","2","Team1","Team2","25") ): Game {

        //Returns a game object with the settings chosen. If none given, use default
        return Game(gameSettings[0].toInt(),gameSettings[1].toInt(),
                    gameSettings[2],gameSettings[3], gameSettings[4].toInt())
    }

    private fun initialize(){
        dbManager = DataBaseManagerAndroid(applicationContext)
    }

    /*
        Sets the point scored button to invisible + next set button visible or vice versa
        depending if the current set of the game is over or not

        Parameters:
        game: Game = the volleyball game object

        returns:
        nothing
    */
    private fun updateButtons(game: Game){
       if(game.sets[game.currentSet-1].isOver) { // if the current set is over show the next set button, hide two other
           addPointTeam2Button.visibility = View.GONE
           addPointTeam1Button.visibility = View.GONE
           nextSetButton.visibility = View.VISIBLE
       }
        else if (!game.sets[game.currentSet-1].isOver){ // if set is not over (next set button pressed) reverses the view
           addPointTeam1Button.visibility = View.VISIBLE
           addPointTeam2Button.visibility = View.VISIBLE
           nextSetButton.visibility = View.GONE
       }
    }

    /*
        Updates the score view and also the button visibility

        Parameters:
        game: Game = the game object that is needed to show scores and stuff
     */
    private fun update(game: Game){
        updateButtons(game)
        refreshDisplay(game)
    }

    private fun refreshDisplay(game: Game){

        var currentSet: Set = game.sets[game.currentSet-1]
        var text: String = "Default Volleyball Game \n\n"
        text += game.isOver.toString() +  "\n"

        text += game.setHistory
        text += "\n"

        text += "\n"
        text += "_______________________\n\n"

        text = text + "Team 1: " + game.team1Name.toString() + "\n"
        text = text + "Score: " + currentSet.team1Score.toString() + "\n\n"

        text = text + "Team 2: " + game.team2Name.toString() + "\n"
        text = text + "Score: " + currentSet.team2Score.toString() + "\n\n"

        text = text + "Score history T1: " + currentSet.team1ScoreHistory + "\n"
        text = text + "Score history T2: " + currentSet.team2ScoreHistory + "\n\n"

        textView.text = text
    }

    /*
        Handles activity results if the activity is called for a result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // This is the case if the settings activity is called and the apply button was clicked
        if (requestCode == REQUEST_SETTINGS_ACTIVITY && resultCode == RESULT_OK){
            var settingsApplied: Boolean = data!!.getBooleanExtra("settingsApplied",false)
            if(settingsApplied){
                var settingsList: MutableList<String> =
                            Gson().fromJson(dbManager.loadGameSettings("latest"),
                                            Array<String>::class.java).toMutableList()
                loadGameSettings(settingsList)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Initialization block
        initialize()
        game = loadGameSettings()
        refreshDisplay(game)

        nextSetButton.visibility = View.GONE

        addPointTeam1Button.setOnClickListener {
            game.addPoint(1)
            update(game)
        }

        addPointTeam2Button.setOnClickListener {
            game.addPoint(2)
            update(game)
        }

        reverse.setOnClickListener {
            game.sets[game.currentSet-1].reverseLastAction()
            update(game)
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent,REQUEST_SETTINGS_ACTIVITY)
        }

        nextSetButton.setOnClickListener{
            game.nextSet()
            update(game)
        }




    }

}



