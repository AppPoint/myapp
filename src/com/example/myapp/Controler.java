package com.example.myapp;

import android.app.ProgressDialog;
import android.content.Context;
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
public abstract class Controler extends AsyncTask<String, String, String> implements CallbackJSON {
    private ProgressDialog progressDialog;
    private Context context;

    public Controler(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Carregando...");
        if(progressDialog != null) progressDialog.show();
    }

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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(progressDialog != null) progressDialog.dismiss();
        if (s != null){
            receiveData(s);
        }
    }
}
