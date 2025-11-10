package no.lambda.client.entur.GraphQL;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.lambda.client.entur.Exceptions.EnturGraphQLExceptions;
import no.lambda.client.entur.dto.TripResponseDto;
import okhttp3.*;
import com.fasterxml.jackson.databind.*; // Importerer ObjectMapper og andre Jackson-klasser

import java.io.IOException;
import java.util.Map;

// Hovedklient for å kommunisere med Entur sin GraphQL-API
public class EnturGraphQLClient {
    private final String clientName; // Klientnavn sendt i header for identifisering
    private final OkHttpClient httpClient; // HTTP-klient for å utføre nettverksforespørsler
    private final ObjectMapper mapper; // Jackson-mapper for JSON-serialisering/deserialisering
    private final String url; // Entur GraphQL API-endepunkt

    //produksjon konstruktør
    public EnturGraphQLClient(){
        // Kaller den fulle konstruktøren med standardverdier
        this("https://api.entur.io/journey-planner/v3/graphql",
                new OkHttpClient(),
                defaultMapper(),
                "LambdaTechAS-SkoleProsjekt_HIOF2025");
    }

    // Full konstruktør (brukes også for testing/tilpasset oppsett)
    public EnturGraphQLClient(String url, OkHttpClient httpClient, ObjectMapper mapper, String clientName) {
        this.url = url;
        // Bruker en OkHttpClient hvis ingen er spesifisert (for standard drift)
        this.httpClient = httpClient != null ? httpClient : new OkHttpClient();
        // Bruker standard konfigurasjon for ObjectMapper hvis ingen er spesifisert
        this.mapper = mapper != null ? mapper : defaultMapper();
        this.clientName = clientName != null ? clientName : "LambdaTechAS-SkoleProsjekt_HIOF2025";
    }

    // Utfører en GraphQL-spørring og returnerer resultatet mappet til TripResponseDto
    public TripResponseDto execute(String query, Map<String, Object> variables) throws EnturGraphQLExceptions {

        ObjectNode body = mapper.createObjectNode(); // Oppretter hoved JSON-objektet for forespørselen
        body.put("query", query); // Legger til GraphQL-spørringen
        // Legger til variabler, eller et tomt objekt hvis variabler er null
        body.set("variables", variables == null ? mapper.createObjectNode() : mapper.valueToTree(variables));

        // Bygger HTTP-forespørselen
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("ET-Client-Name", clientName) // Essensiell header for Entur-API
                // Setter forespørsel-bodyen (JSON-payloaden)
                .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                .build();

        // Utfører HTTP-kallet innenfor en try-with-resources blokk for automatisk lukking av Response
        try (Response response = httpClient.newCall(request).execute()){
            if (!response.isSuccessful()) {
                // Kaster unntak ved HTTP-feil (f.eks. 4xx eller 5xx statuskoder)
                throw new EnturGraphQLExceptions("Error in the Entur API request: " + response);
            }
            if (response.body() == null){
                throw new EnturGraphQLExceptions("Empty response body from Entur GraphQL");
            }
            // Mapper JSON-responsen fra bodyen til TripResponseDto-objektet
            return mapper.readValue(response.body().string(), TripResponseDto.class);
        } catch (IOException e) {
            // Håndterer nettverks- eller I/O-feil under kallet
            throw new EnturGraphQLExceptions(e);
        }
    }

    // Standard konfigurering for ObjectMapper for å håndtere datoer/tider
    private static ObjectMapper defaultMapper(){
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule()); // Registrerer modul for Java 8 Date/Time API-støtte
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Sikrer at datoer serialiseres som ISO-8601 strenger, ikke timestamps
        m.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE); // Deaktiverer tidsjustering under deserialisering
        return m;
    }
}