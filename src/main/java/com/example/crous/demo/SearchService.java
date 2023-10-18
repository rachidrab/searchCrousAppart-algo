package com.example.crous.demo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Service
class SearchService {
    private final String apiUrl = "https://trouverunlogement.lescrous.fr/api/fr/search/32";

    @Scheduled(fixedRate = 30000) // Envoi de la requête toutes les 30 secondes
    public void sendRequestAndProcessResponse() {
        RestTemplate restTemplate = new RestTemplate();
        SearchResponse response = restTemplate.postForObject(apiUrl, getRequestPayload(), SearchResponse.class);

        if (response != null && response.getResults() != null) {
            List<Item> items = response.getResults().getItems();
            if (!items.isEmpty()) {

                // Ouvrir le lien du premier élément
                Item firstItem = items.get(0);
                String link = "https://trouverunlogement.lescrous.fr/tools/32/accommodations/" + firstItem.getId();
                openLinkInBrowser("https://www.youtube.com/watch?v=yflVUteB4kk&ab_channel=BruitageEffetssonores");
            } else {
                System.out.println("rien n'est trouvé");
            }
        }
    }

    private String getRequestPayload() {
        // Construisez ici le corps de la requête POST au format JSON
        return "{\"idTool\": 32, \"need_aggregation\": true, \"page\": 1, \"pageSize\": 24, \"sector\": null, \"occupationModes\": [], \"location\": [{\"lon\": 5.2286902, \"lat\": 43.3910329}, {\"lon\": 5.5324758, \"lat\": 43.1696205}], \"residence\": null, \"precision\": 5, \"equipment\": [], \"price\": {\"min\": 0, \"max\": 10000000}}";
    }

    private void openLinkInBrowser(String link) {
        try {
            Runtime.getRuntime().exec("open " + link);
        } catch (IOException e) {
            // Gérer les exceptions
            e.printStackTrace();
        }
    }
}
