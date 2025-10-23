

// Testing for laging av ruter-funksjonen:
async function testBuildTrips() {


  const from = "Sarpsborg"
  const localDateTime = "2025-10-23T19:37:25.123"
  const response = await fetch(`/api/trips?from=${from}&to=&time=${localDateTime}%2B02:00&arriveBy=false`);
  const data = await response.json()
  console.log(data);

  const sampleTrips = [
    {
      duration: 35,
      legs: [
        {description: "Go to place"},
        {description: "Use the bus"},
        {description: "Walk the rest"}
      ]
    },
    {
      duration: 100,
      legs: [
        {description: "Start at station"},
        {description: "Take the train"},
        {description: "Arrive at destination"}
      ]
    }
  ];
  buildTrips(sampleTrips);
}


// Fyller inn alle rutene vi har fått
// til siden som viser rutene.
function buildTrips(trips) {

  // Lager et tomt element til å holde på HTML koden vi lager her
  let tripsHTML = "";

  // Går igjennom trips listen og bygger ruter til hvert element.
  trips.map((trip, index) => 
    tripsHTML += `
      <details>
        <summary>Rute ${index + 1} (${trip.duration} minutter)</summary>
        <ul>
          ${trip.legs.map(leg => `<li>${leg.description || leg.toString()}</li>`).join("")}
        </ul>
      </details>`
  );

  // Setter inn den nye HTML koden der den skal være
  document.getElementById("trips_wrapper").innerHTML = tripsHTML;

}