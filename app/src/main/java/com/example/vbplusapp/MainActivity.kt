package com.example.vbplusapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.vbplusapp.game.*
import com.example.vbplusapp.game.Set
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Other activity variables or values
    private lateinit var state: AppState
    private lateinit var game: Game // will be set to the states game in the init function
    lateinit var dbMan: DatabaseManagerAndroid

    private fun initialize() {
        this.dbMan.create() // only works if the database files do not exist
        this.state = dbMan.loadState()
        this.game = state.activeGame
        update() // calls the update function to display the loaded game (default)
    }
    /*
        Loads the game settings chosen in the settings menu
        Makes new game with those settings. This causes the old settings to be lost
     */
    fun loadGameSettings(){
        this.game = Game(state.latestSettings)
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
           if(!game.isOver) {
               nextSetButton.visibility = View.VISIBLE
           }

       }
        else if (!game.sets[game.currentSet-1].isOver){ // if set is not over (next set button pressed) reverses the view
           team1ScoreUp.visibility = View.VISIBLE
           team2ScoreUp.visibility = View.VISIBLE
           nextSetButton.visibility = View.GONE
       }
    }

    private fun updateSetPointHighlight(){
        when ( game.sets[game.currentSet-1].teamThatHasSetPoint ) {
            0 -> {
                team1ScoreView.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                team2ScoreView.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
            }
            1 -> {
                team1ScoreView.setTextColor(ContextCompat.getColor(this,R.color.colorAccent))
                team2ScoreView.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                Toast.makeText(this, "Set Point!", Toast.LENGTH_SHORT).show()
            }
            2 -> {
                team1ScoreView.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                team2ScoreView.setTextColor(ContextCompat.getColor(this,R.color.colorAccent))
                Toast.makeText(this, "Set Point!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateServeMarker(){
        when ( game.servingTeamNumber ){
            0 -> {
                team1ServesMarker.visibility = View.INVISIBLE
                team2ServesMarker.visibility = View.INVISIBLE
            }
            1 -> {
                team1ServesMarker.visibility = View.VISIBLE
                team2ServesMarker.visibility = View.INVISIBLE
            }
            2 -> {
                team1ServesMarker.visibility = View.INVISIBLE
                team2ServesMarker.visibility = View.VISIBLE
            }

        }

    }

    fun refreshDisplay(){

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
    fun update(){
        updateButtons()
        updateServeMarker()
        updateSetPointHighlight()
        refreshDisplay()
        dbMan.saveState(state)
    }

    private fun onAlertDialog(view: View){
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Update the game state?")
        builder.setMessage(
            "Do you want to use the new settings while in a ongoing game?" +
                    "\nThis will cause the score to be reset." +
                    "\nYou can manually load the settings later by holding the folder icon on the top left corner."
        )
        builder.setPositiveButton("Apply Settings"){
                _, _ -> loadGameSettings()
        }
        builder.setNegativeButton("Don't Apply"){
            _, _ -> Toast.makeText(this, "Old settings are kept.", Toast.LENGTH_SHORT).show()
        }
        builder.show()
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
        reverse.setOnLongClickListener{
            Toast.makeText(this, "Reverse last point granted.", Toast.LENGTH_LONG).show()
            return@setOnLongClickListener true
        }

        menuButton.setOnClickListener {
            MenuDialog(this).show()
        }

        nextSetButton.setOnClickListener{
            this.game.nextSet()
            update()
        }




    }


    override fun onResume() {
        super.onResume()

        this.state = dbMan.loadState()
        if((game.sets[game.currentSet-1].team1Score != 0 || game.sets[game.currentSet-1].team2Score != 0
                || game.currentSet > 1) && state.reloadRequested ) {
            state.reloadRequested = false
            dbMan.saveState(state)
            onAlertDialog(this.view)
        }

        update()
    }
}



