
// This is run when this page is loaded.
// All page logic should be here.
function allLogicForFavorites(){

    // Checks if we have a valid user id stored in local storage
    // We are sent back to login if not.
    checkForLogin();


    // Updates name text ontop of the screen
    document.getElementById("currentUser").textContent = getUserName();


    // Gets all the favorites and builds the page based on that data
    getFavorites();
}


// Gets all favorites from a user from the API
// Then sends the data to a func that builds it on the page.
async function getFavorites(){

    // Henter API data
    const response = await getFavoritesFromAPI()
    console.log("API data: ", response)

    // Gjør dataen om til noe lesbart
    const data = await response.json()
    console.log("API trip data: ", data)

    // Bygger trips til HTML
    buildFavorites(data)
}


// Fyller inn alle favorittene vi har fått
// til siden som viser favoritter.
function buildFavorites(data) {
  // Lager et tomt element til å holde HTML-koden vi lager her
  let favoritesHTML = "";

  // Hvis data-listen er tom
  if (!data.length) {
    document.getElementById("favs").innerHTML = '<p class="empty-fav" >Du har ingen lagrede favoritter enda.</p>';
    return;
  }


  data.forEach(([from, to, id]) => {
    favoritesHTML += `
      <div class="favorite-item" id="fav_${id}">
        <div class="favorite-info-wrapper">
            <p><strong>Fra:</strong>&nbsp;${from[0].name}</p>
            <p><strong>Til:</strong>&nbsp;${to[0].name}</p>
        </div>
        <div class="favorite-button-wrapper">
            <button class="fav-build-button button" onclick="makeRoute('${from[0].name}', '${to[0].name}')">Lag rute</button>
            <button class="fav-delete-button button" onclick="deleteFavorite(${id})">Slett</button>
        </div>
      </div>
    `;
  });

  // Setter inn alt i HTML-en
  document.getElementById("favs").innerHTML = favoritesHTML;
}


// A func that deletes a favorite item to a User from the database.
// It also removes it from the frontend
async function deleteFavorite(fav_id){

    // Gets id of current user
    const userId = getUserId()

    // Removes favorite from database
    const response = await removeFavoriteAPI(fav_id)

    // Removes the element from the frontend as well
    document.getElementById(`fav_${fav_id}`).remove();

    // Checks if empty, if so then adds a message about not having any favs
    const favs = document.getElementById('favs');

    if (favs.innerHTML.trim() === '') {
      favs.innerHTML = '<p class="empty-fav" >Du har ingen lagrede favoritter enda.</p>';
    } 

}

// A func that goes to the trip builder page.
// This adds the correct hash to the link before going there.
// The page then reads the hash.
function makeRoute(from, to){

  // For testing:
  console.log("Build Trips: ")
  console.log("   -", from)
  console.log("   -", to)

  // Skaffer nåværende tid i riktig format.
  let time = getCurrentTime();


  // Går til rute-siden. Vi legger til verdiene fra input-feltene som hash-verdier som leses i neste fane
  window.location.href = 'ruter.html' + makeHash(from, to, time);
}