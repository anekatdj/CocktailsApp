package com.dk.au.mad22fall.assignment1.au648958.myapplication;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrinksViewModel extends ViewModel {

    //ViewModel inspired by https://developer.android.com/topic/libraries/architecture/viewmodel
    MutableLiveData<ArrayList<Drink>> drinks;

    public LiveData<ArrayList<Drink>> getDrinks(Context context) {
        if(drinks==null){
            drinks = new MutableLiveData<>(loadDrinks(context));
        }

        return drinks;
    }

    //Following method is copied from https://javapapers.com/android/android-read-csv-file/
    public ArrayList<Drink> loadDrinks(Context context){
        ArrayList<Drink> drinkLists = new ArrayList<Drink>();
        InputStream inputStream = context.getResources().openRawResource(R.raw.drinks_data);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String csvLine = reader.readLine();

            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(";");

                drinkLists.add(createDrink(row));
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }

        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return drinkLists;
    }

    private Drink createDrink(String[] drinkData){
        String name = drinkData[0];
        String category = drinkData[1];
        ArrayList<String> measure = new ArrayList(Arrays.asList(drinkData[2],drinkData[3],drinkData[4],drinkData[5]));
        ArrayList<String> ingredients = new ArrayList(Arrays.asList(drinkData[6],drinkData[7],drinkData[8],drinkData[9]));
        String instruction = drinkData[10];
        double rating = 0;

        Drink drink = new Drink(name,category,measure,ingredients,instruction,rating);

        return drink;
    }

    public ArrayList<Drink> updateDrinkRating(double rating, String name){
        //https://stackoverflow.com/questions/42246599/is-there-a-way-to-get-an-arraylist-element-by-its-name
        for(Drink drinkObj : drinks.getValue()) {
            if(drinkObj.name.equals(name)) {
                drinkObj.rating = rating;
            }
        }
        return drinks.getValue();
    }
}
