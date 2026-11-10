# Application Description
**RecipeFinder** is an Android mobile application that allows users to search for cooking recipes from an external database, namely the TheMealDB API.

Main features:
- Dynamic Search: The user enters an ingredient or a recipe name.
- List Display: Search results are displayed in a modern RecyclerView with CardView elements.
- Detailed View: When a recipe is selected, a secondary screen opens, displaying a high-resolution image and the complete cooking instructions.
- Resource Management: Asynchronous loading of images and data to ensure a smooth user experience.
- Local Pagination (Lazy Loading): I implemented a data batch-loading system to optimize memory usage and improve the response time of the graphical user interface.

The application retrieves information from https://www.themealdb.com/api/json/v1/1/search.php?s=.

# Request Structure
The query to the TheMealDB server is performed using an HTTP GET request.

The URL is dynamically constructed by appending the text entered by the user:

https://www.themealdb.com/api/json/v1/1/search.php?s= + query

I used the abstract HttpURLConnection class, imported through `import java.net.HttpURLConnection`, within the `performNetworkCall(query)` method. I then called this method using a background thread managed through ExecutorService.

To process the server response, specifically the returned JSON object, I used the `parseJson(String json)` method, which converted the JSON text into Java objects as follows:
- Validation: Check whether the "meals" key is not null.
- Iteration: Iterate through the JSONArray and extract `idMeal`, `strMeal`, `strMealThumb`, and `strInstructions`.
- Ingredient Concatenation: I used the StringBuilder class to extract and format the ingredients.
- Pagination: I stored the data in `allFetchedRecipes`, and used the `displayNextBatch()` method to display only the first 4 items.

# Screenshots and Description

**Screenshot 1 (Main Screen):**  
Displays the search bar, the "Refresh" button, and the exit button.

<img width="179" height="397" alt="image" src="https://github.com/user-attachments/assets/98a465b2-c83e-47d9-85ed-de735359f886" />

**Screenshot 2 (Search Results):**  
Displays a list of 4 recipes retrieved from the API in CardView format, along with a "Load More" button that adds 4 additional recipes at a time.

Once all recipes retrieved from the API have been displayed, the button is no longer visible.

Pressing the "Refresh" button resets the list to display only 4 recipes, and the "Load More" button becomes visible again.

<img width="254" height="565" alt="image" src="https://github.com/user-attachments/assets/a59cd682-c7b1-4e8f-b729-c7684f35a4a0" />
<img width="255" height="565" alt="image" src="https://github.com/user-attachments/assets/b93e3a83-3e2e-4a7e-a929-a088bf75adc8" />

**Screenshot 3 (Selected Recipe Details):**  
Displays the ingredients and cooking instructions on a separate screen. Pressing the "Back" button returns the user to the screen containing the previous search results.

<img width="286" height="636" alt="image" src="https://github.com/user-attachments/assets/94a1c163-de37-42c5-aeb8-6ca68ccc2673" />
<img width="283" height="629" alt="image" src="https://github.com/user-attachments/assets/ffb98c2c-fea1-4960-850e-1c3f9177821c" />

**Screenshot 4 (Application Icon):**  
Shows the phone screen displaying the application's custom icon.

<img width="183" height="406" alt="image" src="https://github.com/user-attachments/assets/91dae6c8-34ae-4903-a1c3-55485f3f2800" />


***

# Descrierea Aplicației
**RecipeFinder** este o aplicație mobilă Android care permite utilizatorilor să caute rețete culinare dintr-o bază de date externă, respectiv TheMealDB API. 

Funcționalități principale:
-	Căutare dinamică: Utilizatorul introduce un ingredient sau un nume de rețetă
-	Afișare listă: Rezultatele sunt afișate într-un RecyclerView modern cu elemente de tip CardView
-	Vizualizare Detaliată: La selectarea unei rețete, se deschide un ecran secundar cu imaginea la rezoluție mare și instrucțiunile complete
-	Management Resurse: Încărcarea asincronă a imaginilor și a datelor pentru o experiență fluidă
-	Paginare locală(Lazy Loading): Am implementat un sistem de încărcare fragmentată a datelor, pentru a optimiza consumul de memorie și pentru a îmbunătăți timpul de răspuns al interfeței grafice. 

Aplicația preia informații de la https://www.themealdb.com/api/json/v1/1/search.php?s=.

# Structura requestului
Interogarea serverului TheMealDB se face printr-un request de tip HTTP GET. 
URL-ul este construit dinamic preluând textul introdus de utilizator:
https://www.themealdb.com/api/json/v1/1/search.php?s=  + query
Am folosit clasa abstractă HttpURLConnection, importată prin import java.net.HttpURLConnection, in cadrul metodei performNetworkCall(query). Am apelat apoi această metodă folosind un fir de execuție secundar, prin ExecutorService.
 Pentru procesarea răspunsului, respectiv a obiectului JSON returnat de server, am folosit metoda parseJson(String json) care a transformat textul în obiecte Java, după cum urmează:
-	Validare: Verificăm dacă cheia ”meals” nu este null;
-	Iterare: Parcurgem JSONArray și extragem idMeal, strMeal, strMealThumb și strInstructions. 
-	Concatenare ingrediente: Pentru extragerea și formatarea ingredientelor am folosit clasa StringBuilder. 
-	Paginare: Am stocat datele în allFetchedRecipes, iar prin metoda displayNextBatch() am trimis către afișare doar primele 4 elemente.

# Capturi de ecran si Descriere
**Captură Ecran 1 (Ecran Principal):** 
Afișează bara de căutare, butonul de “Refresh” și butonul de ieșire.
<img width="179" height="397" alt="image" src="https://github.com/user-attachments/assets/98a465b2-c83e-47d9-85ed-de735359f886" />

**Captură Ecran 2 (Rezultate):** 
Afișează o listă de 4 rețete primite de la API în format CardView și un buton de “Încarcă mai multe” pentru a adăuga câte 4 rețete suplimentare. 
După epuizarea tuturor rețetelor primite de la API, butonul nu mai este vizibil. 
Dacă apăsăm butonul “Refresh” se revine la afișarea a doar 4 rețete, iar butonul “Încarcă mai multe” redevine vizibil.
<img width="254" height="565" alt="image" src="https://github.com/user-attachments/assets/a59cd682-c7b1-4e8f-b729-c7684f35a4a0" />
<img width="255" height="565" alt="image" src="https://github.com/user-attachments/assets/b93e3a83-3e2e-4a7e-a929-a088bf75adc8" />

**Captură Ecran 3 (Detalii rețetă selectată):** 
Afișează ingredientele și instrucțiunile de gătit într-un ecran separat. Dacă apăsăm pe butonul de “Back” revenim la ecranul care conține rezultatul căutării anterioare.
<img width="286" height="636" alt="image" src="https://github.com/user-attachments/assets/94a1c163-de37-42c5-aeb8-6ca68ccc2673" />
<img width="283" height="629" alt="image" src="https://github.com/user-attachments/assets/ffb98c2c-fea1-4960-850e-1c3f9177821c" />
        
**Captură Ecran 4  (Pictogramă):** 
Imagine cu ecranul telefonului unde se vede iconița proprie aplicației.
<img width="183" height="406" alt="image" src="https://github.com/user-attachments/assets/91dae6c8-34ae-4903-a1c3-55485f3f2800" />
