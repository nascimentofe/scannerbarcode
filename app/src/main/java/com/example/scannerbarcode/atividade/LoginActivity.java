package com.example.scannerbarcode.atividade;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.scannerbarcode.R;
import com.example.scannerbarcode.classes.HTTPService;
import com.example.scannerbarcode.classes.Usuario;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editSenha;
    CircularProgressButton progressButton;
    ArrayList<String> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText) findViewById(R.id.editLoginEmail);
        editSenha = (EditText) findViewById(R.id.editLoginSenha);
        progressButton = (CircularProgressButton) findViewById(R.id.btnLogin);
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editEmail.getText().toString().equals("")){
                    editEmail.setError("E-mail inválido!");
                }else if(editSenha.getText().toString().equals("")){
                    editSenha.setError("Senha inválida!");
                }else{
                    lista = new ArrayList<>();
                    lista.add(0, editEmail.getText().toString());
                    lista.add(1, editSenha.getText().toString());

                    HTTPService request = new HTTPService(getApplicationContext());
                    Usuario usuario = new Usuario();
                    request.sendLogin(usuario, lista, progressButton);
                }
            }
        });

    }
}
