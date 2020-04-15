package com.example.scannerbarcode.atividade;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.scannerbarcode.classes.HTTPService;
import com.example.scannerbarcode.classes.Portrait;
import com.example.scannerbarcode.R;
import com.example.scannerbarcode.classes.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.santalu.maskedittext.MaskEditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity extends AppCompatActivity {

    ImageButton imgScan;
    TextInputEditText editNF;
    Button btnNovoUsuario;
    CircularProgressButton btnEnviarDados;
    ProgressDialog progressDialog;
    MaskEditText editCodigoCompleto, editCNPJ;
    Usuario usuario;
    TextView txtLogadoComo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
//        Toast.makeText(this, usuario.getNomeUsuario(), Toast.LENGTH_LONG).show();

        views();

        acoes();

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

    public void criarAlert(String result){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(result);
        builder.setTitle("Resultado do Scan");
        builder.setCancelable(true);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scanow();
            }
        });
        builder.setNegativeButton("Não", null);

        AlertDialog alert = builder.create();
        alert.show();
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

    public void abrirProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }

    public void views(){
        txtLogadoComo = (TextView) findViewById(R.id.txtMainLogadoComo);
        String txt = txtLogadoComo.getText().toString() + usuario.getNomeUsuario();
        txtLogadoComo.setText(txt);

        editCNPJ = (MaskEditText) findViewById(R.id.editCNPJ);
        editNF = (TextInputEditText) findViewById(R.id.editNF);
        editCodigoCompleto = (MaskEditText) findViewById(R.id.editCodigoCompleto);
        btnEnviarDados = (CircularProgressButton) findViewById(R.id.btnEnviarDados);
        btnNovoUsuario = (Button) findViewById(R.id.btnMainNovousuario);
        btnNovoUsuario.requestFocus();
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

    }

}
