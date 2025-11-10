


async function getFavoritesFromAPI(){

    // Henter API data
    const response = await fetch('http://localhost:8080/api/getFavorites', {headers: {"Bruker-id": 2}});
    console.log("API trip data", response)

    // Gjør dataen om til noe lesbart
    const data = await response.json()
    console.log("API trip data", data)

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
    document.getElementById("favs").innerHTML = "<p>Du har ingen lagrede favoritter enda.</p>";
    return;
  }


  data.forEach(([from, to, id]) => {
    favoritesHTML += `
      <div class="favorite-item">
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


// Test-funksjon som sender testdata i riktig format
function testFavorites() {
  const testData = [
    ["Oslo", "Drammen", 1],
    ["Fredrikstad", "Moss", 2],
    ["Sarpsborg", "Halden", 3],
    ["Moss", "Råde", 4],
    ["Rygge", "Oslo", 5],
    ["Råde", "SKjeberg", 6]
  ];

  buildFavorites(testData);
}


async function deleteFavorite(fav_id){
    console.log("Delete Favorite: ", fav_id)
}


function makeRoute(from, to){
    console.log("Build Trips: ")
    console.log("   -", from)
    console.log("   -", to)
}