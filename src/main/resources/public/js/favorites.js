


async function getFavoritesFromAPI(){

    // Henter API data
    const response = await fetch('http://localhost:8080/api/getFavorites', {headers: {"Bruker-id": 1}});
    console.log("API trip data", response)

    // Gjør dataen om til noe lesbart
    const data = await response.json()
    console.log("API trip data", data)

}