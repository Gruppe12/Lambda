
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

// Skaffer nåværende tid i riktig format.
function getCurrentTime(){
   return new Date().toLocaleString('sv-SE').replace(' ', 'T');
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