package com.example.pethfinder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dto.Board;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.Holder> {
    private Context context;
    private List<Board> boardList;

    public BoardAdapter(Context context, List<Board> boardList) {
        this.context = context;
        this.boardList = boardList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_board, parent, false);
        return new Holder(view);
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(boardList.get(position));
    }

/*
    public void delete(int position) {
        boardList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,boardList.size());

    }
*/

    public class Holder extends RecyclerView.ViewHolder {
        private TextView idTv;
        private TextView titleTv;
        private TextView userNameTv;
        private TextView dateTv;
        private int position;

        public Holder(View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.itemID);
            titleTv = itemView.findViewById(R.id.itemTitle);
            userNameTv = itemView.findViewById(R.id.itemUserName);
            dateTv = itemView.findViewById(R.id.itemDate);
            itemView.setClickable(true);
            itemView.setOnClickListener(view -> {
                position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ViewActivity.class);
                    intent.putExtra("id", boardList.get(position).getId());
                    intent.putExtra("title", boardList.get(position).getTitle());
                    intent.putExtra("userName", boardList.get(position).getUserName());
                    intent.putExtra("text", boardList.get(position).getText());
                    intent.putExtra("date", boardList.get(position).getDate());
                    intent.putExtra("position", position);
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



