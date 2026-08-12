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
