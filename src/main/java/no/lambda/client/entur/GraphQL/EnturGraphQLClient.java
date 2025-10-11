package no.lambda.client.entur.GraphQL;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.lambda.client.entur.dto.TripResponseDto;
import okhttp3.*;
import com.fasterxml.jackson.databind.*;
import java.util.Map;

public class EnturGraphQLClient {
    private static final String URL = "https://api.entur.io/journey-planner/v3/graphql";
    private final OkHttpClient httpClient = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();

    private final String clientName = "LambdaTechAS-SkoleProsjekt_HIOF2025";
    public EnturGraphQLClient(){
    }

    public TripResponseDto execute(String query, Map<String, Object> variables) throws Exception{
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        ObjectNode body = mapper.createObjectNode();
        body.put("query", query);
        body.set("variables", variables == null ? mapper.createObjectNode() : mapper.valueToTree(variables));

        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("ET-Client-Name", clientName)
                .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                .build();
                System.out.println(mapper.writeValueAsString(variables)); //sjekker variablene før kall, for debugging.

        try (Response response = httpClient.newCall(request).execute()){
            if (!response.isSuccessful()) throw new RuntimeException("Error in the Entur API request: " + response);

            return mapper.readValue(response.body().string(), TripResponseDto.class);
        }

    }
}
