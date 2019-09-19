package trap1.luphilip.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

public class GameOver extends AppCompatActivity {

        TextView gameOver;
        TextView highScore;
        Button reset;
        TextView scoreText;
        SharedPreferences mPreferences;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game_over);
            gameOver = findViewById(R.id.gameOver);
            reset = findViewById(R.id.resetButton);
            scoreText = findViewById(R.id.scoreText);
            highScore = findViewById(R.id.highScore);
            mPreferences = getSharedPreferences("trap1.luphilip.quizapp.sharedprefs", MODE_PRIVATE);
            scoreText.setText(String.format("Score: %s", MainActivity.getScore()));

            if(MainActivity.getScore() > mPreferences.getInt("mResponseNum", 0)) {
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putInt("mResponseNum", MainActivity.getScore());
                preferencesEditor.apply();
            }
            highScore.setText("High Score: " + mPreferences.getInt("mResponseNum", 0));
        }

        public void returnToPrevious(View v) {
            Intent returnIntent = new Intent(GameOver.this, MainActivity.class);
            startActivity(returnIntent);
        }

    }
