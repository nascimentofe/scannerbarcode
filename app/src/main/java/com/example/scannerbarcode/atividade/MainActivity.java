package com.example.scannerbarcode.atividade;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.scannerbarcode.classes.HTTPService;
import com.example.scannerbarcode.classes.LocationService;
import com.example.scannerbarcode.classes.Portrait;
import com.example.scannerbarcode.R;
import com.example.scannerbarcode.classes.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.santalu.maskedittext.MaskEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity extends AppCompatActivity {

    ImageButton imgScan;
    TextInputEditText editNF;
    Button btnNovoUsuario, btnDeslogar;
    CircularProgressButton btnEnviarDados;
    MaskEditText editCodigoCompleto, editCNPJ;
    Usuario usuario;
    TextView txtLogadoComo;
    public static final String LOGIN_PREFERENCE = "LOGIN_AUTOMATICO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        views();

        acoes();

        salvarLogin();

    }

    private void buscarLocalizacao() {
        if (Build.VERSION.SDK_INT >= 23){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }else{
                startService();
            }
        }else{
            startService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscarLocalizacao();
    }

    private void salvarLogin() {
        SharedPreferences.Editor editor = getSharedPreferences(LOGIN_PREFERENCE, MODE_PRIVATE).edit();
        editor.putInt("id", usuario.getIdUsuario());
        editor.putString("apelido", usuario.getApelidoUsuario());
        editor.putString("nome", usuario.getNomeUsuario());
        editor.putString("email", usuario.getEmailUsuario());
        editor.putString("telefone", usuario.getTelefoneUsuario());
        editor.putInt("nivel", usuario.getNivelUsuario());
        editor.putString("tipo", usuario.getTipoUsuario());
        editor.apply();
    }

    private void removerLogin(){
        SharedPreferences.Editor editor = getSharedPreferences(LOGIN_PREFERENCE, MODE_PRIVATE).edit();
        editor.clear().apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Nenhum código foi escaneado", Toast.LENGTH_LONG).show();
            }else{
                if (result.getContents().replace(" ", "").length() == 44){
                    preencherCampos(result.getContents());
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 44:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startService();
                }else{
                    Toast.makeText(this, "Permissões são necessarias!", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //tem certeza que deseja se deslogar?
        finishAffinity();
    }

    public void preencherCampos(String result){

        editCNPJ.setText(result.substring(6, 20));
        editNF.setText(result.substring(25, 34));
        editCodigoCompleto.setText(result);
        btnEnviarDados.requestFocus();
    }

    public void scanow(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Portrait.class);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Escaneie o CÓDIGO DE BARRAS ou o QR CODE da NF-E...");
        integrator.initiateScan();
    }

    public void enviarDados(String codigo, String cnpj, String nf){

        ArrayList<String> lista = new ArrayList<String>();
        lista.add(0, codigo);
        lista.add(1, cnpj);
        lista.add(2, nf);
        lista.add(3, String.valueOf(usuario.getIdUsuario()));

        HTTPService service = new HTTPService(getApplicationContext());
        service.sendData(btnEnviarDados, lista);

    }

    public void limparCampos() {
        editCodigoCompleto.setText("");
        editCNPJ.setText("");
        editNF.setText("");
    }

    public void views(){
        txtLogadoComo = (TextView) findViewById(R.id.txtMainLogadoComo);
        String txt = usuario.getNomeUsuario();
        txtLogadoComo.setText(txt);

        editCNPJ = (MaskEditText) findViewById(R.id.editCNPJ);
        editNF = (TextInputEditText) findViewById(R.id.editNF);
        editCodigoCompleto = (MaskEditText) findViewById(R.id.editCodigoCompleto);
        btnEnviarDados = (CircularProgressButton) findViewById(R.id.btnEnviarDados);
        btnNovoUsuario = (Button) findViewById(R.id.btnMainNovousuario);
        btnDeslogar = (Button) findViewById(R.id.btnMainDeslogar);
        btnDeslogar.requestFocus();
        imgScan = (ImageButton) findViewById(R.id.imgScan);
    }

    public void acoes(){
        imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanow();
            }
        });

        btnEnviarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codigo = editCodigoCompleto.getText().toString().replace(" ", "");
                String cnpj = editCNPJ.getText().toString().replace(".", "").replace("-", "").replace("/", "");
                String nf = editNF.getText().toString();

                if (codigo.length() == 44 && !cnpj.equals("") && !nf.equals("")){
                    //enviar
                    enviarDados(codigo, cnpj, nf);
                    limparCampos();
                }else{
                    if (codigo.length() == 44){
                        if (codigo.substring(20, 22).equals("55")){
                            preencherCampos(codigo);
                            //enviar
                            enviarDados(codigo,
                                    editCNPJ.getText().toString().replace(".", "").replace("-", "").replace("/", ""),
                                    editNF.getText().toString());
                            limparCampos();
                        }else{
                            Toast.makeText(getApplicationContext(), "Código de barras inválido!", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        int diff = 44 - codigo.length();
                        Toast.makeText(getApplicationContext(), "Faltam " + diff + " dígitos para completar o código!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnNovoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usuario.getNivelUsuario() == 1){
                    startActivity(new Intent(getApplicationContext(), NewUserActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), "Você não possui esta permissão!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                removerLogin();
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            }
        });

    }

    private void startService(){
        Intent i = new Intent(MainActivity.this, LocationService.class);
        i.putExtra("usuario", usuario);
        startService(i);
    }
}
