package com.lille.ari_vaadin.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lille.ari_vaadin.models.ResponseAPI;

@Service
public class GreetService implements Serializable {

	public String greet(String name) {
		if (name == null || name.isEmpty()) {
			return "Hello anonymous user";
		} else {
			return "Hello " + name;
		}
	}

	public ResponseAPI get10Questions() {
		try {
			URL url = new URL("https://opentdb.com/api.php?amount=10");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			ObjectMapper mapper = new ObjectMapper();
			ResponseAPI response = new ResponseAPI();
			if (connection.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder build = new StringBuilder();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					build.append(inputLine);
				}
				in.close();
				response = mapper.readValue(build.toString(), ResponseAPI.class);
				return response;
			} else {

			}
			return null;
		} catch (IOException e) {
			return null;
		}

	}

}
