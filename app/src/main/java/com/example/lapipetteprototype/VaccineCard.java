package com.example.lapipetteprototype;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VaccineCard extends AppCompatActivity implements View.OnClickListener {
    private int numberOfCards, currentCardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccine_card);

        // get extras -> numberOfCards, currentCardNumber
        Bundle extras = getIntent().getExtras();
        this.numberOfCards = Integer.parseInt(extras.getString("numberOfCards"));
        this.currentCardNumber = Integer.parseInt(extras.getString("currentCardNumber"));

        // Check whether number is valid, otherwise correct it
        if (this.currentCardNumber > this.numberOfCards) {
            this.currentCardNumber = 0;     // start over again with 0
        }
        else if (this.currentCardNumber < 0) {
            this.currentCardNumber = numberOfCards; // go backwards
        }

        setVaccineCard(this.numberOfCards, this.currentCardNumber);

        Button nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);
        Button prevBtn = findViewById(R.id.prev_btn);
        prevBtn.setOnClickListener(this);
        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
        ImageView vaccineCard = findViewById(R.id.vaccine_card_image);
        vaccineCard.setOnClickListener(this);

        setVaccineCardSwipeListeners();     // Swipe listeners for VaccineCard
    }

    /**
     * Loads and displays the info-card of the vaccine according the given input variables
     * @param numberOfCards Total number of available vaccines (or their info cards)
     * @param currentCardNumber The desired vaccine (they are numbered)
     */
    private void setVaccineCard(int numberOfCards, int currentCardNumber) {
        // Create URI for image
        // e.g. "https://raw.githubusercontent.com/KuechlerO/LaPipette-VaccineCards/main/names_transformed/vaccines_cards_01.png";
        String imageUri = "https://raw.githubusercontent.com/KuechlerO/LaPipette-VaccineCards/main/names_transformed/vaccines_cards_";
        if (currentCardNumber < 10) {
            imageUri = imageUri + "0" + currentCardNumber + ".png";       // leading 0
        }
        else {
            imageUri = imageUri + currentCardNumber + ".png";
        }

        // ------- Fill imageView ---------------
        ImageView vaccineCard = (ImageView) findViewById(R.id.vaccine_card_image);
        Picasso.get().setLoggingEnabled(true);      // For debugging
        int screen_width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screen_height = Resources.getSystem().getDisplayMetrics().heightPixels;
        Picasso.get()
                .load(imageUri)
                // TODO -> add other image for error
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.progress_animation)
                .resize(screen_width, screen_height)
                //.fit()
                //.centerInside()

                // Settings needed for animation as well as setting in imageView: scaleType = centerInside
                .centerCrop()
                .noFade()

                .into(vaccineCard);
    }


    private void setVaccineCardSwipeListeners() {
        // Also set on click listener
        ImageView vaccineCard = findViewById(R.id.vaccine_card_image);
        // vaccineCard.setOnClickListener(this);

        vaccineCard.setOnTouchListener(new OnSwipeTouchListener() {
            public boolean onSwipeRight() {
                findViewById(R.id.prev_btn).performClick();
                return true;
            }
            public boolean onSwipeLeft() {
                findViewById(R.id.next_btn).performClick();
                return true;
            }
        });
    }


    /**
     * Button Listeners -> For next- and prev-button
     * -> increments/decrements number of current vaccine
     * -> passes info on in extras-attributes
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_btn) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
        // touch on vaccine-card -> buttons are fade out/int
        else if (view.getId() == R.id.vaccine_card_image) {
            ArrayList<View> viewsToFadeIn = new ArrayList<View>();

            viewsToFadeIn.add(findViewById(R.id.next_btn));
            viewsToFadeIn.add(findViewById(R.id.prev_btn));
            viewsToFadeIn.add(findViewById(R.id.back_btn));

            for (View v : viewsToFadeIn) {
                v.animate().alpha(1.0f - v.getAlpha()).setDuration(500).start();
            }
        }
        else {
            Intent vaccineCardsAct = new Intent(this, VaccineCard.class);

            // Pass new currentCardNumber
            switch (view.getId()) {
                case R.id.next_btn:
                    vaccineCardsAct.putExtra("currentCardNumber", Integer.toString(this.currentCardNumber+1));
                    break;
                case R.id.prev_btn:
                    vaccineCardsAct.putExtra("currentCardNumber", Integer.toString(this.currentCardNumber-1));
                    break;
                default:
                    // TODO -> catch error?!
                    break;
            }
            vaccineCardsAct.putExtra("numberOfCards", Integer.toString(this.numberOfCards));
            startActivity(vaccineCardsAct);
        }
    }
}
