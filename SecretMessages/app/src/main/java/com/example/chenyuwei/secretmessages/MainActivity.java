package com.example.chenyuwei.secretmessages;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText txtIn;
    EditText txtKey;
    EditText txtOut;
    SeekBar sb;
    Button btn;
    Button btnMoveUp;

    public String encode(String message, int keyVal){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < message.length(); i++){
            char c = message.charAt(i);
            if(c >= 'A' && c <= 'Z'){
                c += keyVal;
                if(c > 'Z')
                    c -= 26;
                if(c < 'A')
                    c += 26;
            }
            else if(c >= 'a' && c <= 'z'){
                c += keyVal;
                if(c > 'z')
                    c -= 26;
                if(c < 'a')
                    c += 26;
            }
            else if(c >= '0' && c <= '9'){
                c += keyVal%10;
                if(c > '9')
                    c -= 10;
                if(c < '0')
                    c += 10;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtIn = (EditText)findViewById(R.id.txtIn);
        txtKey = (EditText)findViewById(R.id.txtKey);
        txtOut = (EditText)findViewById(R.id.txtOut);
        sb = (SeekBar)findViewById(R.id.seekBar) ;
        btn = (Button)findViewById(R.id.button);
        btnMoveUp = (Button)findViewById(R.id.btnMoveUp);

        Intent receivedIntent = getIntent(); //accept the incom- ing Intent message shared from another app.
        String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT); //get the text from the Intent message by calling the getStringExtra() method
        if(receivedText != null)
            txtIn.setText(receivedText);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int key = Integer.parseInt(txtKey.getText().toString());
                String message = txtIn.getText().toString();
                String output = encode(message,key);
                txtOut.setText(output);
                sb.setProgress(key+13);
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int key = sb.getProgress() - 13;
                String message = txtIn.getText().toString();
                String output = encode(message,key);
                txtOut.setText(output);
                txtKey.setText(""+key);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnMoveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enCodedMessage = txtOut.getText().toString();
                int key = Integer.parseInt(txtKey.getText().toString());
                txtIn.setText(enCodedMessage);
                sb.setProgress(13-key);
                txtKey.setText(-key + "");


                String originalMessage = encode(enCodedMessage, 13-key);
                txtOut.setText(originalMessage);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent((Intent.ACTION_SEND));
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Secret Message " + DateFormat.getDateTimeInstance().format(new Date()));
                shareIntent.putExtra(Intent.EXTRA_TEXT, txtOut.getText().toString());
                try {
                    startActivity(Intent.createChooser(shareIntent,"Share message..."));
                    finish();
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(MainActivity.this,"Error: Couldn't share.",Toast.LENGTH_SHORT).show();
                }

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
