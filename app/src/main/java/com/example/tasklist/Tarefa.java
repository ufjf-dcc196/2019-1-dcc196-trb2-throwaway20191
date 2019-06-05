package com.example.tasklist;


import java.sql.Timestamp;
import java.util.EnumMap;

public class Tarefa {

    public String titulo;
    public String descricao;
    public Estado estado;
    public int dificuldade;
    public String tags;
    public Timestamp dataLimite;
    public Timestamp dataAtualizado;

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
