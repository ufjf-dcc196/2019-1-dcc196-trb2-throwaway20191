package com.example.tasklist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NovaTarefaActivity  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_tarefa);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerNovoEstado);
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Tarefa.EstadoMap.values().toArray(new String[0]));
        spinner.setAdapter(adapter);
    }

}
