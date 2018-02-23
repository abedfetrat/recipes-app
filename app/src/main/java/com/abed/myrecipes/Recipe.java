package com.abed.myrecipes;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Abed on 02/05/2018.
 */

public class Recipe {

    private int id;
    private String title;
    private String imageUri;
    private String description;
    private String ingredients;
    private String directions;
    private String nutritions;

    public Recipe(int id, String title, String imageUri, String description, String ingredients,
                  String directions, String nutritions) {
        this.id = id;
        this.title = title;
        this.imageUri = imageUri;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.nutritions = nutritions;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public String getNutritions() {
        return nutritions;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public void setNutritions(String nutritions) {
        this.nutritions = nutritions;
    }
}
