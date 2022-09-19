package com.kumard.whatsapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kumard.whatsapp.Modelss.Messages;
import com.kumard.whatsapp.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<Messages> messageModel;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<Messages> messageModel, Context context) {
        this.messageModel = messageModel;
        this.context = context;
    }

//    public ChatAdapter(ArrayList<Messages> messageModel, Context context, String recId) {
//        this.messageModel = messageModel;
//        this.context = context;
//        this.recId = recId;
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciver, parent, false);
            return new ReciverViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (messageModel.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {

            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages = messageModel.get(position);

        // Dialoge Box Lagane ke Liye (Yes or No)

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                new AlertDialog.Builder(context)
//                        .setTitle("Delete")
//                        .setMessage("Are you sure want to delete this message")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                FirebaseDatabase database=FirebaseDatabase.getInstance();
//                                String senderRoom =FirebaseAuth.getInstance().getUid() +recId;
//                                database.getReference().child("chats").child(senderRoom)
//                                        .child(messages.getMessageId())
//                                        .setValue(null);
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        dialogInterface.dismiss();
//                    }
//                }).show();
//
//                return false;
//            }
        // });
        //Dialoge Box End


        if (holder.getClass() == SenderViewHolder.class) {

            ((SenderViewHolder) holder).sendertxt.setText(messages.getMessage());
        } else {
            ((ReciverViewHolder) holder).recivertxt.setText(messages.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messageModel.size();
    }


    public class ReciverViewHolder extends RecyclerView.ViewHolder {

        TextView recivertxt, reciverTime;

        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);

            recivertxt = itemView.findViewById(R.id.reciverTxt);
            reciverTime = itemView.findViewById(R.id.reciverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView sendertxt, senderTim;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderTim = itemView.findViewById(R.id.sendTim);
            sendertxt = itemView.findViewById(R.id.sendertxt);
        }
    }
}
