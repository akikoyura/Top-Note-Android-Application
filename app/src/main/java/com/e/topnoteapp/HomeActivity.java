package com.e.topnoteapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e.topnoteapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private FloatingActionButton fab_btn;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    String post_key;
    String mTitle,mBudget,mNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child(uid);

        fab_btn = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Top Note Application");
        setSupportActionBar(toolbar);

        recyclerView  = findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

    }
    public void addData()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater().from(this);
        View myView = inflater.inflate(R.layout.add_data,null);
        final EditText title = myView.findViewById(R.id.title);
        final EditText budget = myView.findViewById(R.id.budget);
        final EditText notes = myView.findViewById(R.id.notes);
        Button save = myView.findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mTitle = title.getText().toString().trim();
                String mBudget = budget.getText().toString().trim();
                String mNotes = notes.getText().toString().trim();
                if(TextUtils.isEmpty(mTitle))
                {
                    title.setError("Required Field...");
                    return;
                }
                if(TextUtils.isEmpty(mBudget))
                {
                    budget.setError("Required Field...");
                    return;
                }
                if(TextUtils.isEmpty(mNotes))
                {
                    notes.setError("Required Field...");
                    return;
                }

                String id = mDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(mTitle,mBudget,mNotes,date,id);
                mDatabase.child(id).setValue(data);
                builder.show().dismiss();
            }
        });
        builder.setView(myView);
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data,myViewHolder> adapter = new FirebaseRecyclerAdapter<Data, myViewHolder>(Data.class,R.layout.showdata,myViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(myViewHolder viewHolder, final Data model, final int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setNote(model.getNotes());
                viewHolder.setBudget(model.getBudget());
                viewHolder.setDate(model.getDate());
                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key = getRef(position).getKey();
                        mTitle = model.getTitle();
                        mBudget = model.getBudget();
                        mNotes = model.getNotes();
                        editData();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }
        public void setTitle(String title)
        {
            TextView mTitle = myView.findViewById(R.id.sh_title);
            mTitle.setText(title);
        }
        public void setBudget(String budget)
        {
            TextView mBudget = myView.findViewById(R.id.sh_budget);
            mBudget.setText("$"+budget);
        }
        public void setNote(String note)
        {
            TextView mNote  = myView.findViewById(R.id.sh_note);
            mNote.setText(note);
        }
        public void setDate(String date)
        {
            TextView mDate = myView.findViewById(R.id.sh_date);
            mDate.setText(date);
        }


    }
    public void editData()
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.edit_and_delete_data,null);
        dialog.setView(myView);
        final AlertDialog myDialog =  dialog.create();
        final EditText title = myView.findViewById(R.id.title);
        final EditText budget = myView.findViewById(R.id.budget);
        final EditText notes = myView.findViewById(R.id.notes);

        title.setText(mTitle);
        title.setSelection(mTitle.length());

        budget.setText(mBudget);
        budget.setSelection(budget.length());

        notes.setText(mNotes);
        notes.setSelection(notes.length());

        Button btnUpdate = myView.findViewById(R.id.save_btn);
        Button btnDelete = myView.findViewById(R.id.delete_btn);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle = title.getText().toString().trim();
                mBudget = budget.getText().toString().trim();
                mNotes = notes.getText().toString().trim();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(mTitle,mBudget,mNotes,mDate,post_key);
                mDatabase.child(post_key).setValue(data);
                myDialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(post_key).removeValue();
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                AlertDialog.Builder builder =new AlertDialog.Builder(getApplicationContext());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
