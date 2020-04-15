package com.example.scannerbarcode.atividade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.scannerbarcode.R;
import com.example.scannerbarcode.classes.HTTPService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class NewUserActivity extends AppCompatActivity {

    EditText editNome, editEmail, editSenha, editConfirmSenha;
    CircularProgressButton btnCadastrar;
    Toolbar toolbar;
    Spinner spnNivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        toolbar = (Toolbar) findViewById(R.id.toolbarNovoUsuario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        views();

        acoes();

    }

    private void acoes() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editNome.getText().toString().equals("")){
                    editNome.setError("Digite um nome válido");
                }else if (editEmail.getText().toString().equals("")){
                    editEmail.setError("Digite um email válido");
                }else if(editSenha.getText().toString().equals("")){
                    editSenha.setError("Digite uma senha válida");
                }else if (editConfirmSenha.getText().toString().equals("")){
                    editConfirmSenha.setError("Confirme a senha anterior");
                }else{
                    if (editSenha.getText().toString().equals(editConfirmSenha.getText().toString())){
                        ArrayList<String> lista = new ArrayList<>();
                        lista.add(0, editNome.getText().toString());
                        lista.add(1, editEmail.getText().toString());
                        lista.add(2, editSenha.getText().toString());
                        lista.add(
                                3,
                                (spnNivel.getSelectedItem().toString().equals("Comum")) ? "0" : "1"
                        );

                        Log.e("ERRO=>", lista.get(3));

//                        HTTPService request = new HTTPService(getApplicationContext());
//                        request.sendUser(lista, btnCadastrar, view);
                    }else{
                        Snackbar.make(view, "Senhas não conferem!", Snackbar.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void views() {
        editNome = (EditText) findViewById(R.id.editUsuarioNome);
        editEmail = (EditText) findViewById(R.id.editUsuarioEmail);
        editSenha = (EditText) findViewById(R.id.editUsuarioSenha);
        editConfirmSenha = (EditText) findViewById(R.id.editUsuarioConfirmacaoSenha);
        btnCadastrar = (CircularProgressButton) findViewById(R.id.btnNovoUsuarioCadastrar);
        spnNivel = (Spinner) findViewById(R.id.spnNovoUsuarioNivel);
        String[] itensNivel = {"Comum", "Admin"};
        spnNivel.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                itensNivel
        ));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }
}
