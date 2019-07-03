package com.example.tasklist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class TagActivity extends AppCompatActivity {

    ArrayList<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        tags = new ArrayList<>();

        String tagString = getIntent().getStringExtra("tags");
        if (tagString != null){
            tags.addAll(Arrays.asList(tagString.split("|")));
        }

        final TextView tagEdit = findViewById(R.id.editTagName);
        tagEdit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    tags.add(tagEdit.getText().toString());
                    tagEdit.setText("");
                    return true;
                }
                return false;
            }
        });
        RecyclerView rv = findViewById(R.id.rvTag);
        rv.setAdapter(new TagAdapter());
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tag_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_nova_tarefa:
                StringBuilder sb = new StringBuilder("");
                for(String t : tags) {
                    sb.append(t).append("|");
                }
                sb.deleteCharAt(sb.length() - 1);
                Intent intent = new Intent();
                intent.putExtra("tags", sb.toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
        return true;
    }

    public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

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
            viewHolder.viewTag.setText(tags.get(i));
        }

        @Override
        public int getItemCount() {
            return tags.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView viewTag;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewTag = itemView.findViewById(R.id.textItemTag);
            }

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    tags.remove(getAdapterPosition());
                }
            }
        }
    }

}
