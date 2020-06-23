package com.viaexpressa.scannerbarcode.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.viaexpressa.scannerbarcode.R;
import com.viaexpressa.scannerbarcode.service.HTTPService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class NewUserActivity extends AppCompatActivity {

    EditText editApelido, editNome, editEmail, editTelefone, editSenha, editConfirmSenha;
    CircularProgressButton btnCadastrar;
    Toolbar toolbar;
    Spinner spnNivel, spnTipo;

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
                if (editApelido.getText().toString().equals("")){
                    editApelido.setError("Digite um apelido válido");
                }else if (editNome.getText().toString().equals("")){
                    editNome.setError("Digite um nome válido");
                }else if (editEmail.getText().toString().equals("")){
                    editEmail.setError("Digite um email válido");
                }else if (editTelefone.getText().toString().equals("") || editTelefone.getText().toString().length() < 15){
                    editTelefone.setError("Digite um telefone válido");
                }else if(editSenha.getText().toString().equals("")){
                    editSenha.setError("Digite uma senha válida");
                }else if (editConfirmSenha.getText().toString().equals("")){
                    editConfirmSenha.setError("Confirme a senha anterior");
                }else{
                    if (editSenha.getText().toString().equals(editConfirmSenha.getText().toString())){
                        ArrayList<String> lista = new ArrayList<>();
                        lista.add(0, editApelido.getText().toString());
                        lista.add(1, editNome.getText().toString());
                        lista.add(2, editEmail.getText().toString());
                        lista.add(3, formatarTelefone());
                        lista.add(4, editSenha.getText().toString());
                        lista.add(
                                5,
                                (spnNivel.getSelectedItem().toString().equals("Comum")) ? "0" : "1"
                        );
                        lista.add(6, spnTipo.getSelectedItem().toString());

                        HTTPService request = new HTTPService(getApplicationContext());
                        request.sendUser(lista, btnCadastrar, view);

                    }else{
                        Snackbar.make(view, "Senhas não conferem!", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private String formatarTelefone() {
        String telefone = editTelefone.getText().toString();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace(" ", "");
        telefone = telefone.replace("-", "");
        return telefone.trim();
    }

    private void views() {
        editApelido = (EditText) findViewById(R.id.editNovoUsuarioApelido);
        editNome = (EditText) findViewById(R.id.editUsuarioNome);
        editEmail = (EditText) findViewById(R.id.editUsuarioEmail);
        editTelefone = (EditText) findViewById(R.id.editNovoUsuarioTelefone);
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

        spnTipo = (Spinner) findViewById(R.id.spnNovoUsuarioTipo);
        String[] itensTipo = {"Motorista", "Ajudante", "Conferente", "Admin"};
        spnTipo.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                itensTipo
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
