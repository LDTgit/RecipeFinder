package ro.jercan.RecipeFinder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

//Adapter-ul pentru RecyclerView-ul de retete
//Acesta umple fiecare rand din lista cu datele despre reteta
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final List<RecipeModel> recipeList;
    private final Context context;

    public RecipeAdapter(List<RecipeModel> recipeList, Context context){
        this.recipeList = recipeList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_row, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeModel recipe = recipeList.get(position);
        holder.textViewName.setText(recipe.getName());
        //Utilizam bilbioteca Glide pentru a incarca imaginea de la URL
//        Glide.with(context).load(recipe.getImageUrl()).placeholder(android.R.drawable.ic_menu_gallery).into(holder.imageViewRecipe);
        Picasso.get().load(recipe.getImageUrl()).placeholder(android.R.drawable.ic_menu_gallery).into(holder.imageViewRecipe);


        //Navigare catre a doua activitate cand dam click pe un rand
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            intent.putExtra("RECIPE_NAME", recipe.getName());
            intent.putExtra("RECIPE_IMAGE", recipe.getImageUrl());
            intent.putExtra("RECIPE_INSTRUCTIONS", recipe.getInstructions());
            intent.putExtra("RECIPE_INGREDIENTS", recipe.getIngredients());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    //Clasa ViewHolder care identifica elementele din XML
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewRecipe;
        public TextView textViewName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRecipe = itemView.findViewById(R.id.imageViewRecipe);
            textViewName = itemView.findViewById(R.id.textViewRecipeName);
        }
    }
}
