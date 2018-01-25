/**
 ***********************************************************
 * Name:  Janila Khan                                      *
 * Project : Project 2 / Longana Game			          *
 * Class : CMPS 366 Organization of Programming Languages  *
 * Date : December 5, 2017                                 *
 ***********************************************************
 */

package com.longana.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.longana.R;

import java.io.File;

/* ***********************************************************
This class starts the type of game the user would like to play
*********************************************************** */

public class MainActivity extends AppCompatActivity {

    // Class Constants
    public static final String tournament_game = "game";
    public static final String tournament_score = "score";
    public static final String tournament_file = "file";

    // Class Variables
    private static int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     This function will create an alert dialog box that will ask for a tournament score needed for a new tournament
     @param view Holds the view
     */
    public void newGame(View view){
        // Alert Dialog box that will ask for the tournament score
        final AlertDialog.Builder t_input = new AlertDialog.Builder(MainActivity.this);

        // The Title of the Alert Dialog
        t_input.setTitle("Tournament Score");

        // The Message that will be seen as the Alert Dialog is open
        t_input.setMessage("Enter Tournament Score:");

        // The input the Alert Dialog will take
        final EditText input = new EditText(this);
        // The Alert Dialog will only take in integer values
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        // The Keyboard will only show the number keypad
        input.setRawInputType(Configuration.KEYBOARD_12KEY);

        // View what the user inputs
        t_input.setView(input);

        // Press Enter when you are finishing entering the tournament score, when clicked a new activity will start
        t_input.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "A value has not been inputted!", Toast.LENGTH_LONG).show();
                }
                else{
                    // Validate input that is given by the user
                    score = Integer.parseInt(input.getText().toString());

                    // If score is equal to zero give an error
                    if (score == 0){
                        Toast.makeText(MainActivity.this, "You have inputted a zero which is not a valid input!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        // Star a new activity
                        Intent intent = new Intent(MainActivity.this, TournamentActivity.class);
                        intent.putExtra(tournament_game, "new");
                        intent.putExtra(tournament_score, score);
                        startActivity(intent);
                    }
                }
            }
        });

        // Press Cancel if you have decided not to play a new game
        t_input.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        // Show the Alert Dialog
        t_input.show();
    }

    /**
     This function will create an alert dialog box that will hold a list of saved game files
     @param view Holds the view
     */
    public void savedGame(View view){
        // When clicked ask for the file name of the saved file
        final AlertDialog.Builder openFile = new AlertDialog.Builder(MainActivity.this);

        // The Title of the Alert Dialog
        openFile.setTitle("Select a Game:");

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/longanaFiles";

        File dir = new File(path);

        File[] filesList = dir.listFiles();

        String[] savefiles = new String[filesList.length];
        for(int i = 0; i < filesList.length; i++){
            savefiles[i] = filesList[i].getName();
        }

        openFile.setItems(savefiles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ListView listView = ((AlertDialog)dialogInterface).getListView();
                String fileName = listView.getAdapter().getItem(i).toString();

                // Star a new activity
                Intent intent = new Intent(MainActivity.this, TournamentActivity.class);
                intent.putExtra(tournament_game, "save");
                intent.putExtra(tournament_file, fileName);
                startActivity(intent);
            }
        });

        openFile.show();
    }
}
