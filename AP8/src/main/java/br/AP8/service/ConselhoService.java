package br.AP8.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConselhoService {

    public static String buscarConselhoAleatorio() {
        try {
            URL url = new URL("https://api.adviceslip.com/advice");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                resposta.append(linha);
            }
            br.close();

            String json = resposta.toString();
            
            String chave = "\"advice\":\"";
            int inicio = json.indexOf(chave);
            if (inicio != -1) {
                inicio += chave.length();
                int fim = json.indexOf("\"", inicio);
                return json.substring(inicio, fim);
            }

            return "Conclua as suas tarefas do dia com foco!";

        } catch (Exception e) {
            return "Mantenha o foco e conclua as suas tarefas!";
        }
    }
}