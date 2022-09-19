package com.kumard.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kumard.whatsapp.Adapters.ChatAdapter;
import com.kumard.whatsapp.Modelss.Messages;
import com.kumard.whatsapp.databinding.ActivityGroopChatsBinding;

import java.util.ArrayList;
import java.util.Date;

public class Groop_Chats extends AppCompatActivity {

    ActivityGroopChatsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGroopChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Groop_Chats.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final ArrayList<Messages> messagesMode = new ArrayList<>();

        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.mName.setText("Friends Group");

        final ChatAdapter chatAdapter = new ChatAdapter(messagesMode, this);
        binding.chartDtRcy.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.chartDtRcy.setLayoutManager(linearLayoutManager);


        /// Firebase se data ko recycler view or layout me lekar aane ke liye

        database.getReference().child("Group")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        messagesMode.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Messages model = dataSnapshot.getValue(Messages.class);
                            messagesMode.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        /// End


        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String message = binding.mtext.getText().toString();
                final Messages messagesMod = new Messages(senderId, message);
                messagesMod.setTimestamp(new Date().getTime());
                binding.mtext.setText("");

                database.getReference().child("Group")
                        .push()
                        .setValue(messagesMod).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

            }
        });


    }
}