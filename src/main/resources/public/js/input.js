



// This is run when this page is loaded.
// All page logic should be here.
function allLogicForInput(){

    // Checks if we have a valid user id stored in local storage
    // We are sent back to login if not.
    checkForLogin();


    // Any async API funcs should be called from here.
}


// Check for valid user id.
// Sends you back to login page if not found.
// Replace is used to the user cant press the back button in the browser
function checkForLogin(){
    let user_id = localStorage.getItem('user_id')   

    if (user_id == null){
        window.location.replace("login.html");
    } else{

        console.log("User check: OK")
        console.log("User id: ", user_id)

    }
}


// Clears local storage, removing stored user-id
function logOut(){
   // Clears local storage
   localStorage.clear()

   // Checks if we have a stored user-id and if not sends us back to login page
   checkForLogin();
}


// Fjerner hastags fra inputteksten før vi gjør noe mer med det
// I fremtiden kan vi legge til flere ulovlige tegn her
function makeSafeText(text){

  const forbiddenChars = ['#', '+'];

  // Går igjennom alle forbudte symboler
  forbiddenChars.forEach(char => {

    // Fjerner alle forekomster av hvert symbol
    text = text.split(char).join(''); 
  });

  return text; 
}


// Denne funksjonen lager en "hash" verdi som kan settes etter url.
// Denne leses i neste vindu og det blir slik vi sender data fra denne fanen til neste
function makeHash(from, to, time) {

  // Fjerner hastags fra selve input-teksten da dette ødelegger overføringen av data mellom nettsidene.
  from = makeSafeText(from);
  to = makeSafeText(to);

  // Denne funk gjør at navnene vi sender er trygge for bruk i URL
  // Altså, fjerner spesialtegn og mellomrom (Hello There --> hello%there)
  const hashValue = "#" + encodeURIComponent(from) + '+' + encodeURIComponent(to) + '+' + encodeURIComponent(time);
  return hashValue
}


// Logikken for hva som skjer når du trykker på "LAG RUTE"-knappen. 
// Sjekker først om inputs er tomme eller ikke.
function checkInput() {

  // Hente inn informasjon om input-feltene og fjerne tomme mellomrom på start/slutt
  const inputFrom = document.getElementById("input_from").value.trim();
  const inputTo = document.getElementById("input_to").value.trim();
  const inputTime = getInputTime();

  // Sjekker om minst EN av input-feltene er tomme.
  if (inputFrom === "" || inputTo === "") {

    // Lager errormelding
    errorMessage("Feltene kan ikke være tomme!")

  } else {
    
    // Går til rute-siden. Vi legger til verdiene fra input-feltene som hash-verdier som leses i neste fane
    window.location.href = 'ruter.html' + makeHash(inputFrom, inputTo, inputTime);
  }
}


// Logikk for å sjekke om vi trykker på liste elementene i Dropdown menyen
function readValueDropdown(input_id,listElement){
  
  // Skriver over teksten i input-feltet basert på verdien
  document.getElementById(input_id).value = listElement.textContent;

}


// Skaffer data fra tids-input-feltet og gjør om til ønsket format
function getInputTime(){

    // Skaffer rådata fra inputfeltet
    const  inputValue = document.getElementById('travelTime').value;


    // Sjekker om klokke-data og/eller dato er tom
    if (!inputValue) {
        // Setter tiden som er akkurat nå i rikitg format (AI-generert)
        const currentDateTime = new Date().toLocaleString('sv-SE').replace(' ', 'T');
        return currentDateTime
    }

    // Lager et dato-variabel basert på inputen
    const date = new Date(inputValue);

    // Convert to ISO string and remove the trailing 'Z' (AI-generert)
    const travelDateTime = date.toLocaleString('sv-SE').replace(' ', 'T');

    return travelDateTime;
}


// Error message logikken
function errorMessage(errorText){

  // Henter Feilmeldings-elementet
  const msg = document.getElementById('error_message');
  msg.innerText = errorText;

  // Fader inn feilmeldingen og ut igjen etter 3 sekunder
  msg.style.opacity = 0;                         // Gjør den usynlig
  msg.style.animation = 'none';                  // Fjerner animationer som er koblet opp til den
  void msg.offsetWidth;                          // Resetter animasjons-greier
  msg.style.animation = 'fadeInOut 3s forwards'; // Legger til en ny animasjon
}