


async function getFavoritesFromAPI(){

    // Henter API data
    const response = await fetch('http://localhost:8080/api/getFavorites', {headers: {"Bruker-id": "1"}});

    // Gjør dataen om til noe lesbart
    const data = await response.json()

    // Printer data
    console.log("API trip data", data)
}