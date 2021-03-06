package com.viaexpressa.scannerbarcode.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.viaexpressa.scannerbarcode.R;
import com.viaexpressa.scannerbarcode.activity.LoginActivity;
import com.viaexpressa.scannerbarcode.activity.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.viaexpressa.scannerbarcode.classes.Usuario;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class HTTPService {

    private Context context;
    private static final String URL = "https://viaexpressaapp.com/appscanner/appviaexpressa/";

    public HTTPService(Context context){
        this.context = context;
    }

    public void sendData(final CircularProgressButton progressButton, ArrayList<String> lista){

        progressButton.startAnimation();

        final String url = URL +
                "SendData.php?codigo_barras=" + lista.get(0) +
                "&cnpj=" + lista.get(1) +
                "&nota=" + lista.get(2) +
                "&id=" + lista.get(3);

        Ion.with(this.context)
            .load(url)
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {

                    if (result != null){
                        progressButton.doneLoadingAnimation(
                                Color.parseColor("#333639"),
                                BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_done_white_48dp)
                        );

                        Toast.makeText(context, "Dados enviados com sucesso!", Toast.LENGTH_LONG).show();
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressButton.revertAnimation();
                        }
                    },  2000);

                }
            });
    }

    public void sendLogin(ArrayList<String> lista, final CircularProgressButton progressButton){

        final String url = URL + "/Login.php?apelido=" + lista.get(0) + "&pass=" + lista.get(1);

        progressButton.startAnimation();
        final Usuario usuario = new Usuario();

        Ion.with(this.context)
            .load(url)
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    // do stuff with the result or error
                    if(result != null){
                        usuario.setIdUsuario(result.get("ID").getAsInt());
                        usuario.setApelidoUsuario(result.get("APELIDO").getAsString());
                        usuario.setSenhaUsuario(result.get("SENHA").getAsString());
                        usuario.setNomeUsuario(result.get("NOME").getAsString());
                        usuario.setEmailUsuario(result.get("EMAIL").getAsString());
                        usuario.setTelefoneUsuario(result.get("TELEFONE").getAsString());
                        usuario.setNivelUsuario(result.get("NIVEL").getAsInt());
                        usuario.setTipoUsuario(result.get("TIPO").getAsString());

                        progressButton.doneLoadingAnimation(
                                Color.parseColor("#333639"),
                                BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_done_white_48dp)
                        );

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Intent i = new Intent(context.getApplicationContext(), MainActivity.class);
                                i.putExtra("usuario", usuario);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                        },  1000);

                    }else{
                        Toast.makeText(context, "Apelido ou senha incorretos!", Toast.LENGTH_LONG).show();
                        progressButton.revertAnimation();
                    }
                }
            });
    }

    public void sendUser(ArrayList<String> lista, final CircularProgressButton progressButton, final View view){

        final String url = URL + "UserRegister.php";

        progressButton.startAnimation();

        Ion.with(this.context)
                .load(url)
                .setBodyParameter("apelido", lista.get(0))
                .setBodyParameter("nome", lista.get(1))
                .setBodyParameter("email", lista.get(2))
                .setBodyParameter("telefone", lista.get(3))
                .setBodyParameter("senha", lista.get(4))
                .setBodyParameter("nivel", lista.get(5))
                .setBodyParameter("tipo", lista.get(6))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if(result != null){
                            //deu erro
                            String erro = "Erro não categorizado!";
                            Log.e("ERRO=>", result.toString());

                            progressButton.doneLoadingAnimation(
                                    Color.parseColor("#333639"),
                                    BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_delete)
                            );

                            if (result.get(1).toString().equals("1062")){
                                erro = "Já existe um usuario cadastrado com este apelido!";
                            }else if(result.get(1).toString().equals("1406")){
                                erro = "Você excedeu o limite de caracteres em algum campo!";
                            }

                            Snackbar.make(view, erro, Snackbar.LENGTH_LONG).show();

                        }else{
                            //deu certo
                            progressButton.doneLoadingAnimation(
                                    Color.parseColor("#333639"),
                                    BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_done_white_48dp)
                            );

                            Snackbar.make(view, "Usuario inserido com sucesso!", Snackbar.LENGTH_LONG).show();
                        }

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressButton.revertAnimation();
                            }
                        },  2000);
                    }
                });
    }

    public void sendLocation(ArrayList<String> lista){

        final String url = URL +
                "SendLocation.php?latitude=" + lista.get(0) +
                "&longitude=" + lista.get(1) +
                "&id=" + lista.get(2);

        Ion.with(this.context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null){
                            //deu erro no script php
                            Log.i("meulog", "Erro no script php");
                        }else{
                            Log.i("meulog", "localização enviada!");
                        }
                    }
                });

    }

    public void checkLogin(final Usuario usuario){

        final String url = URL + "/checkLogin.php?apelido=" + usuario.getApelidoUsuario() + "&pass=" + usuario.getSenhaUsuario();

        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null){
                            Intent i = new Intent(context.getApplicationContext(), MainActivity.class);
                            i.putExtra("usuario", usuario);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    }
                });
    }

    public ArrayList<Long> getTimeoutLocation(){

        final String url = URL + "/getTimeoutLocation.php";
        final ArrayList<Long> interval = new ArrayList<>();

        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        interval.add(0, result.get("interval").getAsLong());
                        interval.add(1, result.get("fatest").getAsLong());
                    }
                });
        return interval;
    }
}
