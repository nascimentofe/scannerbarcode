package com.viaexpressa.scannerbarcode.atividade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.viaexpressa.scannerbarcode.R;
import com.viaexpressa.scannerbarcode.classes.HTTPService;
import com.viaexpressa.scannerbarcode.classes.Usuario;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    EditText editApelido, editSenha;
    CircularProgressButton progressButton;
    ArrayList<String> lista;
    ImageButton imgBtnExibirSenha;
    public static final String LOGIN_PREFERENCE = "LOGIN_AUTOMATICO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        views();

        acoes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loginAutomatico();
    }

    private void loginAutomatico() {
        SharedPreferences preferences = getSharedPreferences(LOGIN_PREFERENCE, MODE_PRIVATE);
        if (preferences.getInt("id",0) > 0){
            Usuario usuario = new Usuario(
                    preferences.getInt("id",0),
                    preferences.getString("apelido", ""),
                    preferences.getString("senha", ""),
                    preferences.getString("nome", ""),
                    preferences.getString("email", ""),
                    preferences.getString("telefone", ""),
                    preferences.getInt("nivel", 0),
                    preferences.getString("tipo", "")
            );

            HTTPService request = new HTTPService(getApplicationContext());
            request.checkLogin(usuario, progressButton);
        }
    }

    private void acoes() {
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editApelido.getText().toString().equals("")){
                    editApelido.setError("Apelido inválido!");
                }else if(editSenha.getText().toString().equals("")){
                    editSenha.setError("Senha inválida!");
                }else{
                    lista = new ArrayList<>();
                    lista.add(0, editApelido.getText().toString());
                    lista.add(1, editSenha.getText().toString());

                    HTTPService request = new HTTPService(getApplicationContext());
                    request.sendLogin(lista, progressButton);
                }
            }
        });
        imgBtnExibirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editSenha.getInputType() == 129){ // password type
                    editSenha.setInputType(131073); // text type
                }else{
                    editSenha.setInputType(129);
                }
            }
        });
    }

    private void views() {
        editApelido = (EditText) findViewById(R.id.editLoginApelido);
        editSenha = (EditText) findViewById(R.id.editLoginSenha);
        progressButton = (CircularProgressButton) findViewById(R.id.btnLogin);
        imgBtnExibirSenha = (ImageButton) findViewById(R.id.imgBtnExibirSenha);
    }
}
