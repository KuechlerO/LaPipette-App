package com.example.lapipetteprototype;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String numberOfCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setNumberOfCards();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("I got the number: " + numberOfCards);
        // TODO if -1 ... -> No connection

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
    }

    /**
     * Listener for Start-Button
     * @param view
     */
    public void onClick(View view) {
        Intent vaccineCardsAct = new Intent(this, VaccineCard.class);
        vaccineCardsAct.putExtra("numberOfCards", this.numberOfCards);
        vaccineCardsAct.putExtra("currentCardNumber", "0"); // start-nr of cards
        startActivity(vaccineCardsAct);
    }

    /**
     * Creates a parallel thread to open a StringStream to the index.txt file
     * -> returning the total number of available info cards (first line in text-file)
     * @return String number of totally available cards
     */
    private void setNumberOfCards() { // TODO display warning if no Internet-Connection
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    InputStream input = new URL("https://raw.githubusercontent.com/KuechlerO/LaPipette-VaccineCards/main/index.txt").openStream();
                    InputStreamReader reader = new InputStreamReader(input);
                    numberOfCards = "";
                    int data = reader.read();
                    while(data != -1){
                        char currentChar = (char) data;
                        if (currentChar != '\n' && currentChar != '\r') {
                            numberOfCards = numberOfCards + currentChar;
                        }
                        else {
                            break;
                        }
                        data = reader.read();
                    }
                    reader.close();
                    System.out.println("Yes, finished: " + numberOfCards);
                } catch (Exception e) {
                    e.printStackTrace();
                    numberOfCards = "-1";
                }
            }
        });
        thread.start();
    }
}
