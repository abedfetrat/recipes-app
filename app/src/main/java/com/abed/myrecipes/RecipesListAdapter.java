package com.abed.myrecipes;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Abed on 02/03/2018.
 */

public class RecipesListAdapter extends BaseAdapter {

    private Context context;
    private List<Recipe> recipes;

    public RecipesListAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }
    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return recipes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("RecipeListAdapter", "getView " + position + " " + convertView);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_recipes, parent, false);
        }

        ImageView imv = convertView.findViewById(R.id.image);
        TextView tv = convertView.findViewById(R.id.title);

        imv.setImageDrawable(null);
        Bitmap bmp = getBitmap(recipes.get(position).getImageUri());
        if (bmp != null)
            imv.setImageBitmap(bmp);

        tv.setText(recipes.get(position).getTitle());

        return convertView;
    }

    private Bitmap getBitmap(String imageUri) {
        Bitmap bmp = null;
        try {
            Uri uri = Uri.parse(imageUri);
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return bmp;
    }
}