

// Denne funksjonen lager en "hash" verdi som kan settes etter url.
// Denne leses i neste vindu og det blir slik vi sender data fra denne fanen til neste
function makeHash(from, to) {

  // Denne funk gjør at navnene vi sender er trygge for bruk i URL
  // Altså, fjerner spesialtegn og mellomrom (Hello There --> hello%there)
  const hashValue = "#" + encodeURIComponent(to) + '+' + encodeURIComponent(from);
  return hashValue
}



// Logikken for hva som skjer når du trykker på "LAG RUTE"-knappen. 
// Sjekker først om inputs er tomme eller ikke.
function checkInput() {

  // Hente inn informasjon om input-feltene og fjerne tomme mellomrom på start/slutt
  const inputFrom = document.getElementById("input_from").value.trim();
  const inputTo = document.getElementById("input_to").value.trim();

  // Henter Feilmeldings-elementet
  const msg = document.getElementById('error_message');

  // Sjekker om minst EN av input-feltene er tomme.
  if (inputFrom === "" || inputTo === "") {

    // Fader inn feilmeldingen og ut igjen etter 3 sekunder
    msg.style.opacity = 0;                         // Gjør den usynlig
    msg.style.animation = 'none';                  // Fjerner animationer som er koblet opp til den
    void msg.offsetWidth;                          // Resetter animasjons-greier
    msg.style.animation = 'fadeInOut 3s forwards'; // Legger til en ny animasjon
    console.log("Input cant be empty")
    
  } else {


    
    // Går til rute-siden. Vi legger til verdiene fra input-feltene som hash-verdier som leses i neste fane
    window.location.href = 'ruter.html' + makeHash(inputFrom, inputTo);
  }
}


