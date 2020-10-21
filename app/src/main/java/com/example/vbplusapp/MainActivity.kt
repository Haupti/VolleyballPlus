package com.example.vbplusapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.Gson

import com.example.vbplusapp.game.Game
import com.example.vbplusapp.game.Set
import com.example.vbplusapp.game.FAILED


class MainActivity : AppCompatActivity() {
    //default settings. Might need a better solution for that problem.
    // The problem: first starting the app, then there is no templates.json and settings_latest.json file
    // then the app needs to make that. But only for the first ever time the app is started.
    private val defaultSettings: MutableList<String> = mutableListOf("3","2","Team1","Team2","25")

    //Other activity variables or values
    lateinit var game: Game
    lateinit var dbMan: DataBaseManagerAndroid


    /*
        Initializes a new game based on the settings given

        Parameters:
        mutableList

        Returns:
        Game object
     */
    private fun makeGame(gameSettings: MutableList<String> = this.defaultSettings): Game {

        //Returns a game object with the settings chosen. If none given, use default
        return Game(gameSettings[0].toInt(),gameSettings[1].toInt(),
                    gameSettings[2],gameSettings[3], gameSettings[4].toInt())
    }

    /*
        Loads the game settings from the file with given file suffix,
        then makes a new Game object with the given settings from file. If none are found
        Game will be created with the Game classes default settings
     */
    private fun loadGameSettings(templateName:String){
        var response = dbMan.loadGameSettings(templateName)
        if (response.responseState == FAILED){
            this.game = makeGame() // Use standard settings if loading failed
            dbMan.addTemplateToList("latest",this.defaultSettings.toString())
        }
        else {
            var responseText = response.responseText
            var settingsList: MutableList<String> =
                Gson().fromJson(responseText, Array<String>::class.java).toMutableList()
            this.game = makeGame(settingsList)
        }
    }


    private fun initialize(){
        dbMan= DataBaseManagerAndroid(applicationContext)
        this.game = makeGame() // Make a game with standard settings
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
       if(game.sets[game.currentSet-1].isOver || game.isOver) { // if the current set is over show the next set button, hide two other
           team1ScoreUp.visibility = View.GONE
           team2ScoreUp.visibility = View.GONE
           if(game.isOver) {
               reloadButton.text = "New Game"
           }
           else {
               nextSetButton.visibility = View.VISIBLE
           }

       }
        else if (!game.sets[game.currentSet-1].isOver){ // if set is not over (next set button pressed) reverses the view
           reloadButton.text = "Reload"
           team1ScoreUp.visibility = View.VISIBLE
           team2ScoreUp.visibility = View.VISIBLE
           nextSetButton.visibility = View.GONE
       }
    }

    private fun refreshDisplay(game: Game){

        var currentSet: Set = game.sets[game.currentSet-1]

        team1NameView.text = game.team1Name
        team2NameView.text = game.team2Name
        team1SetsView.text = game.setHistory.count{it==1}.toString()
        team2SetsView.text = game.setHistory.count{it==2}.toString()
        team1ScoreView.text = currentSet.team1Score.toString()
        team2ScoreView.text = currentSet.team2Score.toString()
        //team1ScoreHistView.text = currentSet.team1ScoreHistory.toString()
        //team2ScoreHistView.text = currentSet.team2ScoreHistory.toString()

    }

    /*
        Updates the score view and also the button visibility

        Parameters:
        game: Game = the game object that is needed to show scores and stuff
     */
    private fun update(){
        updateButtons(this.game)
        refreshDisplay(this.game)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialization block
        initialize()
        refreshDisplay(this.game)
        update()

        nextSetButton.visibility = View.GONE

        team1ScoreUp.setOnClickListener {
            this.game.addPoint(1)
            update()
        }

        team2ScoreUp.setOnClickListener {
            this.game.addPoint(2)
            update()
        }

        reverse.setOnClickListener {
            this.game.sets[game.currentSet-1].reverseLastAction()
            update()
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        nextSetButton.setOnClickListener{
            this.game.nextSet()
            update()
        }

        reloadButton.setOnClickListener {
            this.loadGameSettings("latest")
            refreshDisplay(this.game)
            update()
        }

    }


}



