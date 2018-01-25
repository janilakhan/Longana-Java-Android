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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.longana.R;

import com.longana.model.Board;
import com.longana.model.Computer;
import com.longana.model.Hand;
import com.longana.model.Human;
import com.longana.model.Player;
import com.longana.model.Stock;
import com.longana.model.Tile;

import java.util.ArrayDeque;
import java.util.Vector;

/* *******************************
This class will initialize a round
******************************* */

public class RoundActivity extends AppCompatActivity {
    // Variables
    private int r_num;
    private int engine;
    private int currentPlayer;
    private boolean firstPlayer;
    private boolean roundOver;
    private int humanRoundScore;
    private int computerRoundScore;

    private Board board;
    private Stock boneyard;
    private Hand userHand;
    private Player humanPlayer;
    private Player computerPlayer;

    private int left = 2;
    private int right = 2;
    private int top = 0;
    private int bottom  = 0;

    private boolean moveMade = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        // Initialize class Variables
        r_num = getIntent().getIntExtra(TournamentActivity.num, 0);
        roundOver = false;
        humanRoundScore = 0;
        computerRoundScore = 0;

        humanPlayer = new Human();
        computerPlayer = new Computer();
        board = new Board();
        userHand = new Hand();
        boneyard = new Stock();

        // Determine what type of game is this round
        String typeGame = getIntent().getStringExtra(TournamentActivity.game);

        // if it is a new game
        if(typeGame.equalsIgnoreCase("new")){
            // Draw 8 tiles for each player
            int firstDraw = 8;
            boneyard.drawTiles(humanPlayer.getHand(),firstDraw);
            boneyard.drawTiles(computerPlayer.getHand(), firstDraw);

            firstPlayer = false;
        }else{
            // Initialize the players' hand and the board and stock
            String computer = getIntent().getStringExtra(TournamentActivity.tempComp);
            Vector<Tile> compHand = stringToVector(computer);
            computerPlayer.setUserHand(compHand);

            String human = getIntent().getStringExtra(TournamentActivity.tempHum);
            Vector<Tile> humHand = stringToVector(human);
            humanPlayer.setUserHand(humHand);

            String layout = getIntent().getStringExtra(TournamentActivity.tempLay);
            ArrayDeque<Tile> boardLayout = stringToArray(layout);
            board.setBoard(boardLayout);

            String bone = getIntent().getStringExtra(TournamentActivity.tempBone);
            Vector<Tile> boneTiles = stringToVector(bone);
            boneyard.setStock(boneTiles);

            // Determine the current player and if the previous player passed or not
            String current = getIntent().getStringExtra(TournamentActivity.currPlay);
            String pass = getIntent().getStringExtra(TournamentActivity.playerPass);

            if((current.equalsIgnoreCase("")&& pass.equalsIgnoreCase(""))||(current.equalsIgnoreCase(null)&&pass.equalsIgnoreCase(null))){
                firstPlayer = false;
            }else{
                firstPlayer = true;
                if(current.equalsIgnoreCase("Computer")){
                    currentPlayer = 1;
                    if(pass.equalsIgnoreCase("Yes")){
                        board.humanPass = true;
                    }
                    else{
                        board.humanPass = false;
                    }
                }
                else{
                    currentPlayer = 0;
                    if(pass.equalsIgnoreCase("Yes")){
                        board.computerPass = true;
                    }
                    else{
                        board.computerPass = false;
                    }
                }
            }
        }

        createLayout();

        displayComputerHand();

        displayBoard();

        displayHumanHand();

