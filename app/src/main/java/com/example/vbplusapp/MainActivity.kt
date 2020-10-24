package com.example.vbplusapp

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.vbplusapp.game.*
import com.example.vbplusapp.game.Set
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.Gson
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    //Other activity variables or values
    private lateinit var state: AppState
    private lateinit var game: Game // will be set to the states game in the init function
    lateinit var dbMan: DatabaseManagerAndroid

    private fun initialize() {
        this.dbMan.createDatabase() // only works if the database files do not exist
        this.state = dbMan.loadState()
        this.game = state.activeGame
        update() // calls the update function to display the loaded game (default)
    }
    /*
        Loads the game settings chosen in the settings menu
        Makes new game with those settings. This causes the old settings to be lost
     */
    private fun loadGameSettings(){
        this.game = Game(dbMan.getSettings(state.settingsPresetSelected))
        update()
    }


    /*
        Sets the point scored button to invisible + next set button visible or vice versa
        depending if the current set of the game is over or not

        Parameters:
        game: Game = the volleyball game object

        returns:
        nothing
    */
    private fun updateButtons(){
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

    private fun refreshDisplay(){

        var currentSet: Set = this.game.sets[game.currentSet-1]

        team1NameView.text = this.game.settings.team1Name
        team2NameView.text = this.game.settings.team2Name
        team1SetsView.text = this.game.setHistory.count{it==1}.toString()
        team2SetsView.text = this.game.setHistory.count{it==2}.toString()
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
        updateButtons()
        refreshDisplay()
        dbMan.saveState(state)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        // START of init block
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nextSetButton.visibility = View.GONE
        this.dbMan = DatabaseManagerAndroid(applicationContext)
        initialize() //Do not change this to init{} block! If you do, the app wont be able to
                    // find applicationContext -> null pointer exception
                    // because the context is created during the creation of the activity.
                    // An init block would try to set the context before the Application is created
                    // during the creation of the class!
        // END of init block

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
            startActivityForResult(intent, 0 )
        }

        nextSetButton.setOnClickListener{
            this.game.nextSet()
            update()
        }

        reloadButton.setOnClickListener {
            Toast.makeText(this, "Hold button to reload. \nThis will cause the current game to get lost",
                Toast.LENGTH_LONG).show()
        }

        reloadButton.setOnLongClickListener {
            loadGameSettings()
            refreshDisplay()
            update()
            return@setOnLongClickListener true
        }


    }


    override fun onResume() {
        super.onResume()

        this.state = dbMan.loadState()
        update()
    }


}



