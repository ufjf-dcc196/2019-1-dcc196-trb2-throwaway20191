package com.example.tasklist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final int RESULT_NOVO = 1;

    private final TAdapter tAdapter = new TAdapter();
    private ArrayList<Tarefa> tarefas = new ArrayList<>();
    private TarefaDBHelper dbHelper;
    /*
    private Tarefa createTarefa() {
        Tarefa t = new Tarefa();
        t.titulo = "AAAA";
        t.descricao = "BBBBB";
        t.tags = "CCCCC";
        t.dificuldade = 3;
        t.estado = com.example.tasklist.Tarefa.Estado.AFazer;
        return t;
    }
    */

    private void queryTarefas() {
        dbHelper = new TarefaDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
        Cursor c = db.query(Contract.TarefaColumns.TABLE_NAME, columns, "", null, null, null, null);
        tarefas = new ArrayList<>();
        while (c.moveToNext()) {
            Tarefa tarefa = Contract.TarefaFromCursor(c);
            tarefas.add(tarefa);
        }
        tAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queryTarefas();

        RecyclerView rv = findViewById(R.id.rvTarefas);
        rv.setAdapter(tAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Button newButton = findViewById(R.id.buttonNovaTarefa);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NovaTarefaActivity.class);
                startActivityForResult(intent, RESULT_NOVO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Activity.RESULT_OK == resultCode){
            switch (requestCode){
                case RESULT_NOVO:
                    queryTarefas();
                    break;
            }
        }
    }

    public class TAdapter extends RecyclerView.Adapter<TAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater infl = LayoutInflater.from(context);
            View view = infl.inflate(R.layout.item_tasklist, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            view.findViewById(R.id.buttonRemover).setOnClickListener(holder);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.setData(tarefas.get(i));
        }

        @Override
        public int getItemCount() {
            return tarefas.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            Tarefa tarefa;
            TextView viewTitulo;
            TextView viewEstado;
            TextView viewTags;
            TextView viewDificuldade;
            TextView viewUpdated;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewTitulo = itemView.findViewById(R.id.textItemTitulo);
                viewEstado = itemView.findViewById(R.id.textItemEstado);
                viewTags = itemView.findViewById(R.id.textItemTags);
                viewDificuldade = itemView.findViewById(R.id.textItemDificuldade);
                viewUpdated = itemView.findViewById(R.id.textItemUpdated);
            }

            void setData(Tarefa tarefa){
                this.tarefa = tarefa;
                viewTitulo.setText(tarefa.titulo);
                viewDificuldade.setText(tarefa.stars());
                viewEstado.setText(com.example.tasklist.Tarefa.EstadoMap.get(tarefa.estado));
                viewTags.setText(tarefa.tags);
                viewUpdated.setText(tarefa.dataAtualizado);
            }

            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String selection = Contract.TarefaColumns._ID + " = ?";
                String[] args = { tarefa.id };
                db.delete(Contract.TarefaColumns.TABLE_NAME, selection, args);
                queryTarefas();
                Toast.makeText(getApplicationContext(), "Tarefa removida!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
