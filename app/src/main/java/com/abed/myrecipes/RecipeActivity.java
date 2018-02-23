package com.abed.myrecipes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class RecipeActivity extends AppCompatActivity {

    private ImageView imv;

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imv = findViewById(R.id.toolbar_imageView);
        TextView tvDescription = findViewById(R.id.tv_description);
        TextView tvIngredients = findViewById(R.id.tv_ingredients);
        TextView tvDirections = findViewById(R.id.tv_directions);
        TextView tvNutritons = findViewById(R.id.tv_nutritions);

        setRecipe();

        setRecipeImage();
        toolbar.setTitle(recipe.getTitle());
        tvDescription.setText(recipe.getDescription());
        tvIngredients.setText(recipe.getIngredients());
        tvDirections.setText(recipe.getDirections());
        tvNutritons.setText(recipe.getNutritions());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecipe() {
        Intent data = getIntent();

        String title = data.getStringExtra(getString(R.string.key_recipe_title));
        String imageUri = data.getStringExtra(getString(R.string.key_recipe_image_uri));
        String description = data.getStringExtra(getString(R.string.key_recipe_description));
        String ingredients = data.getStringExtra(getString(R.string.key_recipe_ingredients));
        String directions = data.getStringExtra(getString(R.string.key_recipe_directions));
        String nutritions = data.getStringExtra(getString(R.string.key_recipe_nutritions));

        recipe = new Recipe(1, title, imageUri, description, ingredients, directions, nutritions);
    }

    private void setRecipeImage() {
        Bitmap bmp = null;
        try {
            Uri uri = Uri.parse(recipe.getImageUri());
            bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }

        if (bmp != null)
            imv.setImageBitmap(bmp);
    }
}
