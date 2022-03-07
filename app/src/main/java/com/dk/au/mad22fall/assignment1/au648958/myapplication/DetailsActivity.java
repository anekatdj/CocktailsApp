package com.dk.au.mad22fall.assignment1.au648958.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    Button btnBack, btnSave;
    TextView txtDrinkName, txtDrinkCategory, txtIngredients, txtInstructions, txtUserRating;
    SeekBar skbRating;
    ImageView imgDrink;

    double rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        txtDrinkName = findViewById(R.id.txtDrinkName);
        txtDrinkCategory = findViewById(R.id.txtDrinkCategory);
        txtIngredients = findViewById(R.id.txtIngredients);
        txtInstructions = findViewById(R.id.txtInstructions);
        txtUserRating = findViewById(R.id.txtUserRating);
        skbRating = findViewById(R.id.skbRating);
        imgDrink = findViewById(R.id.imgDrinkChosen);

        btnBack.setOnClickListener(view -> cancel());
        btnSave.setOnClickListener(view -> save());

        Intent data = getIntent();
        txtDrinkName.setText(data.getStringExtra(Constants.NAME));
        txtDrinkCategory.setText(data.getStringExtra(Constants.CATEGORY));
        txtInstructions.setText(formatInstructions(data.getStringExtra(Constants.INSTRUCTIONS)));
        txtIngredients.setText(formatIngredients(data.getStringArrayListExtra(Constants.MEASURES),data.getStringArrayListExtra(Constants.INGREDIENTS)));
        txtUserRating.setText("" + data.getDoubleExtra(Constants.RATING,0.0));

        //Converting back to 0-100 scale
        rating = data.getDoubleExtra(Constants.RATING,0.0)*10;
        skbRating.setProgress((int) rating);

        //Setting drink image - again inspired by https://stackoverflow.com/questions/56560897/how-to-get-drawable-from-string/56561415
        String drinkName = data.getStringExtra(Constants.NAME).toLowerCase().replace(' ','_');
        Context context = imgDrink.getContext();
        imgDrink.setImageResource(context.getResources().getIdentifier(drinkName,"drawable", context.getPackageName()));

        skbRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //SeekBar with decimal values: https://stackoverflow.com/questions/6197674/seekbar-with-decimal-values
                rating = ((double) i/10);
                txtUserRating.setText("" + rating);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //Inspired by Kasper Løvborg L2 ActivityResultLauncher Video
    private void save() {
        //Using Activity Launcher
        Intent intent = new Intent();
        intent.putExtra(Constants.RATING, rating);
        intent.putExtra(Constants.NAME,txtDrinkName.getText());
        setResult(RESULT_OK,intent);
        finish();
    }

    //Inspired by Kasper Løvborg L2 ActivityResultLauncher Video
    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private String formatIngredients(ArrayList<String> measuresData, ArrayList<String> ingredientsData){
        //Building ingredients string
        ArrayList<String> measures = measuresData;
        ArrayList<String> ingredients = ingredientsData;

        String measureList = "";
        for (int i = 0; i < measures.size(); i++){
            String measure = measures.get(i);
            String ingredient = ingredients.get(i);
            if(!measure.equals("null") || !ingredient.equals("null")){
                //Line separator: https://stackoverflow.com/questions/2840608/how-do-i-add-a-newline-to-a-textview-in-android
                measureList += (measure + " " + ingredient + System.getProperty("line.separator"));
            }
        }
        return measureList;
    }

    private String formatInstructions(String instructions){
        //replace not working: https://stackoverflow.com/questions/15450519/why-does-string-replace-not-work/15450539
        instructions = instructions.replaceAll("\\\\r\\\\n\\\\r\\\\n",System.getProperty("line.separator"));
        return instructions;
    }


}