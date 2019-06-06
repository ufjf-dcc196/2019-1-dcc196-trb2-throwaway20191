package com.example.tasklist;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NovaTarefaActivity  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_tarefa);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerNovoEstado);
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, com.example.tasklist.Tarefa.EstadoMap.values().toArray(new String[0]));
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nova_tarefa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_nova_tarefa:
                String titulo = ((TextView)findViewById(R.id.textNovoTitulo)).getText().toString();
                String descricao = ((TextView)findViewById(R.id.textNovoDescricao)).getText().toString();
                int dificuldade = (int)((RatingBar)findViewById(R.id.ratingNovaDificuldade)).getRating();
                String estadoText = ((Spinner)findViewById(R.id.spinnerNovoEstado)).getSelectedItem().toString();
                Tarefa.Estado estado = Tarefa.estadoFromString(estadoText);
                String dataLimite = ((TextView)findViewById(R.id.textNovoDataLimite)).getText().toString();
                String horaLimite = ((TextView)findViewById(R.id.textNovoHoraLimite)).getText().toString();

                TarefaDBHelper dbHelper = new TarefaDBHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Contract.TarefaColumns.COLUMN_TITULO, titulo);
                values.put(Contract.TarefaColumns.COLUMN_DESCRICAO, descricao);
                values.put(Contract.TarefaColumns.COLUMN_DIFICULDADE, dificuldade);
                values.put(Contract.TarefaColumns.COLUMN_ESTADO, estado.name());
                values.put(Contract.TarefaColumns.COLUMN_DEADLINE, dataLimite + "_" + horaLimite);
                values.put(Contract.TarefaColumns.COLUMN_UPDATED, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                long id = db.insert(Contract.TarefaColumns.TABLE_NAME, null, values);
                Toast.makeText(this,"Nova Tarefa adicionada com sucesso!", Toast.LENGTH_LONG).show();
                Log.i("TAREFA", "Nova tarefa com id: " + id);
                //Intent intent = new Intent();
                setResult(Activity.RESULT_OK);
                finish();

            default:
                break;
        }
        return true;
    }

}
