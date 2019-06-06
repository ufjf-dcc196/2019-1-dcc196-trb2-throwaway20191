package com.example.tasklist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final TAdapter tAdapter = new TAdapter();
    private ArrayList<Tarefa> tarefas = new ArrayList<>();

    private Tarefa createTarefa() {
        Tarefa t = new Tarefa();
        t.titulo = "AAAA";
        t.descricao = "BBBBB";
        t.tags = "CCCCC";
        t.dificuldade = 3;
        t.estado = Tarefa.Estado.AFazer;
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tarefas.add(createTarefa());

        RecyclerView rv = findViewById(R.id.rvTarefas);
        rv.setAdapter(tAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Button newButton = findViewById(R.id.buttonNovaTarefa);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NovaTarefaActivity.class);
                startActivity(intent);
            }
        });

    }

    public class TAdapter extends RecyclerView.Adapter<TAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater infl = LayoutInflater.from(context);
            View view = infl.inflate(R.layout.item_tasklist, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.setData(tarefas.get(i));
        }

        @Override
        public int getItemCount() {
            return tarefas.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView viewTitulo;
            TextView viewEstado;
            TextView viewTags;
            TextView viewDificuldade;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                // Click em item da lista de planejamentos
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            //Intent intent = new Intent(MainActivity.this, DisciplinasCursadasActivity.class);
                            //intent.putExtra("position", position);
                            //startActivityForResult(intent, PLAN_DETALHES);
                        }
                    }
                });
                viewTitulo = itemView.findViewById(R.id.textItemTitulo);
                viewEstado = itemView.findViewById(R.id.textItemEstado);
                viewTags = itemView.findViewById(R.id.textItemTags);
                viewDificuldade = itemView.findViewById(R.id.textItemDificuldade);
            }

            void setData(Tarefa tarefa){
                viewTitulo.setText(tarefa.titulo);
                viewDificuldade.setText(tarefa.stars());
                viewEstado.setText(Tarefa.EstadoMap.get(tarefa.estado));
                viewTags.setText(tarefa.tags);
            }
        }
    }
    }
