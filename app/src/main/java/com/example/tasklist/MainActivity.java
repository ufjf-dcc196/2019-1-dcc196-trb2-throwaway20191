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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    final int RESULT_NOVO = 1;

    private final TAdapter tAdapter = new TAdapter();
    private ArrayList<Tarefa> tarefas = new ArrayList<>();
    public TarefaDBHelper dbHelper;

    private List<String> queryTags() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                Contract.TarefaColumns.COLUMN_TAGS
        };
        Cursor c = db.query(Contract.TarefaColumns.TABLE_NAME, columns,  "", null, null, null, null);
        StringBuilder sb = new StringBuilder("|");
        while (c.moveToNext()) {
            sb.append(c.getString(0) + "|");
        }
        Log.i("Task", sb.toString());
        c.close();
        List<String> list = Arrays.asList(sb.toString().split("\\|"));
        Set<String> tags = new HashSet<>(list);
        return new ArrayList<>(tags);
    }


    private void queryTarefas(String filter) {
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
        Cursor c;
        if (filter.equals("")){
            c = db.query(Contract.TarefaColumns.TABLE_NAME, columns,  "", null, null, null, null);
        }
        else {
            String selection = "tags like '%"+ filter + "%'";
            c = db.query(Contract.TarefaColumns.TABLE_NAME, columns,  selection, null, null, null, null);
        }
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
        dbHelper = new TarefaDBHelper(this);

        RecyclerView rv = findViewById(R.id.rvTarefas);
        rv.setAdapter(tAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        final List<String> tags = queryTags();
        Spinner spinner = findViewById(R.id.spinnerTags);
        ArrayAdapter<String> adapter =  new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tags);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tag = tags.get(position);
                queryTarefas(tag);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_nova_tarefa:
                Intent intent = new Intent(MainActivity.this, NovaTarefaActivity.class);
                startActivityForResult(intent, RESULT_NOVO);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Activity.RESULT_OK == resultCode){
            switch (requestCode){
                case RESULT_NOVO:
                    queryTarefas("");
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
            view.findViewById(R.id.buttonItemEdit).setOnClickListener(holder);
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
            TextView viewDeadline;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewTitulo = itemView.findViewById(R.id.textItemTitulo);
                viewEstado = itemView.findViewById(R.id.textItemEstado);
                viewTags = itemView.findViewById(R.id.textItemTags);
                viewDificuldade = itemView.findViewById(R.id.textItemDificuldade);
                viewUpdated = itemView.findViewById(R.id.textItemUpdated);
                viewDeadline = itemView.findViewById(R.id.textItemDeadline);
            }

            void setData(Tarefa tarefa){
                this.tarefa = tarefa;
                viewTitulo.setText(tarefa.titulo);
                viewDificuldade.setText(tarefa.stars());
                viewEstado.setText(com.example.tasklist.Tarefa.EstadoMap.get(tarefa.estado));
                viewTags.setText(tarefa.tags);
                viewUpdated.setText(tarefa.dataAtualizado);
                viewDeadline.setText(tarefa.dataLimite);
            }

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                    return;
                }
                switch(v.getId()) {
                    case R.id.buttonRemover:
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String selection = Contract.TarefaColumns._ID + " = ?";
                        String[] args = { tarefa.id };
                        db.delete(Contract.TarefaColumns.TABLE_NAME, selection, args);
                        queryTarefas("");
                        Toast.makeText(getApplicationContext(), "Tarefa removida!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.buttonItemEdit:
                        Intent intent = new Intent(MainActivity.this, NovaTarefaActivity.class);
                        intent.putExtra("id", tarefa.id);
                        startActivityForResult(intent, RESULT_NOVO);
                        break;
                }

            }
        }
    }
}
