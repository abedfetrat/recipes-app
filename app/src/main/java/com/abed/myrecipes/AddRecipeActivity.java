package com.abed.myrecipes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE_REQUEST = 1;

    private TextInputLayout etTitleWrapper;
    private TextInputEditText etTitle;
    private TextInputEditText etDescription;
    private TextInputEditText etIngredients;
    private TextInputEditText etDirections;
    private TextInputEditText etNutritions;

    private ImageView imv;

    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getString(R.string.title_activity_add_recipe));

        etTitleWrapper = findViewById(R.id.et_title_wrapper);
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etIngredients = findViewById(R.id.et_ingredients);
        etDirections = findViewById(R.id.et_directions);
        etNutritions = findViewById(R.id.et_nutritions);

        imv = findViewById(R.id.imageView);

        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imv.getDrawable() == null) {
                    openImageChooser();
                } else {
                    imageUri = "";
                    imv.setImageDrawable(null);
                }
            }
        });

        Intent intent = getIntent();
        if (intent.getIntExtra("request_code", 0) == 2) {
            toolbar.setTitle(getString(R.string.title_edit_recipe));
            etTitle.setText(intent.getStringExtra(getString(R.string.key_recipe_title)));
            etDescription.setText(intent.getStringExtra(getString(R.string.key_recipe_description)));
            imageUri = intent.getStringExtra(getString(R.string.key_recipe_image_uri));
            setRecipeImage();
            etIngredients.setText(intent.getStringExtra(getString(R.string.key_recipe_ingredients)));
            etDirections.setText(intent.getStringExtra(getString(R.string.key_recipe_directions)));
            etNutritions.setText(intent.getStringExtra(getString(R.string.key_recipe_nutritions)));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_add_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                createRecipe();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    imageUri = uri.toString();
                    setRecipeImage();
                }
            }
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST);
        /*
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose picture"), 1);
        */
    }

    private void setRecipeImage() {
        Bitmap bmp = null;
        try {
            Uri uri = Uri.parse(imageUri);
            bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }

        if (bmp != null)
            imv.setImageBitmap(bmp);
    }

    private String capitalizeFirstLetter(String word) {
        return word.length() == 0 ? "" : word.substring(0,1).toUpperCase() +
                word.substring(1);
    }

    private boolean isFormValid() {
        return etTitle.getText().length() > 0;
    }

    private void createRecipe() {
        if (isFormValid()) {
            String title = capitalizeFirstLetter(etTitle.getText().toString());
            String description = capitalizeFirstLetter(etDescription.getText().toString());
            String ingredients = etIngredients.getText().toString();
            String directions = etDirections.getText().toString();
            String nutritions = etNutritions.getText().toString();

            Intent result = new Intent();
            result.putExtra(getString(R.string.key_recipe_title), title);
            result.putExtra(getString(R.string.key_recipe_image_uri), imageUri);
            result.putExtra(getString(R.string.key_recipe_description), description);
            result.putExtra(getString(R.string.key_recipe_ingredients), ingredients);
            result.putExtra(getString(R.string.key_recipe_directions), directions);
            result.putExtra(getString(R.string.key_recipe_nutritions), nutritions);
            setResult(RESULT_OK, result);
            finish();
        } else {
            etTitleWrapper.setError(getString(R.string.error_no_title));
            etTitleWrapper.requestFocus();
        }
    }
}