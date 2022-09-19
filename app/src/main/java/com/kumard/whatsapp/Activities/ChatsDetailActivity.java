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
import com.kumard.whatsapp.databinding.ActivityChatsDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatsDetailActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;

    ActivityChatsDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String recivedId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");


        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.mProfile);
        binding.mName.setText(userName);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChatsDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        final ArrayList<Messages> messagesModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messagesModels, this);
        binding.chartDtRcy.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.chartDtRcy.setLayoutManager(linearLayoutManager);


        final String senderRoom = senderId + recivedId;
        final String receiverRoom = recivedId + senderId;

        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesModels.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                            Messages model = snapshot1.getValue(Messages.class);

                            //  model.setMessage(snapshot.getKey());
                            messagesModels.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = binding.mtext.getText().toString();
                final Messages messagesMod = new Messages(senderId, message);
                messagesMod.setTimestamp(new Date().getTime());
                binding.mtext.setText("");

                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(messagesMod).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .push()
                                .setValue(messagesMod).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

                    }
                });

            }
        });

    }
}