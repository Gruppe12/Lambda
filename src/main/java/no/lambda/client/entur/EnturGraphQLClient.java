package no.lambda.client.entur;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import com.fasterxml.jackson.databind.*;
import java.util.Map;

public class EnturGraphQLClient {
    private static final String URL = "https://api.entur.io/journey-planner/v3/graphql";
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String clientName = "LambdaTech AS";

    public EnturGraphQLClient(){

    }

    public JsonNode execute(String query, Map<String, Object> variables) throws Exception{
        ObjectNode body = mapper.createObjectNode();
        body.put("query", query);
        body.set("variables", variables == null ? mapper.createObjectNode() : mapper.valueToTree(variables));

        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("ET-Client-Name", clientName)
                .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                .build();

        try (Response response = httpClient.newCall(request).execute()){
            if (!response.isSuccessful()) throw new RuntimeException("Error in the Entur API request: " + response);
            return mapper.readTree(response.body().string());
        }

    }
}
