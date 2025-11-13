// Kjører koden kun når vinduet er loadet.
// Slik at alle HTML-elementer finnes før vi prøver å manipulere dem.
// Bruker vanligvis "window-onload", men i dette tilfellet funker DOM-eventlistner best
document.addEventListener("DOMContentLoaded", () => {

    console.log("Starting autocomplete...OK");

    // Skaffer input feltene som variabler slik at vi kan bruke de under
    const inputFrom = document.getElementById("input_from");
    const inputTo = document.getElementById("input_to");

    // Holder styr på delayen slik at vi ikke legger til flere counters i køen.

    let timeoutFrom;
    let timeoutTo;

    const millis = 200;

    // Lager en eventlistener som ser etter input-update fra de.
    // Logikken er den samme, så de deler funksjon, men vi må sende
    // inn ID til dropdown menyen som skal oppdateres
    inputFrom.addEventListener("input", () => {

        // Resetter timeout, så vi ikke kan spam skrive
        clearTimeout(timeoutFrom);
        timeoutFrom = setTimeout(() => updateOptions("input_from", "dropdown_from"), millis);
    });

    inputTo.addEventListener("input", () => {

        // Resetter timeout, så vi ikke kan spam skrive
        clearTimeout(timeoutTo);
        timeoutTo = setTimeout(() => updateOptions("input_to", "dropdown_to"), millis);
    });

    // Funksjon som oppdaterer dropdown options basert på input
    async function updateOptions(input_name, dropdown_name) {

        console.log("YES")

        // Henter ut riktig dropdown table slik at vi kan lett manipulere den
        const dropdown = document.getElementById(dropdown_name);

        // Henter verdien som er skrevet i input-feltet
        let inputValue = document.getElementById(input_name).value;

        // Hvis den er tom, fjern alle forslag
        if (inputValue == ""){
            dropdown.innerHTML = "";
            return;
        }

        //console.log(inputValue);

        // Sender et API kall med teksten fra input
        // Gjør også om til riktig/trygt format for URL sending
        const response = await autoCompleteAPI(inputValue);
        //console.log("API status: ", response)
        
        // Gjør dataen om til noe lesbart
        const data = await response.json()
        //console.log("API name suggestions: ", data)


        // Fills the correct dropdown with elements

        let nameHTML = ""
        const amountOfSuggestions = 5;

        data.slice(0, amountOfSuggestions).forEach(name => {
            nameHTML += `
                 <li onmousedown="readValueDropdown('${input_name}', this)">${name[0]}</li>
            `
        });

        dropdown.innerHTML = nameHTML
    }

});
