package com.example.myapp;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by patrick on 26/05/14.
 */
public class Controler extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {

        String linha = "";
        Boolean Erro = true;

        if (params.length > 0)


            try {

                HttpClient client = new DefaultHttpClient();
                HttpGet requisicao = new HttpGet(params[0]);
                HttpResponse resposta = client.execute(requisicao);
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        resposta.getEntity().getContent()));
                String s ="";

                while ((s = br.readLine()) != null) {
                    linha += s;
                }

                br.close();

                Erro = false;

            } catch (Exception e) {
                Erro = true;
            }

        return linha;
    }
}
