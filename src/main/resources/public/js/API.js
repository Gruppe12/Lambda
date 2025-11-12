


// Removes favorite based on fav-id
async function removeFavoriteAPI(favId){

    const response = await fetch(`http://localhost:8080/api/removeFavorite?favoritId=${favId}`, { headers: { "Bruker-id": getUserId() } })

    return response
}


// Gets all favorites from a user from the API
// Then sends the data to a func that builds it on the page.
async function getFavoritesFromAPI(){
    
    const response = await fetch('http://localhost:8080/api/getFavorites', {headers: {"Bruker-id": getUserId()}});

    return response;
}


// Sjekker om denne ruten finnes i favorittlisten eller ikke
async function checkIfFavoriteAPI(fromLat, fromLon, toLat, toLon){

    // Legger kordinatene i riktig format
    const fromCoords = `${fromLat},${fromLon}`;
    const toCoords = `${toLat},${toLon}`;

    const response = await fetch(`http://localhost:8080/api/checkIfFavorite?fromCoords=${fromCoords}&toCoords=${toCoords}`, { headers: { "Bruker-id": getUserId() } })

    return response
}


// Legger til et nytt element i databasen
async function addToFavoriteAPI(fromLat, fromLon, toLat, toLon){

    // Legger kordinatene i riktig format
    const fromCoords = `${fromLat},${fromLon}`;
    const toCoords = `${toLat},${toLon}`;
    
    const response = await fetch(`/api/addToFavorite?fromCoords=${fromCoords}&toCoords=${toCoords}`, {headers: {"Bruker-id": getUserId()}});

    return response;

}


// Lager en rute via EnTur sin API
async function makeTripAPI(from, to, time) {

    const response = await fetch(`/api/trips?from=${from}&to=${to}&time=${time}%2B02:00&arriveBy=false`)

    return response;
}


// Får noen stedsnavn forslag baser på teksten vi sender inn
async function autoCompleteAPI(text) {
    
    const response = await fetch(`http://localhost:8080/api/autocomplete?typedIn=${encodeURIComponent(text)}`)

    return response;
}