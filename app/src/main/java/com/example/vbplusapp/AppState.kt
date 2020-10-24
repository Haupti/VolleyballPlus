package com.example.vbplusapp

import com.example.vbplusapp.game.Game

/*
    The purpose for this class is to store all the information and various other stuff
    that must be carried between activities and stored in case of a crash.

    This for example includes:
    settingsPresetSelected number - indicating which settings preset was selected in the settings activity
                                    so it can be loaded in the main activity

    This does not include things like:
    the actual list of settings presets - those should be permanent as long the app is installed,
                                          thus are saved in separate file
 */
class AppState constructor(){
    var settingsPresetSelected : Int = 0
    var activeGame : Game = Game()
}