package com.example.demo.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class APIController {

    @GetMapping("/stations")
    public ResponseEntity<Map<String, JSONArray>> getStations() throws IOException {
        URL url = new URL("https://apis.deutschebahn.com/db-api-marketplace/apis/station-data/v2/stations");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("DB-Api-Key", "06e45424f9a14a330cc7016de7e6af65");
        con.setRequestProperty("DB-Client-Id", "49b7f269fb66139f537c2f1324aa19e1");
        int status = con.getResponseCode();
        System.out.println(status);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(content.toString());
        JSONArray result = json.getJSONArray("result");
        List<String> bahnhofsnamen = new ArrayList<>();
        Map<String, JSONArray> infos = new HashMap<>();
        for (int i = 0; i < result.length(); i++) {
            JSONObject bahnhof = result.getJSONObject(i);
            String name = bahnhof.getString("name");
            bahnhofsnamen.add(name);
            infos.put(name, bahnhof.getJSONArray("DBinformation"));
        }

        for (String name : bahnhofsnamen) {
            System.out.println(infos);
        }



        return ResponseEntity.ok(infos);
    }
}
