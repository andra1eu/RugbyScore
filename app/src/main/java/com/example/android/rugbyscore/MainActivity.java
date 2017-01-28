package com.example.android.rugbyscore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private static final int TEAM_A_ID = 1;
    private static final int TEAM_B_ID = 2;

    private static final String TEAM_A_SCORE_SAVE_KEY = "TEAM_A_SCORE_SAVE_KEY";
    private static final String TEAM_B_SCORE_SAVE_KEY = "TEAM_B_SCORE_SAVE_KEY";
    private static final String ELAPSED_TIME_SAVE_KEY = "ELAPSED_TIME_SAVE_KEY";

    private static final int SESSION_DURATION = 60;

    final Handler timerHandler = new Handler();
    private boolean gameStarted = false;
    private int elapsedTime = 0;

    private int teamAScore = 0;
    private int teamBScore = 0;

    private TextView teamA;
    private TextView teamB;

    private ProgressBar progressBar;
    private Button startButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamA = (TextView) findViewById(R.id.team_a_score);
        teamB = (TextView) findViewById(R.id.team_b_score);
        startButton = (Button) findViewById(R.id.start_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        startButton.setOnClickListener(this);
        progressBar.setMax(SESSION_DURATION);

        if (savedInstanceState != null) {
            teamAScore = savedInstanceState.getInt(TEAM_A_SCORE_SAVE_KEY, 0);
            teamBScore = savedInstanceState.getInt(TEAM_B_SCORE_SAVE_KEY, 0);
            elapsedTime = savedInstanceState.getInt(ELAPSED_TIME_SAVE_KEY, 0);
            if (elapsedTime != 0) startButton.performClick();
        }

        teamA.setText(teamAScore + "");
        teamB.setText(teamBScore + "");
    }

    @Override
    public void onClick(View v) {
        int buttonStringId;

        if (gameStarted) {
            buttonStringId = R.string.button_start;
            resetGame();
        } else {
            progressBar.setProgress(elapsedTime);
            buttonStringId = R.string.button_stop;
            timerHandler.postDelayed(this, 1000);
        }

        gameStarted = !gameStarted;
        startButton.setText(buttonStringId);
    }

    @Override
    public void run() {
        if (elapsedTime == SESSION_DURATION) {
            resetGame();
            return;
        }
        elapsedTime++;
        progressBar.incrementProgressBy(1);
        timerHandler.postDelayed(this, 1000);
    }


    private void resetGame() {
        timerHandler.removeCallbacksAndMessages(null);
        elapsedTime = 0;
        progressBar.setProgress(0);
        teamAScore = 0;
        teamBScore = 0;
        updateScore(TEAM_A_ID, 0);
        updateScore(TEAM_B_ID, 0);
        startButton.setText(R.string.button_start);
    }


    public void scoreButtonClicked(View v) {
        int id = v.getId();
        int teamId = 0;
        int points = 0;
        switch (id) {
            case R.id.team_a_penalty_try:
                teamId = TEAM_A_ID;
                points = 5;
                break;
            case R.id.team_a_convension:
                teamId = TEAM_A_ID;
                points = 2;
                break;
            case R.id.team_a_drop_goal:
                teamId = TEAM_A_ID;
                points = 3;
                break;
            case R.id.team_a_penalty_goal:
                teamId = TEAM_A_ID;
                points = 3;
                break;
            case R.id.team_b_penalty_try:
                teamId = TEAM_B_ID;
                points = 5;
                break;
            case R.id.team_b_convension:
                teamId = TEAM_B_ID;
                points = 2;
                break;
            case R.id.team_b_drop_goal:
                teamId = TEAM_B_ID;
                points = 3;
                break;
            case R.id.team_b_penalty_goal:
                teamId = TEAM_B_ID;
                points = 3;
                break;
        }
        updateScore(teamId, points);
    }

    private void updateScore(int teamId, int points) {
        switch (teamId) {
            case TEAM_A_ID:
                teamAScore += points;
                teamA.setText(teamAScore + "");
                break;

            case TEAM_B_ID:
                teamBScore += points;
                teamB.setText(teamBScore + "");
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TEAM_A_SCORE_SAVE_KEY, teamAScore);
        outState.putInt(TEAM_B_SCORE_SAVE_KEY, teamBScore);
        outState.putInt(ELAPSED_TIME_SAVE_KEY, elapsedTime);
    }
}