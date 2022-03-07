package com.dk.au.mad22fall.assignment1.au648958.myapplication;

import java.util.ArrayList;
import java.util.List;

//DTO
public class Drink {
    public String name;
    public String category;
    public ArrayList<String> measure;
    public ArrayList<String> ingredients;
    public String instruction;
    public double rating;

    public Drink(String name, String category, ArrayList<String> measure, ArrayList<String> ingredients, String instruction, double rating){
        this.name = name;
        this.category = category;
        this.measure = measure;
        this.ingredients = ingredients;
        this.instruction = instruction;
        this.rating = rating;
    }
}
