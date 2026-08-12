package ro.jercan.RecipeFinder;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

// A doua activitate a aplicatiei
// Afiseaza detaliile despre reteta selectata
public class RecipeDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        //Activam butonul de Back in bara de sus pentru navigare
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Identificam elemente UI din layout-ul XML
        ImageView imageView = findViewById(R.id.detailImageView);
        TextView titleView = findViewById(R.id.detailTitle);
        TextView instructionsView = findViewById(R.id.detailInstructions);
        TextView ingredientsView = findViewById(R.id.detailIngredients);


        //Preluarea datelor trimise din MainActivity prin obiectul Intent
        String name = getIntent().getStringExtra("RECIPE_NAME");
        String imageUrl = getIntent().getStringExtra("RECIPE_IMAGE");
        String instructions = getIntent().getStringExtra("RECIPE_INSTRUCTIONS");
        String ingredients = getIntent().getStringExtra("RECIPE_INGREDIENTS");


        //Setarea titlului in Action Bar
        setTitle(name);

        //Popularea ecranului cu date
        titleView.setText(name);
        instructionsView.setText(instructions);
        ingredientsView.setText(ingredients);


        //Incarcarea imaginii de la URL folosind biblioteca Picasso
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .into(imageView);
        }
    }

    //Metoda apelata cand se apasa butonul Back
    //Inchidere activitate
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
