package com.example.vbplusapp.game

class Game constructor(settings: GameSettings = GameSettings()){
    var settings: GameSettings = settings
    var setNumber : Int
    var currentSet : Int
    var currentSetOver : Boolean
    var setHistory: MutableList<Int>
    var isOver: Boolean
    var sets : MutableList<Set>
    var servingTeamNumber : Int = 0

    init {
        this.setNumber = (this.settings.winScore * 2) - 1
        this.currentSet = 1
        this.currentSetOver = false
        this.setHistory = MutableList(setNumber){0}
        this.isOver = false
        this.sets = MutableList(setNumber){ Set(this.settings.winScore, this.settings.getPointGap())}
    }


    /*
        Adds a point to team number teamNumber on the current set.
        Returns true if successful but false if the set is over
     */
    fun addPoint(teamNumber: Int): Boolean {
        if(!sets[currentSet-1].isOver) {
            sets[currentSet-1].addPoint(teamNumber)
            setHistory[currentSet-1] = sets[currentSet-1].checkWinner()
            servingTeamNumber = teamNumber
            checkWinner() // this is the games checkWinner function. does something else as the sets checkWinner function
            return true
        }
        currentSetOver = true
        return false
    }


    /*
        Starts the next set, if the current set is over and the game is not over.
        Returns true if it is successful, false if not.
     */
    fun nextSet():Boolean {
        if (!isOver && sets[currentSet-1].isOver) {
            this.setHistory[currentSet-1] = sets[currentSet-1].checkWinner()
            currentSet += 1
            currentSetOver = false
            servingTeamNumber = 0
            return true
        }
        return false
    }


    /*
        Checks if the next point is the game point.
        Returns true if it is, false if not.

        Point is a game point, if one team won winningSets - 1 sets, and the current point is a set point.
     */
    fun isGamePoint():Boolean {
        if((setHistory.count{it == 1} == (settings.winSets- 1)) && sets[currentSet-1].isSetPoint()){
            return true
        }
        else if((setHistory.count{it == 2} == (settings.winSets -1)) && sets[currentSet-1].isSetPoint()){
            return true
        }
        return false
    }


    /*
         Checks if the game is over and sets isOver to true if it is.
         The game is over, if one of the teams has won winningSets number of sets.
     */
    private fun checkWinner(){
        if((setHistory.count{it==1} == settings.winSets) || (setHistory.count{it==2} == settings.winSets)){
            this.isOver = true
        }
    }
}