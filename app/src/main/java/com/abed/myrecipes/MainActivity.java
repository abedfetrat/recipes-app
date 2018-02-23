package com.abed.myrecipes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CREATE_RECIPE_REQUEST = 1;
    private static final int EDIT_RECIPE_REQUEST = 2;
    private static final String KEY_APP_PREF_FILE = "app_prefs";
    private static final String KEY_RECIPES= "recipes";
    private static final String KEY_FIRST_RUN = "first_run";
    private SharedPreferences prefs;
    private Gson gson;
    private List<Recipe> recipes;
    private RecipesListAdapter adapter;
    private Dialog dialog;
    private int longClickedListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_main));
        setSupportActionBar(toolbar);

        ListView lv = findViewById(R.id.listView);
        FloatingActionButton fab = findViewById(R.id.fab);

        prefs = getSharedPreferences(KEY_APP_PREF_FILE, MODE_PRIVATE);
        gson = new Gson();
        recipes = new ArrayList<>();

        loadRecipes();

        adapter = new RecipesListAdapter(this, recipes);
        lv.setAdapter(adapter);

        createDialog();

        boolean isFirstRun = prefs.getBoolean(KEY_FIRST_RUN, true);
        if (isFirstRun) {
            createDemoRecipes();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_FIRST_RUN, false);
            editor.apply();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewRecipe(position);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedListItem = position;
                dialog.show();
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddRecipeActivity.class);
                startActivityForResult(intent, CREATE_RECIPE_REQUEST);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveRecipes();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_RECIPE_REQUEST) {
            if (resultCode == RESULT_OK) {
                createRecipeAndAddToList(data);
            }
        } else if(requestCode == EDIT_RECIPE_REQUEST) {
            if (resultCode == RESULT_OK) {
                updateRecipe(data);
            }
        }
    }

    private void saveRecipes() {
        Log.d("MainActivity", "Saving recipes...");
        SharedPreferences.Editor editor = prefs.edit();
        String recipesJson = gson.toJson(recipes);
        editor.putString(KEY_RECIPES, recipesJson);
        editor.apply();
    }

    private void loadRecipes() {
        Log.d("MainActivity", "Loading recipes...");
        String recipesJson = prefs.getString(KEY_RECIPES, null);
        if (recipesJson != null) {
            Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
            List<Recipe> r = gson.fromJson(recipesJson, listType);
            recipes.clear();
            recipes.addAll(r);
        }
    }

    private void createDemoRecipes() {
        String imageUri = "android.resource://" + getPackageName() + "/drawable/";

        // Easy pancakes
        Recipe pancake = new Recipe(1, getString(R.string.demo_recipe_1_title), imageUri+"pancake", getString(R.string.demo_recipe_1_description),
                getString(R.string.demo_recipe_1_ingredients), getString(R.string.demo_recipe_1_directions), getString(R.string.demo_recipe_1_nutritions));
        
        recipes.add(pancake);
        adapter.notifyDataSetChanged();
    }

    private void createRecipeAndAddToList(Intent data) {
        int id = recipes.size() + 1;
        String title = data.getStringExtra(getString(R.string.key_recipe_title));
        String imageUri = data.getStringExtra(getString(R.string.key_recipe_image_uri));
        String description = data.getStringExtra(getString(R.string.key_recipe_description));
        String ingredients = data.getStringExtra(getString(R.string.key_recipe_ingredients));
        String directions = data.getStringExtra(getString(R.string.key_recipe_directions));
        String nutritions = data.getStringExtra(getString(R.string.key_recipe_nutritions));

        Recipe recipe = new Recipe(id, title, imageUri, description, ingredients, directions, nutritions);
        recipes.add(0, recipe);
        //Collections.reverse(recipes);
        adapter.notifyDataSetChanged();
    }

    private void updateRecipe(Intent data) {
        int i = longClickedListItem;
        recipes.get(i).setTitle(data.getStringExtra(getString(R.string.key_recipe_title)));
        recipes.get(i).setImageUri(data.getStringExtra(getString(R.string.key_recipe_image_uri)));
        recipes.get(i).setDescription(data.getStringExtra(getString(R.string.key_recipe_description)));
        recipes.get(i).setIngredients(data.getStringExtra(getString(R.string.key_recipe_ingredients)));
        recipes.get(i).setDirections(data.getStringExtra(getString(R.string.key_recipe_directions)));
        recipes.get(i).setNutritions(data.getStringExtra(getString(R.string.key_recipe_nutritions)));
        adapter.notifyDataSetChanged();    
    }

    private void viewRecipe(int position) {
        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
        intent.putExtra(getString(R.string.key_recipe_title), recipes.get(position).getTitle());
        intent.putExtra(getString(R.string.key_recipe_image_uri), recipes.get(position).getImageUri());
        intent.putExtra(getString(R.string.key_recipe_description), recipes.get(position).getDescription());
        intent.putExtra(getString(R.string.key_recipe_ingredients), recipes.get(position).getIngredients());
        intent.putExtra(getString(R.string.key_recipe_directions), recipes.get(position).getDirections());
        intent.putExtra(getString(R.string.key_recipe_nutritions), recipes.get(position).getNutritions());
        startActivity(intent);
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.dialog_items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //Edit
                        int i = longClickedListItem;
                        Intent intent = new Intent(getApplicationContext(), AddRecipeActivity.class);
                        intent.putExtra(getString(R.string.key_recipe_title), recipes.get(i).getTitle());
                        intent.putExtra(getString(R.string.key_recipe_image_uri), recipes.get(i).getImageUri());
                        intent.putExtra(getString(R.string.key_recipe_description), recipes.get(i).getDescription());
                        intent.putExtra(getString(R.string.key_recipe_ingredients), recipes.get(i).getIngredients());
                        intent.putExtra(getString(R.string.key_recipe_directions), recipes.get(i).getDirections());
                        intent.putExtra(getString(R.string.key_recipe_nutritions), recipes.get(i).getNutritions());
                        intent.putExtra("request_code", EDIT_RECIPE_REQUEST);
                        startActivityForResult(intent, EDIT_RECIPE_REQUEST);
                        break;
                    case 1:
                        //Delete
                        Toast.makeText(MainActivity.this, recipes.get(longClickedListItem).getTitle() +
                                " " + getString(R.string.msg_deleted), Toast.LENGTH_SHORT).show();
                        recipes.remove(longClickedListItem);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        dialog = builder.create();
    }
}