        // If the first player was not found
        if(!firstPlayer){
            // Create an alert dialog box
            final AlertDialog.Builder save = new AlertDialog.Builder(RoundActivity.this);

            // The message that will be displayed
            save.setMessage("Would you like to save?");

            // The button of the dialog box, when pressed would close the dialog box
            save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent saveIntent = new Intent();
                    //saveIntent.putExtra("saveRound", saveRound());
                    saveIntent.putExtra("Board", stringArray(board.getBoard()));
                    saveIntent.putExtra("Stock", stringVector(boneyard.getStock()));
                    saveIntent.putExtra("Computer", stringVector(computerPlayer.getHand()));
                    saveIntent.putExtra("Human", stringVector(humanPlayer.getHand()));
                    saveIntent.putExtra("current", currentPlayer);
                    saveIntent.putExtra("first",firstPlayer);
                    saveIntent.putExtra("humPass", board.didHumanPass());
                    saveIntent.putExtra("compPass", board.didComputerPass());
                    setResult(RESULT_CANCELED, saveIntent);
                    finish();
                }
            });

            save.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    findFirstPlayer();
                }
            });

            // Display the alert dialog box
            save.show();
        }else{
            if(currentPlayer == 1){
                computerMove();
            }
        }
    }

    /**
     Display the Tournament Score and the round number
     */
    public void createLayout(){
        // Display Tournament Score to the screen
        TextView score = findViewById(R.id.score);
        int points = getIntent().getIntExtra(TournamentActivity.t_score, 0);
        score.append(" " + String.valueOf(points));

        // Display the Round Number to the screen
        TextView round = findViewById(R.id.round_num);
        round.append(" " + String.valueOf(r_num + 1));
    }

    /**
     Find the first player
     */
    public void findFirstPlayer(){
        int draw = 1;

        engineNumber();

        // Determine who is the first player
        do {
            if (searchEngine(humanPlayer.getHand())) {
                firstPlayer = true;
                currentPlayer = 0;
                moveMade = true;
                humanPlayer.placeEngine(board, engine);
                Toast.makeText(RoundActivity.this, "Human is the first player", Toast.LENGTH_SHORT).show();
                updateLayout();
            }
            else if(searchEngine(computerPlayer.getHand())) {
                firstPlayer = true;
                currentPlayer = 1;
                computerPlayer.placeEngine(board, engine);
                Toast.makeText(RoundActivity.this, "Computer is the first player", Toast.LENGTH_SHORT).show();
                updateLayout();
            }
            else {
                boneyard.drawTiles(humanPlayer.getHand(), draw);
                boneyard.drawTiles(computerPlayer.getHand(), draw);
            }
        } while (!firstPlayer);
    }

    /**
     Display the tiles in the board
     */
    public void displayBoard(){
        // Where the board will be displayed
        GridLayout b_layout = findViewById(R.id.board_layout);
        b_layout.removeAllViews();

        // The parameters of the Grid Layout
        GridLayout.LayoutParams param;

        int r = 0;
        int c = 0;
        int i = 0;

        for(Tile t : board.getBoard()){
            if (i >= 28) r = 1;
            c = i % 28;

            GridLayout.Spec boardR = GridLayout.spec(r,1);
            GridLayout.Spec boardC = GridLayout.spec(c,1);

            param = new GridLayout.LayoutParams(boardR, boardC);
            param.setMargins(left,top,right,bottom);

            // add tile to the layout
            b_layout.addView(makeTileImage(t),param);
            i++;
        }
    }

    /**
     Display the tiles in the computer hand
     */
    public void displayComputerHand(){
        // Where the computer hand will be displayed
        GridLayout comp = findViewById(R.id.c_hand);
        comp.removeAllViews();

        // The parameters of the Grid Layout
        GridLayout.LayoutParams param;

        int r = 0;
        int c = 0;

        for(int i = 0; i < computerPlayer.getHand().size(); i++){
            if (i >= 20) r = 1;
            c = i % 20;

            GridLayout.Spec boardR = GridLayout.spec(r,1);
            GridLayout.Spec boardC = GridLayout.spec(c,1);

            param = new GridLayout.LayoutParams(boardR, boardC);
            param.setMargins(left,top,right,bottom);

            // add the tile to the layout
            comp.addView(makeTileImage(computerPlayer.getHand().get(i)), param);
        }
    }

    /**
     Display the tiles in the human hand
     */
    public void displayHumanHand(){
        // Where the human hand will be displayed
        GridLayout hum = findViewById(R.id.h_hand);
        hum.removeAllViews();

        // The parameters of the Grid Layout
        GridLayout.LayoutParams param;

        int r = 0;
        int c = 0;

        for(int i = 0; i < humanPlayer.getHand().size(); i++){
            if (i >= 20) r = 1;
            c = i % 20;

            GridLayout.Spec boardR = GridLayout.spec(r,1);
            GridLayout.Spec boardC = GridLayout.spec(c,1);

            param = new GridLayout.LayoutParams(boardR, boardC);
            param.setMargins(left,top,right,bottom);

            // add the tile to the layout
            hum.addView(makeTileButton(humanPlayer.getHand().get(i)), param);
        }
    }

    /**
     Update the layout after evey turn
     */
    public void updateLayout(){
        // update the board, computer hand, human hand
        displayBoard();
        displayHumanHand();
        displayComputerHand();

        // check if the round is over
        if(isRoundOver()){
            displayRoundScore();
            displayPlayerScore();
        }
        else{
            // Display how many tiles and ask the user if they would like to save
            Toast.makeText(RoundActivity.this, "There are " + boneyard.getStock().size() + " tiles in the boneyard.", Toast.LENGTH_SHORT).show();
            saveGame();
        }
    }

    /**
     Make an image view of a tile
     @param t, a tile object
     @return a image view of the tile
     */
    private ImageView makeTileImage(final Tile t){
        ImageView tileImg = new ImageView(this);
        tileImg.setLayoutParams(new ViewGroup.LayoutParams(1, 2));
        tileImg.setBackground(createTileImage(t.getFirst(), t.getSecond()));
        return tileImg;
    }

    /**
     Make an button of a tile
     @param t, a tile object
     @return a button with the background being an image of the tile
     */
    public Button makeTileButton(final Tile t){
        // Create an image button
        Button tileButton = new Button(this);

        tileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // If it is a double or if the computer has passed then the user decided where they would like to place a tile
                if(t.isDouble()|| board.didComputerPass()){
                    // Create an alert dialog box
                    AlertDialog.Builder location = new AlertDialog.Builder(RoundActivity.this);

                    location.setTitle("Location");

                    location.setMessage("Choose a location:");

                    // If the user presses the left button
                    location.setPositiveButton("LEFT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            humanPlayer.addToLeft(board, boneyard, t);
                            String playerMessage = humanPlayer.getMessage();
                            // If the string is null then the move was made
                            if(playerMessage == null){
                                moveMade = true;
                                String message = "The tile " + t.toString() + " was placed to the left side of the board";
                                Toast.makeText(RoundActivity.this, message, Toast.LENGTH_SHORT).show();
                                board.humanPass = false;
                                updateLayout();
                            }
                            else{
                                moveMade = false;
                                Toast.makeText(RoundActivity.this, playerMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // If the user presses the right button
                    location.setNegativeButton("RIGHT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            humanPlayer.addToRight(board, boneyard, t);
                            String playerMessage = humanPlayer.getMessage();
                            // If the string is null then the move was made
                            if(playerMessage == null){
                                moveMade = true;
                                String message = "The tile " + t.toString() + " was placed to the right side of the board";
                                Toast.makeText(RoundActivity.this, message, Toast.LENGTH_SHORT).show();
                                board.humanPass = false;
                                updateLayout();
                            }
                            else{
                                moveMade = false;
                                Toast.makeText(RoundActivity.this, playerMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    location.show();
                }
                else{
                    // if not a double or if the coputer did not pass then the user automatically places on the left side
                    humanPlayer.addToLeft(board, boneyard, t);
                    String playerMessage = humanPlayer.getMessage();
                    // If the string is null then the move was made
                    if(playerMessage == null){
                        moveMade = true;
                        String humanMessage = "The tile " + t.toString() + " was placed to the left side of the board";
                        Toast.makeText(RoundActivity.this, humanMessage, Toast.LENGTH_SHORT).show();
                        board.humanPass = false;
                        updateLayout();
                    }
                    else{
                        moveMade = false;
                        Toast.makeText(RoundActivity.this, playerMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tileButton.setBackground(createTileImage(t.getFirst(),t.getSecond()));
        return tileButton;
    }

    /**
     When the human user presses the pass button
     @param view, a view object
     */
    public void pass(View view){
        humanPlayer.passTurn(board, boneyard);
        Toast.makeText(RoundActivity.this, humanPlayer.getMessage(), Toast.LENGTH_SHORT).show();

        moveMade = true;

        updateLayout();
    }

    /**
     When the human user presses the help button
     @param view, a view object
     */
    public void help(View view){
        humanPlayer.play(board,boneyard);
        Toast.makeText(RoundActivity.this, humanPlayer.getMessage(), Toast.LENGTH_SHORT).show();

        moveMade = false;
        alternatePlayers();
    }

    /**
     The computer action during their turn
     */
    public void computerMove(){
        if(currentPlayer == 1){
            computerPlayer.play(board, boneyard);
            Toast.makeText(RoundActivity.this, computerPlayer.getMessage(), Toast.LENGTH_SHORT).show();

            updateLayout();
        }
    }

    /**
     Ask the user if they would like to save the game
     */
    public void saveGame(){
        // Create an alert dialog box
        final AlertDialog.Builder save = new AlertDialog.Builder(RoundActivity.this);

        // The message that will be displayed
        save.setMessage("Would you like to save?");

        // The button of the dialog box, when pressed would close the dialog box
        save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent saveIntent = new Intent();
                saveIntent.putExtra("Board", stringArray(board.getBoard()));
                saveIntent.putExtra("Stock", stringVector(boneyard.getStock()));
                saveIntent.putExtra("Computer", stringVector(computerPlayer.getHand()));
                saveIntent.putExtra("Human", stringVector(humanPlayer.getHand()));
                saveIntent.putExtra("current", currentPlayer);
                saveIntent.putExtra("first",firstPlayer);
                saveIntent.putExtra("humPass", board.didHumanPass());
                saveIntent.putExtra("compPass", board.didComputerPass());
                setResult(RESULT_CANCELED, saveIntent);
                finish();
            }
        });

        save.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alternatePlayers();
            }
        });

        // Display the alert dialog box
        save.show();
    }

    /**
     Determine and display the winner of the round
     */
    public void displayRoundScore(){
        // Display each user's hand
        String scoreMessage = null;

        // Print out the winner of the round
        // Check if the human or the computer hand are empty
        if (humanPlayer.getHand().isEmpty() || computerPlayer.getHand().isEmpty()) {
            // if the human hand is empty, the human gains the computer hand value
            if (humanPlayer.getHand().isEmpty()) {
                humanRoundScore = userHand.countHand(computerPlayer.getHand());
                scoreMessage = "You win this round and gains " + humanRoundScore + " points.";
            }
            else {
                // if the computer hand is empty, the computer gains the human hand points
                computerRoundScore = userHand.countHand(humanPlayer.getHand());
                scoreMessage = "The computer wins this round and gains " + computerRoundScore + " points.";
            }
        }
        else {
            // if the boneyard is empty and both player pass, calculate both player hand and see who has the smallest sum
            // the smallest sum is the winner of the round
            if (boneyard.getStock().isEmpty() && board.didHumanPass() && board.didComputerPass()) {
                humanRoundScore = userHand.countHand(humanPlayer.getHand());
                computerRoundScore = userHand.countHand(computerPlayer.getHand());

                if (humanRoundScore < computerRoundScore) {
                    humanRoundScore = computerRoundScore;
                    computerRoundScore = 0;
                    scoreMessage = "You win this round and gains " + humanRoundScore + " points.";
                }
                // If tied both players get zero points
                else if (humanRoundScore == computerRoundScore) {
                    humanRoundScore = 0;
                    computerRoundScore = 0;
                    scoreMessage = "This round is a draw since both players have the same amount of points.";
                }
                else {
                    computerRoundScore = humanRoundScore;
                    humanRoundScore = 0;
                    scoreMessage = "The computer wins this round and gains " + computerRoundScore + " points.";
                }
            }
        }

        Toast.makeText(RoundActivity.this, scoreMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     Display both player score during the round
     */
    public void displayPlayerScore(){
        // Create an alert dialog box
        AlertDialog.Builder playerScore = new AlertDialog.Builder(RoundActivity.this);

        // Set the title of the alert dialog box
        playerScore.setTitle("Players Round Score");

        // The message that will be displayed
        String displayMessage = "Human Score: " + humanRoundScore + "\n";
        displayMessage += "Computer Score: " + computerRoundScore + "\n";
        playerScore.setMessage(displayMessage);

        // The button of the dialog box, when pressed would close the dialog box
        playerScore.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("human", humanRoundScore);
                returnIntent.putExtra("computer", computerRoundScore);
                setResult(1, returnIntent);
                finish();
            }
        });

        // Display the alert dialog box
        playerScore.show();
    }

    /**
     Getting the drawable for a tile
     @param firstPip, an integer that holds the first value of a tile
     @param secondPip, an integer that holds the second value of a tile
     @return a bitmap drawable of the tile image
     */
    private Drawable createTileImage(int firstPip, int secondPip){
        String tileName = "tile";

        if(firstPip > secondPip){
            tileName += Integer.toString(secondPip) + Integer.toString(firstPip);
        }
        else {
            tileName += Integer.toString(firstPip) + Integer.toString(secondPip);
        }

        Bitmap img = BitmapFactory.decodeResource(getResources(), this.getResources().getIdentifier(tileName, "drawable", getPackageName()));
        img = img.copy(Bitmap.Config.ARGB_8888, true);

        Matrix rotateImg = new Matrix();
        if(firstPip > secondPip){
            rotateImg.postRotate(180);
        }

        return new BitmapDrawable(this.getResources(), Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), rotateImg, true));
    }

    /**
     Convert a string to a vector of tiles
     @param roundVector, a string representation of a vector of tiles
     @return a vector of tiles
     */
    private Vector<Tile> stringToVector(String roundVector){
        Tile tempTile;

        int first;
        int second;

        String[] tempObj = roundVector.trim().split(" ");
        Vector<Tile> temp = new Vector<>(28);

        for(int i = 0; i < tempObj.length; i++){
            tempObj[i].trim();

            if(tempObj[i].equalsIgnoreCase(" ")){
                continue;
            }

            first = Character.getNumericValue(tempObj[i].charAt(0));
            second = Character.getNumericValue(tempObj[i].charAt(2));

            tempTile = new Tile(first, second);

            temp.add(tempTile);
        }

        return temp;
    }

    /**
     Convert a string to a array deque of tiles
     @param roundBoard, a string representation of a array deque of tiles
     @return an array deque of tiles
     */
    private ArrayDeque<Tile> stringToArray(String roundBoard){
        Tile tempTile;

        int first;
        int second;

        String[] tempBoard = roundBoard.trim().split(" ");
        ArrayDeque<Tile> temp = new ArrayDeque<>(28);

        for(int i = 0; i < tempBoard.length; i++){
            tempBoard[i].trim();

            if(tempBoard[i].equalsIgnoreCase("L")|| tempBoard[i].equalsIgnoreCase("R") ||tempBoard[i].equalsIgnoreCase(" ")){
                continue;
            }else {
                first = Character.getNumericValue(tempBoard[i].charAt(0));
                second = Character.getNumericValue(tempBoard[i].charAt(2));

                tempTile = new Tile(first, second);

                temp.add(tempTile);
            }
        }

        return temp;
    }

    /**
     Convert an array deque of tiles into a string
     @param roundBoard, an array deque
     @return a string representation of an array deque
     */
    private String stringArray(ArrayDeque<Tile>roundBoard){
        StringBuilder boardTiles = new StringBuilder();

        for (Tile t: roundBoard){
            boardTiles.append(t.toString() + " ");
        }

        return boardTiles.toString();
    }

    /**
     Convert a vector of tiles into a string
     @param roundObject, a vector of tiles
     @return a string representation of a vector of tiles
     */
    private String stringVector(Vector<Tile>roundObject){
        StringBuilder roundTile = new StringBuilder();

        for (int i = 0; i < roundObject.size(); i++){
            roundTile.append(roundObject.get(i).toString() + " ");
        }

        return roundTile.toString();
    }

    /**
     Determine the engine for the round
     */
    private void engineNumber(){
        int mod;

        mod = r_num % 7;

        switch (mod) {
            case 1:
                engine = 5;
                break;
            case 2:
                engine = 4;
                break;
            case 3:
                engine = 3;
                break;
            case 4:
                engine = 2;
                break;
            case 5:
                engine = 1;
                break;
            case 6:
                engine = 0;
                break;
            default:
                engine = 6;
                break;
        }
    }

    /**
     Search for the engine in the user hand
     @param player, a vector of tiles which holds a player's hand
     @return true if the engine is found, false otherwise
     */
    private boolean searchEngine(Vector<Tile> player){
        // Initialize temp
        Tile temp = new Tile(engine, engine);

        // Search through the vector for the tile
        for (int i = 0; i < player.size(); i++)
        {
            // Return true if found
            if(player.elementAt(i).equals(temp)){
                return true;
            }
        }

        // If not found, return false
        return false;
    }

    /**
     Alternate the players
     */
    private void alternatePlayers(){
        if(currentPlayer == 0){
            if(moveMade){
                currentPlayer = 1;
                Toast.makeText(RoundActivity.this, "Computer Turn!", Toast.LENGTH_SHORT).show();
                computerMove();
            }
            else{
                currentPlayer = 0;
                Toast.makeText(RoundActivity.this, "Your Turn!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            currentPlayer = 0;
            Toast.makeText(RoundActivity.this, "Your Turn!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     Check to see if the round is over
     @return true if the round is over, false otherwise
     */
    private boolean isRoundOver(){
        if (humanPlayer.getHand().isEmpty() || computerPlayer.getHand().isEmpty()) {
            roundOver = true;
        }
        else
            roundOver = boneyard.getStock().isEmpty() && board.didHumanPass() && board.didComputerPass();

        return roundOver;
    }
}