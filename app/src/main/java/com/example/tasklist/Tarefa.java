package com.example.tasklist;


import android.database.Cursor;

import java.sql.Timestamp;
import java.util.EnumMap;

public class Tarefa {

    public String id;
    public String titulo;
    public String descricao;
    public Estado estado;
    public int dificuldade;
    public String tags;
    public String dataLimite;
    public String dataAtualizado;

    public Tarefa(){}

    public Tarefa(String id, String titulo, String descricao, Estado estado, int dificuldade, String tags, String dataLimite, String dataAtualizado) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.estado = estado;
        this.dificuldade = dificuldade;
        this.tags = tags;
        this.dataLimite = dataLimite;
        this.dataAtualizado = dataAtualizado;
    }

    public static final EnumMap<Estado, String> EstadoMap = new EnumMap<Estado, String>(Estado.class){{
        put(Estado.AFazer, "A Fazer");
        put(Estado.EmExecucao, "Em Execução");
        put(Estado.Bloqueada, "Bloqueada");
        put(Estado.Concluida, "Concluida");
    }};

    public enum Estado {
        AFazer,
        EmExecucao,
        Bloqueada,
        Concluida
    }

    public static Estado estadoFromString(String estadoString) {
        for(Estado estado : EstadoMap.keySet()) {
            if (EstadoMap.get(estado).equals(estadoString)) {
                return estado;
            }
        }
        System.out.println("Something went wrong");
        return Estado.Bloqueada;
    }

    public String stars() {
        StringBuilder result = new StringBuilder();
        int i;
        for (i =0; i < dificuldade; i++) {
            result.append("★");
        }
        for (;i < 5; i++) {
            result.append("☆");
        }
        return result.toString();
    }
}
