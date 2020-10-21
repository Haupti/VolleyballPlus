package com.example.vbplusapp.game

class GameSettings {
    lateinit var identifier: String
    lateinit var templateName: String
    lateinit var team1Name: String
    lateinit var team2Name: String
    var winScore: Int = NOT_SET
    private var pointGap: Int = 2
    var winSets: Int  = NOT_SET

    /*
        Creates a settings object with standard settings
        with indoor volleyball rules
     */
    constructor(){
        this.identifier = "default"
        this.templateName = "Standard Indoor"
        this.team1Name = "Team 1"
        this.team2Name = "Team 2"
        this.winScore = 25
        this.winSets = 3

    }

    /*
        Creates a settings object with given settings
     */
    constructor(team1Name: String, team2Name: String, winScore: Int, winSets: Int, templateName: String = "template"){
        this.identifier = this.toString()
        this.templateName = templateName
        this.team1Name = team1Name
        this.team2Name = team2Name
        this.winScore = winScore
        this.winSets = winSets
    }

    /*
        Sets the private value pointGap that defines
        how many points one team must be ahead of the other
        in order to be able to win
     */
    fun setPointGap(gap: Int){
        this.pointGap = gap
    }
    /*
        Returns the private property pointGap
    */
    fun getPointGap(): Int{
        return this.pointGap
    }
}
