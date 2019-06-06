package com.example.tasklist;

import android.database.Cursor;
import android.provider.BaseColumns;

public class Contract {

    public static final String SQL_CREATE = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, " +
                    "%s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
            TarefaColumns.TABLE_NAME, TarefaColumns._ID,
            TarefaColumns.COLUMN_TITULO, TarefaColumns.COLUMN_DESCRICAO,
            TarefaColumns.COLUMN_DIFICULDADE, TarefaColumns.COLUMN_ESTADO,
            TarefaColumns.COLUMN_TAGS, TarefaColumns.COLUMN_DEADLINE,
            TarefaColumns.COLUMN_UPDATED
    );

    public static final String SQL_DROP = String.format(
            "DROP TABLE IF EXISTS %s", TarefaColumns.TABLE_NAME
    );

    public static final class TarefaColumns implements BaseColumns {
        public static final String TABLE_NAME = "tarefa";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_DIFICULDADE = "dificuldade";
        public static final String COLUMN_ESTADO = "estado";
        public static final String COLUMN_TAGS = "tags";
        public static final String COLUMN_DEADLINE = "deadline";
        public static final String COLUMN_UPDATED = "time_updated";
    }

    public static Tarefa TarefaFromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(TarefaColumns._ID));
        String titulo = cursor.getString(cursor.getColumnIndex(TarefaColumns.COLUMN_TITULO));
        String descricao = cursor.getString(cursor.getColumnIndex(TarefaColumns.COLUMN_DESCRICAO));
        String estadoStr = cursor.getString(cursor.getColumnIndex(TarefaColumns.COLUMN_ESTADO));
        int dificuldade = cursor.getInt(cursor.getColumnIndex(TarefaColumns.COLUMN_DIFICULDADE));
        String tags = cursor.getString(cursor.getColumnIndex(TarefaColumns.COLUMN_TAGS));
        String deadline = cursor.getString(cursor.getColumnIndex(TarefaColumns.COLUMN_DEADLINE));
        String lastUpdate = cursor.getString(cursor.getColumnIndex(TarefaColumns.COLUMN_UPDATED));

        Tarefa.Estado estado = Tarefa.Estado.valueOf(estadoStr);
        return new Tarefa(id, titulo, descricao, estado, dificuldade, tags, deadline, lastUpdate);
    }


}
