package com.example.vbplusapp.game

class Set (_defaultPoints:Int = 25, _pointGap: Int = 2){
    var defaultPoints: Int = _defaultPoints
    var pointGap: Int = _pointGap
    var team1Score: Int = 0
    var team2Score: Int = 0
    var team1ScoreHistory: MutableList<Int> = MutableList(1) {0}
    var team2ScoreHistory: MutableList<Int> = MutableList(1) {0}
    var isOver:Boolean = false
    var teamThatHasSetPoint : Int = 0

    /*
        Adds a score point to the team with number teamNumber
        Updates score history of the team and current score
        Also checks if the set is won.
     */
    fun addPoint(teamNumber:Int){
        if(isOver) return
        when (teamNumber) {
            1 -> {
                team1Score += 1
                team1ScoreHistory.add(team1Score)
                team2ScoreHistory.add(team2Score) //if the enemy scores, the history must know when nothing changed
            }
            2 -> {
                team2Score += 1
                team1ScoreHistory.add(team1Score) //if the enemy scores, the history must know when nothing changed
                team2ScoreHistory.add(team2Score)
            }
        }
        checkWinner()
        isSetPoint()
    }

    /*
        Checks if the set is over and returns the winning teams number.
        If team 1 or 2 wins, returns 1 or 2. If 0 is returned the game is not over.
    */
    fun checkWinner():Int {
        if(isOver){
            if(team1Score>team2Score) return 1
            return 2
        }
        if (team1Score >= defaultPoints || team2Score >= defaultPoints) {
            if (team1Score >= team2Score + pointGap || team2Score >= team1Score + pointGap) {
                this.isOver = true
                return checkWinner()
            }
        }
        return 0
    }


    /*
        Checks if the next point is a set point.
        This is the case if the teams score differ by the pointGap -1 and are close to the default points
        or higher
     */
    fun isSetPoint() : Boolean{
        if(team1Score >= (defaultPoints - 1) && (team2Score + pointGap - 1) <= team1Score){
            teamThatHasSetPoint = 1
            return true
        }
        else if (team2Score >= (defaultPoints - 1) && (team1Score + pointGap - 1) <= team2Score){
            teamThatHasSetPoint = 2
            return true
        }
        teamThatHasSetPoint = 0
        return false
    }


    /*
        Reverses the last action.
        Last action is: Team1 gets a point, Team2 gets a point.
        New set can't be reversed.
        Returns:
        Success Boolean: true if successfull, false if not (for example try of removing 0 th point)
    */
    fun reverseLastAction(): Boolean{

        if(this.isOver){
            return false
        }
        else if(team1ScoreHistory.size>1){
            team1ScoreHistory.removeLast()
            team2ScoreHistory.removeLast()
            team1Score = team1ScoreHistory[team1ScoreHistory.size-1]
            team2Score = team2ScoreHistory[team2ScoreHistory.size-1]
            isSetPoint()

            return true
        }
        else {return false}
    }
}