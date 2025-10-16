


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

    // Går til rute-siden
    window.location.href = 'ruter.html';
  }
}