
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




// This is run when this page is loaded.
// All page logic should be here.
function allLogicForRuter(){

    // Checks if we have a valid user id stored in local storage
    // We are sent back to login if not.
    checkForLogin();


    // Any async API funcs should be called from here.
    connectToAPI();
}



// Funksjon til å gi oss stor forbokstav i en string
// For JS har ikke dette innebygd..
function capitalize(str) {
  return str.charAt(0).toUpperCase() + str.slice(1)
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
async function connectToAPI() {


  // Skaffer Til og Fra punkter fra URL hash-data
  const startAndEndPoints = readHashFromURL()
  console.log("Hash data: ", startAndEndPoints)

  // Setter til og fra
  const from = startAndEndPoints["to"]
  const to = startAndEndPoints["from"]
  const time = startAndEndPoints["time"]

  // Sender denne dataen til vår API
  const response = await fetch(`/api/trips?from=${from}&to=${to}&time=${time}%2B02:00&arriveBy=false`)

  // Gjør dataen om til noe lesbart
  const data = await response.json()
  console.log("API trip data", data)

  // Lager overskriften på siden
  // "Fra - Til"
  makeHeader(startAndEndPoints)

  // Bygger trips til HTML
  buildTrips(data)

}


// Lager overskriften
function makeHeader(dict){

  // Setter H3 elementet på siden til å bli dette
  document.getElementById("title").innerHTML = capitalize(dict["from"]) + " - " + capitalize(dict["to"])


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
        <summary>Rute ${index + 1} (${(trip.duration / 60).toFixed(2)} minutter)</summary>
        <ul>`

    // Lager hvert "leg" i reisen
    // Vi deler distanse på 1000 for å få km (den er i meter orginalt)
    trip.legs.forEach(leg => 
      tripsHTML += `<li>${capitalize(transportTranslations[leg.mode])}   ${(leg.distance / 1000).toFixed(2)} km </li>`
    )

    // Lager slutten av resien
    tripsHTML += `</ul>
                  </details>`
  })



  // Setter inn den nye HTML koden der den skal være
  document.getElementById("trips_wrapper").innerHTML = tripsHTML

}