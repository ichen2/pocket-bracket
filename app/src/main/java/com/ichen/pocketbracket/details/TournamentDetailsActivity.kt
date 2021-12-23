package com.ichen.pocketbracket.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme

class TournamentDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tournament = intent.getParcelableExtra<Tournament>("tournament")
        setContent {
            PocketBracketTheme {
                if(tournament != null) TournamentDetailsScreen(tournament) else Text("Error")
            }
        }
    }
}