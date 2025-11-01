

// Fjerner hastags fra inputteksten før vi gjør noe mer med det
// I fremtiden kan vi legge til flere ulovlige tegn her
function makeSafeText(text){

  const forbiddenChars = ['#', '+'];

  // Går igjennom alle forbudte symboler
  forbiddenChars.forEach(char => {

    // Fjerner alle forekomster av hvert symbol
    text = text.split(char).join(''); 
  });

  console.log(text)

  return text; 
}


// Denne funksjonen lager en "hash" verdi som kan settes etter url.
// Denne leses i neste vindu og det blir slik vi sender data fra denne fanen til neste
function makeHash(from, to) {

  // Fjerner hastags fra selve input-teksten da dette ødelegger overføringen av data mellom nettsidene.
  from = makeSafeText(from);
  to = makeSafeText(to);

  // Denne funk gjør at navnene vi sender er trygge for bruk i URL
  // Altså, fjerner spesialtegn og mellomrom (Hello There --> hello%there)
  const hashValue = "#" + encodeURIComponent(from) + '+' + encodeURIComponent(to);
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


// Logikk for å sjekke om vi trykker på liste elementene i Dropdown menyen
function readValueDropdown(input_id,listElement){
  
  // Skriver over teksten i input-feltet basert på verdien
  document.getElementById(input_id).value = listElement.textContent;

}


