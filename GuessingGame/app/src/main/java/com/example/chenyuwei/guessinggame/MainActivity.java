package com.example.chenyuwei.guessinggame;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText txtGuess;
    private Button btnGuess;
    private TextView lblOutput;
    private int theNumber;
    private int numberOfTries = 0;
    private int limitOfTries;
    private int range = 100;
    private TextView lblRange;

    public  void checkGuess(){
        String guessText = txtGuess.getText().toString();
        String message = "";

        try{
            if(numberOfTries < limitOfTries) {
                int guess = Integer.parseInt(guessText);
                numberOfTries += 1;
                if (guess < theNumber) {
                    message = guess + " is too low. Try again.";
                } else if (guess > theNumber) {
                    message = guess + " is too high. Try again.";
                } else {
                    message = guess + " is correct. You win after " + numberOfTries + " tries!";
                    numberOfTries = 0;

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    int gamesWon = preferences.getInt("gameWon", 0) + 1;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("gameWon", gamesWon);
                    editor.apply();

                   // System.out.println(gamesWon);

                    newGame();
                }
            }
            else{
                message = "Oops! You failed.";
                numberOfTries = 0;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesFailed = preferences.getInt("gameFailed", 0) + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("gameFailed", gamesFailed);
                editor.apply();

               // System.out.println(gamesFailed);

                newGame();
            }
        } catch (Exception e){
            message = "Enter a number between 1 and " + range + ".";

        } finally {
            lblOutput.setText(message);
            txtGuess.requestFocus();
            txtGuess.selectAll();
        }
    }

    public void newGame(){
        theNumber = (int)(Math.random() * range + 1);
        lblRange.setText("Enter a number between 1 and " + range + ".");
        txtGuess.setText("" + range/2);
        txtGuess.requestFocus();
        txtGuess.selectAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGuess = (EditText) findViewById(R.id.txtGuess); // R stands for R.java and short for Resources
        btnGuess = (Button) findViewById(R.id.btnGuess);
        lblOutput = (TextView) findViewById(R.id.lblOutput);
        lblRange = (TextView) findViewById(R.id.textView2);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        range = preferences.getInt("range", 100);
        limitOfTries = (int) (Math.log(range)/Math.log(2)+1);

        newGame();

        btnGuess.setOnClickListener(new View.OnClickListener(){
            @Override  //compiler directive
            public void onClick(View v){
                checkGuess();
            }
        });


        txtGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                checkGuess();
                return true;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                final CharSequence[] items = {"1 to 10", "1 to 100", "1 to 1000"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select the Range:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                range = 10;
                                storeRange(10);
                                newGame();
                                break;

                            case 1:
                                range = 100;
                                storeRange(100);
                                newGame();
                                break;

                            case 2:
                                range = 1000;
                                storeRange(1000);
                                newGame();
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;


            case R.id.action_newgame:
                newGame();
                return true;


            case R.id.action_gamestats:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gameWon",0);
                int gamesFailed = preferences.getInt("gameFailed", 0);

                int totalOfGames = gamesWon + gamesFailed;
                DecimalFormat df = new DecimalFormat("#%");  // Convert digits to percentage format

                AlertDialog statDialog = new AlertDialog.Builder(MainActivity.this).create();
                statDialog.setTitle("Guessing Game Stats");
                statDialog.setMessage("You have won " + gamesWon + " out of " + totalOfGames + " games, " + df.format((float) gamesWon/totalOfGames) + "! Way to go!");
                statDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                statDialog.show();
                return true;


            case R.id.action_about:
                AlertDialog aboutDialog = new AlertDialog.Builder(MainActivity.this).create();
                aboutDialog.setTitle("About Guessing Game");
                aboutDialog.setMessage("(c)2018 Yuwei");
                aboutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                aboutDialog.show();
                return true;


            default:
                return super.onOptionsItemSelected((item));
        }
    }

    private void storeRange(int newRange) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("range", newRange);
        editor.apply();
    }

}
