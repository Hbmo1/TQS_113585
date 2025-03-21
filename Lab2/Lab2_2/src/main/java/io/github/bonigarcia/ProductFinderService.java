package io.github.bonigarcia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class ProductFinderService implements ISimpleHttpClient {
    public String apiProducts = "https://fakestoreapi.com/products/";
    private ISimpleHttpClient httpClient;

    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Optional<Product> findProductDetails(int id) throws IOException { // Add throws IOException
        String url = apiProducts + id;
        String response = httpClient.doHttpGet(url);

        if (response == null || response.isEmpty()) {
            return Optional.empty();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(response, Product.class); // Throws IOException naturally
        System.out.println(product);
        return Optional.of(product);
    }

    @Override
    public String doHttpGet(String url) {
        return url;
    }

}