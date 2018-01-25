/**
***********************************************************
* Name:  Janila Khan                                      *
* Project : Project 2 / Longana Game			          *
* Class : CMPS 366 Organization of Programming Languages  *
* Date : November 17, 2017                                *
***********************************************************
*/

package com.longana.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.longana.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* ******************************************
This class will initialize a tournament game
****************************************** */

public class TournamentActivity extends AppCompatActivity {
    // Class Constants
    public static final String num = "number";
    public static final String t_score = "score";
    public static final String game = "roundType";
    public static final String tempComp = "computerHand";
    public static final String tempHum = "humanHand";
    public static final String tempLay = "layout";
    public static final String tempBone = "stock";
    public static final String currPlay = "player";
    public static final String playerPass = "pass";

    // Class Variables
    private int tournamentPoints;
    private int roundNumber;
    private int humanOverallScore;
    private int computerOverallScore;
    private boolean tournamentOver;
    private String fileName;

    private String tempCompHand;
    private String tempHumanHand;
    private String tempLayout;
    private String tempStock;
    private String current;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        // Determine what type of tournament will be played
        final String roundGame = getIntent().getStringExtra(MainActivity.tournament_game);

        // if it is equal to new a new game will be played
        if(roundGame.equalsIgnoreCase("new"))
        {
            // Set tournament points to the value the user set in main activity
            tournamentPoints = getIntent().getIntExtra(MainActivity.tournament_score,0);

            // Initialize the class variables
            roundNumber = 0;
            humanOverallScore = 0;
            computerOverallScore = 0;
        }else{
            // Set file name to the value the user selected in main activity
            fileName = getIntent().getStringExtra(MainActivity.tournament_file);

            // The absolute path of where the file was saved
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/longanaFiles/" + fileName;
            openRoundGame(path);
        }

        tournamentOver = false;

