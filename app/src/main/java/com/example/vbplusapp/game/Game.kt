package com.example.vbplusapp.game

class Game(_winningSets: Int = 3, _pointsGap:Int = 2, _team1Name:String = "Team1", _team2Name:String = "Team2", _defaultPoints:Int = 25) {
    var currentSet = 1;
    var currentSetOver: Boolean = false
    val winningSets = _winningSets
    val setNumber = (_winningSets * 2) - 1
    var setHistory: MutableList<Int> = MutableList(setNumber) {0}
    var isOver: Boolean = false
    var sets:MutableList<Set> = MutableList(setNumber){ Set(_defaultPoints, _pointsGap) }
    var team1Name = _team1Name
    var team2Name = _team2Name


    /*
        Adds a point to team number teamNumber on the current set.
        Returns true if successful but false if the set is over
     */
    fun addPoint(teamNumber: Int): Boolean {
        if(!sets[currentSet-1].isOver) {
            sets[currentSet-1].addPoint(teamNumber)
            setHistory[currentSet-1] = sets[currentSet-1].checkWinner()
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
        if((setHistory.count{it == 1} == (winningSets - 1)) && sets[currentSet-1].isSetPoint()){
            return true
        }
        else if((setHistory.count{it == 2} == (winningSets -1)) && sets[currentSet-1].isSetPoint()){
            return true
        }
        return false
    }


    /*
         Checks if the game is over and sets isOver to true if it is.
         The game is over, if one of the teams has won winningSets number of sets.
     */
    private fun checkWinner(){
        if((setHistory.count{it==1} == winningSets) || (setHistory.count{it==2} == winningSets)){
            this.isOver = true
        }
    }
}