package com.example.pethfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dto.Board;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.Holder> {
    private Context context;
    private List<Board> boards;

    public BoardAdapter(Context context, List<Board> boards) {
        this.context = context;
        this.boards = boards;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_board, parent, false);
        return new Holder(view);
    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(boards.get(position));
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView idTv;
        private TextView titleTv;
        private TextView userNameTv;
        private TextView textTv;
        private TextView dateTv;


        public Holder(View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.itemID);
            titleTv = itemView.findViewById(R.id.itemTitle);
            userNameTv = itemView.findViewById(R.id.itemUserName);
            dateTv = itemView.findViewById(R.id.itemDate);
            itemView.setClickable(true);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ViewActivity.class);
                    intent.putExtra("id", boards.get(position).getId().toString());
                    intent.putExtra("title", boards.get(position).getTitle());
                    intent.putExtra("userName", boards.get(position).getUserName());
                    intent.putExtra("text", boards.get(position).getText());
                    intent.putExtra("date", boards.get(position).getDate());
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Board board) {
            idTv.setText(board.getId().toString());
            titleTv.setText(board.getTitle());
            userNameTv.setText(board.getUserName());
            dateTv.setText(board.getDate());
        }


    }

}


