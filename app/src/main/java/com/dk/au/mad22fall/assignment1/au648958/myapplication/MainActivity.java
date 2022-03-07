package com.dk.au.mad22fall.assignment1.au648958.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DrinkAdapter.IDrinkItemClickedListener{

    //ICON LAYOUT: https://learntodroid.com/how-to-add-a-gradient-background-to-an-android-app/
    //Look in 'ic_drink_app_launcer_background.xml'
    //Gradients: https://uigradients.com/#DanceToForget

    private DrinksViewModel viewModel;
    private Button btnExit;

    private RecyclerView rcvDrink;
    private DrinkAdapter adapter;

    private TextView txtRating;

    private ArrayList<Drink> drinks;

    //Launcher
    ActivityResultLauncher<Intent> launcher;

    String drinkRated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ViewModel
        viewModel = new ViewModelProvider(this).get(DrinksViewModel.class);
        viewModel.getDrinks(this);

        btnExit = findViewById(R.id.btnExit);
        rcvDrink = findViewById(R.id.rcvDrinks);

        adapter = new DrinkAdapter(this);
        rcvDrink.setLayoutManager(new LinearLayoutManager(this));
        rcvDrink.setAdapter(adapter);

        drinks = viewModel.getDrinks(this).getValue();
        adapter.updateDrinkList(drinks);

        btnExit.setOnClickListener(view -> { exit(); });


        //get data from intent
        Intent data = getIntent();
        String rating = data.getStringExtra(Constants.RATING);
        if (rating==null){
            rating = "0.0";
        }

        //Launcher - Inspired by Kasper Løvborg - L2 Video about ActivityResult
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== RESULT_OK){
                    double rating = result.getData().getDoubleExtra(Constants.RATING,0.0);

                    drinkRated = result.getData().getStringExtra(Constants.NAME);

                    adapter.updateDrinkList(viewModel.updateDrinkRating(rating, result.getData().getStringExtra(Constants.NAME)));

                    drinks = viewModel.getDrinks(getApplicationContext()).getValue();
                }else{
                    Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Copied from https://stackoverflow.com/questions/6014028/closing-application-with-exit-button
    private void exit() {
        finish();
        System.exit(0);
    }

    //Inspired by Kasper Løvborg - L2 Video Intents
    @Override
    public void onDrinkClicked(int index) {
        Intent intent = new Intent(this, DetailsActivity.class);

        Drink drink = drinks.get(index);

        intent.putExtra(Constants.NAME,drink.name);
        intent.putExtra(Constants.CATEGORY,drink.category);
        intent.putExtra(Constants.INGREDIENTS,drink.ingredients);
        intent.putExtra(Constants.MEASURES,drink.measure);
        intent.putExtra(Constants.INSTRUCTIONS,drink.instruction);
        intent.putExtra(Constants.RATING,drink.rating);

        launcher.launch(intent);
    }
}