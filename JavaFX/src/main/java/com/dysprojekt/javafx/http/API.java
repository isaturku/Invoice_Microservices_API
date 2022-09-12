package com.dysprojekt.javafx.http;

import javafx.application.Platform;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class API {
    private static final String apiURL = "http://localhost:8080/invoices/";

    // kjo metode dergon nje POST HTTP Request drejt serverit
    public static void generateInvoice(String id) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest postrequest = HttpRequest.newBuilder() // ketu krijohet POST HTTP Request per tu cuar drejt serverit
                .uri(URI.create(apiURL + id)) // ketu vihet url, ne kete rast "http://localhost:8080/invoices/{customerID}"
                .POST(HttpRequest.BodyPublishers.ofString(id)) // ketu vendoset metoda e HTTP request, ne kete rast "POST"
                .build();

      HttpClient.newBuilder()
                .build()
                .send(postrequest, HttpResponse.BodyHandlers.ofString()); // ketu POST HTTP Request drejt serverit, per krijimin e invoices
    }

}
