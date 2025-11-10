


async function getFavoritesFromAPI(){

    // Henter API data
    const response = await fetch('http://localhost:8080/api/getFavorites', {headers: {"Bruker-id": 1}});

    // Printer data
    console.log("API trip data", response)
}