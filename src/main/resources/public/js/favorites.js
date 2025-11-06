


async function getFavoritesFromAPI(){


    const favorits = await fetch('http://localhost:8080/api/getFavorits', {headers: {"Bruker-id": "123"}});
    console.log(favorits)
}