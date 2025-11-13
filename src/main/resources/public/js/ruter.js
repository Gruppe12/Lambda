
// En engelsk-norsk ordbok til å oversetter Legs i reisen.
// De kommer på engelsk, vi vil ha på norsk.
// Dette er alle mulige metoder som kan komme tilbake fra EnTur sin API (2025)
const transportTranslations = {
  air: "fly",
  bicycle: "sykkel",
  bus: "buss",
  cableway: "taubane",
  water: "båt",
  funicular: "taubane",
  lift: "heis",
  rail: "tog",
  metro: "t-bane",
  taxi: "drosje",
  tram: "trikk",
  trolleybus: "trolleybuss",
  monorail: "monorail",
  coach: "langdistansebuss",
  foot: "gå",
  car: "bil",
  scooter: "sparkesykkel"
};

// Where coordinates for "FRA" and "TIL" is saved in case we want to 
// save this route as a favorite
let savedCoordinates = []

// This is run when this page is loaded.
// All page logic should be here.
function allLogicForRuter(){

    // Checks if we have a valid user id stored in local storage
    // We are sent back to login if not.
    checkForLogin();

    // Updates name text ontop of the screen
    document.getElementById("currentUser").textContent = getUserName();

    // Gets hashdata from url, sends it to API, and builds the page based on that data
    makeRouteAPI();
}



// Funksjon til å gi oss stor forbokstav i en string
// For JS har ikke dette innebygd..
function capitalize(str) {
  return str.charAt(0).toUpperCase() + str.slice(1)
}


