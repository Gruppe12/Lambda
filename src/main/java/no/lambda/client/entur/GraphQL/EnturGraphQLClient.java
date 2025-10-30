package no.lambda.client.entur.GraphQL;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.lambda.client.entur.Exceptions.EnturGraphQLExceptions;
import no.lambda.client.entur.dto.TripResponseDto;
import okhttp3.*;
import com.fasterxml.jackson.databind.*;


import java.io.IOException;
import java.util.Map;

public class EnturGraphQLClient {
    private final String clientName;
    private final OkHttpClient httpClient;
    private final ObjectMapper mapper;
    private final String url;

    //produksjon konstruktør
    public EnturGraphQLClient(){
        this("https://api.entur.io/journey-planner/v3/graphql",
                new OkHttpClient(),
                defaultMapper(),
                "LambdaTechAS-SkoleProsjekt_HIOF2025");
    }


    //konstruktør for testing
    public EnturGraphQLClient(String url, OkHttpClient httpClient, ObjectMapper mapper, String clientName) {
        this.url = url;
        this.httpClient = httpClient != null ? httpClient : new OkHttpClient();
        this.mapper = mapper != null ? mapper : defaultMapper();
        this.clientName = clientName != null ? clientName : "LambdaTechAS-SkoleProsjekt_HIOF2025";
    }

    public TripResponseDto execute(String query, Map<String, Object> variables) throws EnturGraphQLExceptions {

        ObjectNode body = mapper.createObjectNode();
        body.put("query", query);
        body.set("variables", variables == null ? mapper.createObjectNode() : mapper.valueToTree(variables));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("ET-Client-Name", clientName)
                .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                .build();

        try (Response response = httpClient.newCall(request).execute()){
            if (!response.isSuccessful()) {
             throw new EnturGraphQLExceptions("Error in the Entur API request: " + response);
            }
            if (response.body() == null){
                throw new EnturGraphQLExceptions("Empty response body from Entur GraphQL");
            }
            return mapper.readValue(response.body().string(), TripResponseDto.class);
        } catch (IOException e) {
            throw new EnturGraphQLExceptions(e);
        }
    }

    private static ObjectMapper defaultMapper(){
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        m.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        return m;
    }
}
