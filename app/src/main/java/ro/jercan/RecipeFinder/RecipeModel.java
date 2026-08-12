package ro.jercan.RecipeFinder;

//Clasa Model pentru o reteta
//Contine informatiile de baza extrase din JSON
public class RecipeModel {
    private final String id;
    private final String name;
    private final String imageUrl;
    private final String instructions;
    private final String ingredients;

    public RecipeModel(String id, String name, String imageUrl, String instructions, String ingredients){
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    //Getteri necesari pentru Adapter si activitatea de detalii

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getIngredients() { return ingredients;}
}
