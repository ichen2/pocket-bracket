package com.ichen.pocketbracket.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme

class TournamentDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketBracketTheme {
                TournamentDetailsScreen()
            }
        }
    }
}