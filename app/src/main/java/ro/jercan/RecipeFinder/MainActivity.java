package ro.jercan.RecipeFinder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//Activitatea principala
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "RecipeFinder_Main";
    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView loadingText;
    private RecipeAdapter adapter;
    private List<RecipeModel> recipeList;
    private List<RecipeModel> allFetchedRecipies = new ArrayList<>(); //Stocam toate informatiile primite de la API
    int currentDisplayCount = 0; //Numarul de retetele afisate pe ecran
    static final int PAGE_SIZE = 4;
    Button btnLoadMore;


    //ExecutorService pentru operatiuni in background
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    //Handler pentru operatiuni pe UI thread
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializare componente UI
        editTextSearch = findViewById(R.id.editTextSearch);
        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);
        recyclerView = findViewById(R.id.recyclerViewRecipes);

        //Configurare RecyclerView - folosim LayoutManager si Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        adapter = new RecipeAdapter(recipeList, this);
        recyclerView.setAdapter(adapter);

        //Logica pentru butonul de cautare
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            String query = editTextSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                //Pornim taskul asincron
                fetchRecipes(query);
            } else {
                Toast.makeText(this, "Introduceți un ingredient", Toast.LENGTH_SHORT).show();
            }
        });

        //Implementare buton de iesire
        FloatingActionButton fabQuit = findViewById(R.id.fab_quit);
        fabQuit.setOnClickListener(v -> {
            Log.d(TAG, "Inchidere aplicatie solicitata de utilizator.");
            finishAffinity();
            System.exit(0);
        });

        //Buton refresh
        FloatingActionButton fabRefresh = findViewById(R.id.fab_refresh);
        fabRefresh.setOnClickListener(v -> {
            String query = editTextSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                fetchRecipes(query);
            }
        });

        //Buton Load More
        btnLoadMore = findViewById(R.id.btnLoadMore);
        btnLoadMore.setOnClickListener(v -> loadNextPage());
    }

    //Metoda care muta urmatoarele 4 obiecte din lista mare in lista afisata de adaptor
    private void loadNextPage(){
        int nextLimit = currentDisplayCount + PAGE_SIZE;

        for (int i=currentDisplayCount; i<nextLimit && i<allFetchedRecipies.size(); i++){
            recipeList.add(allFetchedRecipies.get(i));
            currentDisplayCount++;
        }

        adapter.notifyDataSetChanged();

        //Ascundem butonul daca nu mai avem de incarca
        if (currentDisplayCount >= allFetchedRecipies.size()){
            btnLoadMore.setVisibility(View.GONE);
        } else {
            btnLoadMore.setVisibility(View.VISIBLE);
        }
    }

    //ExecutorService pentru preluarea retetelor
    private void fetchRecipes (String query) {
        //UI Thread - incarcarea
        progressBar.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);
        recipeList.clear();

        //Rulam operatiune de retea pe un thread separat
        executorService.execute(()-> {
            String result = performNetworkCall(query);
            //Revenim la UI Thread pentru a actualiza interfata
            mainHandler.post(() -> {
                progressBar.setVisibility(View.GONE);
                loadingText.setVisibility(View.GONE);
                if (result != null) {
                    parseJson(result);
                } else {
                    Log.e(TAG, "Rezultatul apelului de retea este nul");
                    Toast.makeText(MainActivity.this, "Eroare la apelul de retea", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private String performNetworkCall(String query) {
        String apiURL = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + query;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(apiURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    //Parsarea raspunsului JSON
    private void parseJson(String json){
        try {
            JSONObject data = new JSONObject(json);
            //API-ul returneaza un array numit meals
            if (!data.isNull("meals")){
                JSONArray meals = data.getJSONArray("meals");

                allFetchedRecipies.clear(); //Stergem ce am luat anterior
                recipeList.clear(); // Stergem lista afisata in RecyclerView
                currentDisplayCount = 0;


                for (int i=0; i<meals.length(); i++){
                    JSONObject meal = meals.getJSONObject(i);
                    //Extragere informatii despre reteta
                    String id = meal.getString("idMeal");
                    String name = meal.getString("strMeal");
                    String thumb = meal.getString("strMealThumb");
                    String instructions = meal.getString("strInstructions");

                    // Extragere si formatare ingrediente in numar maxim de 20
                    StringBuilder ingredientBuilder = new StringBuilder();
                    for (int j=1; j<=20; j++) {
                        String ingredient = meal.getString("strIngredient" + j);
                        String measure = meal.getString("strMeasure" + j);

                        //Adaugam ingredientul doar daca nu este gol sau null
                        if (ingredient != null && !ingredient.trim().isEmpty() && !ingredient.equals("null")) {
                            ingredientBuilder.append(" - ").append(ingredient);
                            if (measure != null && !measure.trim().isEmpty() && !measure.equals("null")) {
                                ingredientBuilder.append(" - ").append(measure);
                            }
                           ingredientBuilder.append("\n");
                        }
                    }

                    String formatedIngredients = ingredientBuilder.toString();

                    //Adaugam obiectul in lista allFetchedRecipes
                    allFetchedRecipies.add(new RecipeModel(id, name, thumb, instructions, formatedIngredients));
                }
                //Afisam primele 4 retete
                displayNextBatch();
            } else{
                //Daca API-ul returneaza {"meals": null}
                recipeList.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Nu s-au gasit retete pentru acest ingredient", Toast.LENGTH_SHORT).show();
                btnLoadMore.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Eroare la parsarea JSON: " + e.getMessage());
            Toast.makeText(MainActivity.this, "Eroare la analiza raspunsului JSON", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayNextBatch(){
        int startIndex = currentDisplayCount;
        int endIndex = Math.min(currentDisplayCount + PAGE_SIZE, allFetchedRecipies.size());

        for (int i = startIndex; i < endIndex; i++) {
            recipeList.add(allFetchedRecipies.get(i));
        }

        currentDisplayCount = endIndex;
        //Notificam adaptorul ca s-au adaugat date
        adapter.notifyDataSetChanged();

        //Gestionam vizibilitatea butonului
        if (currentDisplayCount < allFetchedRecipies.size()) {
            btnLoadMore.setVisibility(View.VISIBLE); // Mai sunt retete de incarcat, aratam butonul
        } else {
            btnLoadMore.setVisibility(View.GONE); //Nu mai sunt retete de incarcat
        }

    }
}