        // This button will play the tournament depending on the user decision
        Button playButton = findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If the tournament is new it will call the playGame function otherwise the loadGame function will be called
                if(roundGame.equalsIgnoreCase("new")){
                    playGame();
                }else{
                    loadGame();
                }
            }
        });

        // This button will quit the tournament if the user changes their mind on what type of tournament they would like to play
        Button quitButton = findViewById(R.id.quit);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitTournament();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED){
            AlertDialog.Builder saveFileName = new AlertDialog.Builder(TournamentActivity.this);
            saveFileName.setTitle("Save Game Name");
            saveFileName.setMessage("What would you like to call the saved game (include .txt at the end)?");

            // The input the Alert Dialog will take
            final EditText saveInput = new EditText(this);

            // View what the user inputs
            saveFileName.setView(saveInput);

            // Press Enter when you are finishing entering the file name, when clicked the game will be saved
            saveFileName.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    fileName = saveInput.getText().toString();
                    saveRoundGame(data);
                    Toast.makeText(TournamentActivity.this, "The game was saved!", Toast.LENGTH_SHORT).show();
                    finish();
                    System.exit(0);
                }
            });

            // Show the Alert Dialog
            saveFileName.show();
        }
        else {
            int h_score = data.getIntExtra("human", 0);
            int c_score = data.getIntExtra("computer", 0);

            humanOverallScore += h_score;
            computerOverallScore += c_score;

            // Create an alert dialog box
            AlertDialog.Builder tournamentScores = new AlertDialog.Builder(TournamentActivity.this);

            // Set the title of the alert dialog box
            tournamentScores.setTitle("Display Tournament Scores");

            // The message that will be displayed
            String playerMessage = "Human: " +  humanOverallScore + "\n";
            playerMessage += "Computer: " + computerOverallScore + "\n";
            tournamentScores.setMessage(playerMessage);

            // The button of the dialog box, when pressed would close the dialog box
            tournamentScores.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (isTournamentOver()) {
                        displayTournamentWinner();
                    } else {
                        roundNumber++;
                        playGame();
                    }
                }
            });

            // Display the alert dialog box
            tournamentScores.show();
        }
    }

    /**
     Start a new round activity
     */
    public void playGame(){
        Intent intent = new Intent(this, RoundActivity.class);
        intent.putExtra(game, "new");
        intent.putExtra(num, roundNumber);
        intent.putExtra(t_score, tournamentPoints);
        startActivityForResult(intent,1);
    }

    /**
     Start a saved round activity
     */
    public void loadGame(){
        Intent intent = new Intent(this, RoundActivity.class);
        intent.putExtra(game, "load");
        intent.putExtra(num, roundNumber);
        intent.putExtra(t_score, tournamentPoints);
        intent.putExtra(tempComp, tempCompHand);
        intent.putExtra(tempHum, tempHumanHand);
        intent.putExtra(tempLay, tempLayout);
        intent.putExtra(tempBone, tempStock);
        intent.putExtra(playerPass, pass);
        intent.putExtra(currPlay, current);
        startActivityForResult(intent,1);
    }

    /**
     Write to a text file
     @param data,
     */
    public void saveRoundGame(Intent data){
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/longanaFiles/";

        File dir = new File(baseDir);

        if(!dir.exists()){
            dir.mkdir();
        }

        File file = new File(baseDir + fileName);

        if(file.exists()){
            file.delete();
        }

        try{
            FileOutputStream output = new FileOutputStream(file);

            // Tournament Score
            output.write("Tournament Score: ".getBytes());
            output.write(Integer.toString(tournamentPoints).getBytes());
            output.write("\r\n".getBytes());

            // Round Number
            output.write("Round No.: ".getBytes());
            output.write(Integer.toString(roundNumber+1).getBytes());
            output.write("\r\n".getBytes());

            // Blank Line
            output.write("\r\n".getBytes());

            // Computer Info
            output.write("Computer: \r\n".getBytes());
            String comp_hand = data.getStringExtra("Computer");
            output.write("   Hand: ".getBytes());
            output.write(comp_hand.getBytes());
            output.write("\r\n".getBytes());
            output.write("   Score: ".getBytes());
            output.write(Integer.toString(computerOverallScore).getBytes());
            output.write("\r\n".getBytes());

            // Blank Line
            output.write("\r\n".getBytes());

            // Human Info
            output.write("Human: \r\n".getBytes());
            String hum_hand = data.getStringExtra("Human");
            output.write("   Hand: ".getBytes());
            output.write(hum_hand.getBytes());
            output.write("\r\n".getBytes());
            output.write("   Score: ".getBytes());
            output.write(Integer.toString(humanOverallScore).getBytes());
            output.write("\r\n".getBytes());

            // Blank Line
            output.write("\r\n".getBytes());

            // Board Info
            output.write("Layout: \r\n".getBytes());
            String board_layout = data.getStringExtra("Board");
            output.write("  L ".getBytes());
            output.write(board_layout.getBytes());
            output.write("R".getBytes());
            output.write("\r\n".getBytes());

            // Blank Line
            output.write("\r\n".getBytes());

            // Boneyard Info
            output.write("Boneyard: \r\n".getBytes());
            String stock_tiles = data.getStringExtra("Stock");
            output.write(stock_tiles.getBytes());
            output.write("\r\n".getBytes());

            // Blank Line
            output.write("\r\n".getBytes());

            // Get the previous player pass
            output.write("Previous Player Passed: ".getBytes());
            boolean first = data.getBooleanExtra("first", false);
            int player = data.getIntExtra("current", 0);
            boolean comp = data.getBooleanExtra("compPass", false);
            boolean hum = data.getBooleanExtra("humPass", false);
            if (first)
            {
                if (player == 1)
                {
                    if (comp)
                    {
                        output.write("Yes\r\n".getBytes());
                    }
                    else
                    {
                        output.write("No\r\n".getBytes());
                    }
                }
                else
                {
                    if (hum)
                    {
                        output.write("Yes\r\n".getBytes());
                    }
                    else
                    {
                        output.write("No\r\n".getBytes());
                    }
                }
            }
            else
            {
                output.write(" \r\n".getBytes());
            }

            // Blank Line
            //output.write("\r\n".getBytes());
            output.write("\r\n".getBytes());

            // Get the next player
            output.write("Next Player: ".getBytes());
            if (first)
            {
                if (player == 0)
                {
                    output.write("Computer\r\n".getBytes());
                }
                else
                {
                    output.write("Human\r\n".getBytes());
                }
            }
            else
            {
                output.write(" \r\n".getBytes());
            }

            output.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     Read from a text file
     @param filepath, a string that holds path to the file
     @return check for file not found exceptions and io exceptions
     */
    public void openRoundGame(String filepath){
        try{
            InputStream input = new FileInputStream(filepath);
            BufferedReader roundInput = new BufferedReader(new InputStreamReader(input));
            try{
                // Read Tournament Score
                String roundLine = roundInput.readLine();
                String[] data = roundLine.split(":");
                tournamentPoints = Integer.parseInt(data[1].trim());

                // Read Round Number
                roundLine = roundInput.readLine();
                data = roundLine.split(":");
                roundNumber = Integer.parseInt(data[1].trim());
                roundNumber -= 1;

                roundInput.readLine();

                // Read Computer Info
                roundInput.readLine();
                // Computer hand
                roundLine = roundInput.readLine();
                data = roundLine.split(":");
                tempCompHand = data[1].trim();
                // Computer Score
                roundLine = roundInput.readLine();
                data = roundLine.split(":");
                computerOverallScore = Integer.parseInt(data[1].trim());

                roundInput.readLine();

                // Read Human Info
                roundInput.readLine();
                // Human hand
                roundLine = roundInput.readLine();
                data = roundLine.split(":");
                tempHumanHand = data[1].trim();
                // human Score
                roundLine = roundInput.readLine();
                data = roundLine.split(":");
                humanOverallScore = Integer.parseInt(data[1].trim());

                roundInput.readLine();

                // Read Board Info
                roundInput.readLine();
                roundLine = roundInput.readLine();
                tempLayout = roundLine.trim();

                roundInput.readLine();

                // Read Boneyard Info
                roundInput.readLine();
                roundLine = roundInput.readLine();
                tempStock = roundLine.trim();

                roundInput.readLine();

                // Read Pass
                roundLine = roundInput.readLine();
                data = roundLine.split(":");
                pass = data[1].trim();

                roundInput.readLine();

                // Read Current
                roundLine = roundInput.readLine();
                data = roundLine.split(":");
                current = data[1].trim();

                roundInput.close();
            }catch(IOException i){
                i.printStackTrace();
            }
        }catch(FileNotFoundException f){
            f.printStackTrace();
        }
    }

    /**
     Display the winner of the tournament
     */
    public void displayTournamentWinner(){
        // Create an alert dialog box
        AlertDialog.Builder winner = new AlertDialog.Builder(TournamentActivity.this);

        // Set the title of the alert dialog box
        winner.setTitle("Winner of the Tournament");

        // The message that will be displayed
        String winnerMessage = tournamentWinner();
        winner.setMessage(winnerMessage);

        // The button of the dialog box, when pressed would close the dialog box
        winner.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(TournamentActivity.this, "The Tournament is Over!", Toast.LENGTH_SHORT).show();
                finish();
                System.exit(0);
            }
        });

        // Display the Dialog box
        winner.show();
    }

    /**
     Quit the Tournament
     */
    public void quitTournament(){
        finish();
        System.exit(0);
    }

    /**
     Check if the tournament is over
     @return true if the tournament is over, false otherwise
     */
    private boolean isTournamentOver(){
        if(tournamentPoints <= humanOverallScore || tournamentPoints <= computerOverallScore){
            tournamentOver = true;
        }
        else{
            tournamentOver = false;
        }

        return tournamentOver;
    }

    /**
     Detemine the winner of the tournament
     @return a string that specifies the winner of the tournament
     */
    private String tournamentWinner(){
        String winnerMessage;

        // The message that will be displayed
        if (humanOverallScore >= tournamentPoints && computerOverallScore >= tournamentPoints)
        {
            if (humanOverallScore >= computerOverallScore)
            {
                winnerMessage = "The winner of the tournament is you!";
            }
            else
            {
                winnerMessage = "The winner of the tournament is the computer!";
            }
        }
        else
        {
            if (humanOverallScore >= tournamentPoints)
            {
                winnerMessage = "The winner of the tournament is you!";
            }
            else
            {
                winnerMessage = "The winner of the tournament is the computer!";
            }
        }

        return winnerMessage;
    }
}