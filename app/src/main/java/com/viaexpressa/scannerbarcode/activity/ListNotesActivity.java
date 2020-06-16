package com.viaexpressa.scannerbarcode.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.viaexpressa.scannerbarcode.R;
import com.viaexpressa.scannerbarcode.services.HTTPService;
import com.viaexpressa.scannerbarcode.classes.Usuario;

public class ListNotesActivity extends AppCompatActivity {

    Usuario usuario;
    RecyclerView recyclerView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        toolbar = (Toolbar) findViewById(R.id.toolbarListNotes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        views();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarNotas(recyclerView);
    }

    private void carregarNotas(RecyclerView recyclerView) {
        HTTPService request = new HTTPService(getApplicationContext());
        request.listarNotas(recyclerView, usuario.getIdUsuario());
    }

    private void views() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerListNotes);
    }
}