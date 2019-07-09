package com.example.tasklist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private final int RESULT_OK = 1;

    Tarefa tarefa;

    void restoreData(String id) {
        TarefaDBHelper helper = new TarefaDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {
                Contract.TarefaColumns._ID,
                Contract.TarefaColumns.COLUMN_TITULO,
                Contract.TarefaColumns.COLUMN_DIFICULDADE,
                Contract.TarefaColumns.COLUMN_ESTADO,
                Contract.TarefaColumns.COLUMN_DEADLINE,
                Contract.TarefaColumns.COLUMN_TAGS,
                Contract.TarefaColumns.COLUMN_DESCRICAO,
                Contract.TarefaColumns.COLUMN_UPDATED
        };
        String selection = Contract.TarefaColumns._ID + " = ?";
        String[] args = { id };
        Cursor c = db.query(Contract.TarefaColumns.TABLE_NAME, columns, selection, args, null, null, null, null);
        if (!c.moveToNext()) {
            Log.i("TAREFA", "Could not find row with id " + id);
            return;
        }
        tarefa = Contract.TarefaFromCursor(c);

        ((TextView)findViewById(R.id.textNovoTitulo)).setText(tarefa.titulo);
        ((TextView)findViewById(R.id.textNovoDescricao)).setText(tarefa.descricao);
        ((RatingBar)findViewById(R.id.ratingNovaDificuldade)).setRating(tarefa.dificuldade);
        ((Spinner)findViewById(R.id.spinnerNovoEstado)).setSelection(tarefa.estado.ordinal());
        String[] parts = tarefa.dataLimite.split(" ");
        ((TextView)findViewById(R.id.textNovoDataLimite)).setText(parts[0]);
        ((TextView)findViewById(R.id.textNovoHoraLimite)).setText(parts[1]);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_tarefa);

        Spinner spinner = findViewById(R.id.spinnerNovoEstado);
        ArrayAdapter<String> adapter =  new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, com.example.tasklist.Tarefa.EstadoMap.values().toArray(new String[0]));
        spinner.setAdapter(adapter);

        String id = getIntent().getStringExtra("id");
        if (id != null) {
            restoreData(id);
        }
        else {
            tarefa = new Tarefa();
        }
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
                values.put(Contract.TarefaColumns.COLUMN_TAGS, tarefa.tags);
                values.put(Contract.TarefaColumns.COLUMN_DEADLINE, dataLimite + " " + horaLimite);
                values.put(Contract.TarefaColumns.COLUMN_UPDATED, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

                String feedback;
                if (tarefa.id == null) {
                    long id = db.insert(Contract.TarefaColumns.TABLE_NAME, null, values);
                    feedback = "adicionada";
                }
                else {
                    String where = Contract.TarefaColumns._ID + " = ?";
                    String[] args = { tarefa.id };
                    db.update(Contract.TarefaColumns.TABLE_NAME, values, where, args);
                    feedback = "atualizada";
                }
                Toast.makeText(this,"Tarefa " + feedback + " com sucesso!", Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.action_edit_tags:
                Intent intent = new Intent(NovaTarefaActivity.this, TagActivity.class);
                intent.putExtra("tags", tarefa.tags);
                startActivityForResult(intent, RESULT_OK);
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Activity.RESULT_OK == resultCode){
            switch (requestCode){
                case RESULT_OK:
                    tarefa.tags = data.getStringExtra("tags");
                    break;
            }
        }
    }

}
