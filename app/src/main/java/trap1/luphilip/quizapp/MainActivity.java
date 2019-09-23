package trap1.luphilip.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    //Field instantiation
    Button startButton;
    Button resetButton;
    Button nextButton;
    Button redButton;
    Button blueButton;
    Button yellowButton;
    Button greenButton;
    TextView displayText;
    TextView resultText;
    TextView scoreText;
    TextView timerBox;
    TextView highScore;
    SharedPreferences mPreferences;
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("s");
    private int randomNum = (int) (Math.random() * 4 + 1);
    private int count = 0;
    private String correct = "";
    private String userSequence = "";
    private static int score = 0;
    private char temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assigning widgets
        mPreferences = getSharedPreferences("trap1.luphilip.quizapp.sharedprefs", MODE_PRIVATE);
        resetButton = findViewById(R.id.resetButton);
        startButton = findViewById(R.id.clickButton);
        nextButton = findViewById(R.id.nextButton);
        redButton = findViewById(R.id.hover_red);
        blueButton = findViewById(R.id.hover_blue);
        yellowButton = findViewById(R.id.hover_yellow);
        greenButton = findViewById(R.id.hover_green);
        displayText = findViewById(R.id.displayBox);
        resultText = findViewById(R.id.resultBox);
        scoreText = findViewById(R.id.scoreText);
        timerBox = findViewById(R.id.timerBox);
        highScore = findViewById(R.id.highScore);

        //Display high score
        highScore.setText("High Score: " + mPreferences.getInt("mResponseNum", 0));


        //Default Board
        redButton.setBackgroundResource(R.drawable.simon_button_red);
        blueButton.setBackgroundResource(R.drawable.simon_button_blue);
        yellowButton.setBackgroundResource(R.drawable.simon_button_yellow);
        greenButton.setBackgroundResource(R.drawable.simon_button_green);
        nextButton.setEnabled(false);
        boardToggle(false);

        //START
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBoard();
                startButton.setEnabled(false);
                correct += randomNum;
                count = 0;
                colorTimer.start();
            }
        });

        //NEXT
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardToggle(false);
                nextButton.setEnabled(false);
                countDownTimer.cancel();
                hintTimer.cancel();
                if (check(userSequence, correct)) { // If user got the correct sequence
                    count = 0;
                    score++;
                    scoreText.setText(String.format("Score: %s", score));
                    resultText.setText(R.string.resultName);
                    displayText.setText("Correct!");
                    cancelTimers();
                    countDownTimer2.start();
                } else {                            // If user is wrong
                    displayText.setText("Incorrect!");
                    cancelTimers();
                    Intent intent = new Intent(MainActivity.this, GameOver.class);
                    startActivity(intent);
                    resultText.setText(correct);
                }
            }
        });

        //RESET
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimers();
                resultText.setText(R.string.resultName);
                score = 0;
                scoreText.setText(String.format("Score: %s", score));
                timerBox.setText(R.string.timerBox);
                displayText.setText(R.string.displayName);
                redButton.setBackgroundResource(R.drawable.simon_button_red);
                blueButton.setBackgroundResource(R.drawable.simon_button_blue);
                yellowButton.setBackgroundResource(R.drawable.simon_button_yellow);
                greenButton.setBackgroundResource(R.drawable.simon_button_green);
                startButton.setEnabled(true);
                resetButton.setEnabled(true);
                nextButton.setEnabled(false);
                boardToggle(false);
                correct = "";
                userSequence = "";
                count = 0;
            }
        });



        //COLOR BUTTONS
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSequence += 1;
            }
        });


        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSequence += 2;
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSequence += 3;
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSequence += 4;
            }
        });
    }

    ///Helper methods
    public static int getScore() {
        return score;
    }

    public void clearBoard() {
        redButton.setBackgroundResource(R.drawable.hover_button_red);
        blueButton.setBackgroundResource(R.drawable.hover_button_blue);
        yellowButton.setBackgroundResource(R.drawable.hover_button_yellow);
        greenButton.setBackgroundResource(R.drawable.hover_button_green);
    }

    public boolean check(String correct, String user) {
        return correct.equals(user);
    }


    public void boardToggle(boolean b) {
        redButton.setEnabled(b);
        blueButton.setEnabled(b);
        yellowButton.setEnabled(b);
        greenButton.setEnabled(b);
    }

    public void cancelTimers() {
        colorTimer.cancel();
        hintTimer.cancel();
        countDownTimer.cancel();
        countDownTimer2.cancel();
    }


    //Main timer
    CountDownTimer countDownTimer = new CountDownTimer(11000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timerBox.setText(mSimpleDateFormat.format(millisUntilFinished));
        }

        @Override
        public void onFinish() {
            startButton.setEnabled(false);
            boardToggle(false);
            displayText.setText("Done!");
            timerBox.setText("fin");
            resultText.setText(R.string.resultName);
        }
    };

    //Buffer between rounds
    CountDownTimer countDownTimer2 = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            randomNum = (int) (Math.random() * 4 + 1);
            count = 0;
            correct += randomNum;
            userSequence = "";
            displayText.setText(R.string.displayName);
            colorTimer.start();
        }
    };

    //Color sequence display
    CountDownTimer colorTimer = new CountDownTimer(1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            temp = correct.charAt(count);
            if (temp == '1') {
                System.out.println("Red");
                redButton.setBackgroundResource(R.drawable.simon_button_red);
                blueButton.setBackgroundResource(R.drawable.hover_button_blue);
                yellowButton.setBackgroundResource(R.drawable.hover_button_yellow);
                greenButton.setBackgroundResource(R.drawable.hover_button_green);
            }
            if (temp == '2') {
                System.out.println("Blue");
                redButton.setBackgroundResource(R.drawable.hover_button_red);
                blueButton.setBackgroundResource(R.drawable.simon_button_blue);
                yellowButton.setBackgroundResource(R.drawable.hover_button_yellow);
                greenButton.setBackgroundResource(R.drawable.hover_button_green);

            }
            if (temp == '3') {
                redButton.setBackgroundResource(R.drawable.hover_button_red);
                blueButton.setBackgroundResource(R.drawable.hover_button_blue);
                yellowButton.setBackgroundResource(R.drawable.simon_button_yellow);
                greenButton.setBackgroundResource(R.drawable.hover_button_green);
            }
            if (temp == '4') {
                redButton.setBackgroundResource(R.drawable.hover_button_red);
                blueButton.setBackgroundResource(R.drawable.hover_button_blue);
                yellowButton.setBackgroundResource(R.drawable.hover_button_yellow);
                greenButton.setBackgroundResource(R.drawable.simon_button_green);
            }
            count++;
            if (count < correct.length()) {
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        redButton.setBackgroundResource(R.drawable.simon_button_black);
                        blueButton.setBackgroundResource(R.drawable.simon_button_black);
                        yellowButton.setBackgroundResource(R.drawable.simon_button_black);
                        greenButton.setBackgroundResource(R.drawable.simon_button_black);
                    }
                }.start();
                colorTimer.start();
            } else {
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        clearBoard();
                    }
                }.start();
                resultText.setText(correct);
                boardToggle(true);
                nextButton.setEnabled(true);
                hintTimer.start();
                countDownTimer.start();
            }

        }
    };

    //delay for the hint
    CountDownTimer hintTimer = new CountDownTimer(2000, 10000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            resultText.setText(R.string.resultName);
        }
    };
}
