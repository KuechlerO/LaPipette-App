package com.example.lapipetteprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String numberOfCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberOfCards = getNumberOfCards();
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
     * Functions downloads the index.txt file from repository and extracts the first line
     * -> returning the total number of available info cards
     * @return String number of totally available cards
     */
    private String getNumberOfCards() { // TODO display warning if no Internet-Connection
        // --- loading index ----
        Uri indexUri = Uri.parse("https://raw.githubusercontent.com/KuechlerO/LaPipette-VaccineCards/main/index.txt");
        DownloadManager.Request r = new DownloadManager.Request(indexUri);

        // This put the download in the same Download dir the browser uses
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "index");
        //r.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "upload");
        r.allowScanningByMediaScanner();


        // Start download
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(r);

        String numberOfCards = "-1";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "index")));
            numberOfCards = reader.readLine();
            reader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return numberOfCards;
    }
}