// En funksjon for å escape HTML spesialtegn, for å hindre XSS
function escapeHTML(str) {
  if (!str) return '';
  return str
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

// En funksjon som leser "hash" verdiene fra URL-en og gir os en Fra og Til verdi
function readHashFromURL(){

    // Skaffer all tekst fra og med #-symbolet i URL
    const hash = window.location.hash

    // Fjerner #(som ligger først) og splitter ordene basert på +
    // Noe som vil gi oss to stedsnavn
    const values = hash.slice(1).split('+')

    // Lager en dictonary med til og fra verdi.
    // Vi dekoder også Hash, altså alle spesialtegn og mellomrom kommer tilbake
    return {
        from: decodeURIComponent(values[0]),
        to: decodeURIComponent(values[1]),
        time: decodeURIComponent(values[2])
    };
}



// Logikken for bygging av Trips på denne siden
async function makeRouteAPI() {


  // Skaffer Til og Fra punkter fra URL hash-data
  const startAndEndPoints = readHashFromURL()
  console.log("Hash data: ", startAndEndPoints)

  // Setter til, fra og tid fra dataen vi leste fra URL hash-data
  const from = startAndEndPoints["from"]
  const to = startAndEndPoints["to"]
  const time = startAndEndPoints["time"]

  // Sender denne dataen til vår API
  const response = await makeTripAPI(from, to, time);

  // Gjør dataen om til noe lesbart
  const data = await response.json()
  console.log("API trip data", data)

  // Lager overskriften på siden
  // "Fra - Til"
  makeHeader(startAndEndPoints)


  // Makes button visible
  document.getElementById("favButton").style.display = "block";

  // Lagrer koordinatene slik at vi kan bruke de hvis vi vil legge til som favoritt
  savedCoordinates = data[1].slice();
  console.log("Saved Coordinates: ", savedCoordinates)

  // Sjekker om den allerede er favorite eller ikke
  checkIfFavorite()

  // Bygger trips til HTML
  buildTrips(data[0])

}


// Sjekker om denne ruten finnes i favorittlisten eller ikke
// Hvis den finnes, så legger vi til en "Add to fav"-knapp, if not, 
// så legger vi til en "remove from fav"-knapp
async function checkIfFavorite(){

    // Skaffer dataen rundt koordinater
    const fromLat = savedCoordinates[0];
    const fromLon = savedCoordinates[1];
    const toLat = savedCoordinates[2];
    const toLon = savedCoordinates[3];

    // Sjekker om denne ruten er en favoritt fra før eller ikke
    const response = await checkIfFavoriteAPI(fromLat, fromLon, toLat, toLon)
    console.log("Fav checker:", response)

    // Gjør dataen om til noe lesbart
    const data = await response.json()
    console.log("Fav id: ", data)

    // Lager enten "legg til"-knappen, eller "fjern"-knappen
    changeFavButton(data)
}


// Lager overskriften
function makeHeader(dict){

  // Setter H3 elementet på siden til å bli dette
  capitalize(dict["from"]) + "<br>" + " &#8595; " + "<br>" + capitalize(dict["to"])
  document.getElementById("title").innerHTML = `
  
        <span>
            <strong>Fra:</strong>
            <p>${escapeHTML(capitalize(dict["from"]))}</p>
        </span>

        <span>
            <strong>Til:</strong>
            <p>${escapeHTML(capitalize(dict["to"]))}</p>
        </span>

  `


}

// Fyller inn alle rutene vi har fått
// til siden som viser rutene.
function buildTrips(trips) {

    // Lager et tomt element til å holde på HTML koden vi lager her
    let tripsHTML = ""

    // Hvis trips er tom
    if (!trips.length) {
        document.getElementById("trips_wrapper").innerHTML = "<p> Det finnes ingen rute dit akkurat nå </p>";
    return
    }


    // Går igjennom trips listen og bygger ruter til hvert element.
    trips.forEach((trip, index) => {

        // Lager toppen av listen
        // Vi deler tid på 60 for å få minutter (den er i sekunder orginalt)
        tripsHTML += `
          <details>
            <summary>
              <span class="arrow">&#9656;</span>
              <div>
                <span class="summary-title">Rute ${index + 1}</span>
                <span class="summary-small-text">${Math.ceil(trip.duration / 60)} minutter</span>
              </div>
            </summary>
            <ul>`;

        // Lager hvert "leg" i reisen
        // Vi deler distanse på 1000 for å få km (den er i meter orginalt)
        trip.legs.forEach(leg => {

            let temp_id = "";

            // Å gå har ingen line.id, så ville ført til en krasj. 
            // Dette stopper oss fra å sjekke en undefined verdi.
            if (leg.line && leg.line.id != null) {
                temp_id = `<p>${leg.line.publicCode}</p>`; // Eks: VYG:Line:RE20 
            } 

            // Så bygger vi innholdet i hver rute
            tripsHTML += `
            <li> 
               <span> <strong>${capitalize(transportTranslations[leg.mode])}</strong> ${temp_id} </span> 
               <span class="li-route-distance"> ${(leg.distance / 1000).toFixed(2)} km </span> 
            </li>`;
        });

        // Lager slutten av reisen
        tripsHTML += `</ul>
          </details>`;
    });



  // Setter inn den nye HTML koden der den skal være
  document.getElementById("trips_wrapper").innerHTML = tripsHTML

}

// Legger til rute som favoritt
async function addFavorite(){

  // Skaffer dataen rundt koordinater
  const fromLat = savedCoordinates[0];
  const fromLon = savedCoordinates[1];
  const toLat = savedCoordinates[2];
  const toLon = savedCoordinates[3];

  // Sender info til API
  const response = await addToFavoriteAPI(fromLat, fromLon, toLat, toLon)
  console.log("Adding to favorite response", response)

  // Gjør dataen om til noe lesbart (int fav_id)
  const data = await response.json()
  console.log("Fav id: ", data)

  // Gjør om på "legg til favoritt"-knappen
  // true=den finnes i favoritter, false=den finnes ikke i favoritter
  changeFavButton(data)

}


// Gjør om knappen til enten "legg til fav" eller "fjern fra fav", basert på true/false
async function changeFavButton(fav_id){

    // Skaffer add/remove fav knappen som et element vi kan gjøre om på
    const btn = document.getElementById("favButton");

    // Hvis den finnes i databasen fra før så legger vi til en "fjern" knapp
    if (fav_id != 0){
      btn.setAttribute("onclick", `removeFavorite(${fav_id})`);
      btn.classList.add("remove-btn");
      btn.textContent = "Fjern rute fra favoritter";
    }

    // Hvis den ikke finnes i databasen fra før legger vi til en "legg til" knapp
   else{
      btn.setAttribute("onclick", "addFavorite()");
      btn.classList.remove("remove-btn");
      btn.textContent = "Lagre rute som favoritt";
    }

}


async function removeFavorite(fav_id) {

    // Removes favorite from database
    const response = await removeFavoriteAPI(fav_id);
    console.log("Removing favorite respone: ", response)

    // Changes the button to "Legg til favoritter"
    changeFavButton(0);
}