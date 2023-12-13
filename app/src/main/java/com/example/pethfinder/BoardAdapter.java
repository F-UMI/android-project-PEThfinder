package com.example.pethfinder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dto.BoardDto;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.Holder> {
    private Context context;
    private List<BoardDto> boardDtoList;

    public BoardAdapter(Context context, List<BoardDto> boardDtoList) {
        this.context = context;
        this.boardDtoList = boardDtoList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_board, parent, false);
        return new Holder(view);
    }

    @Override
    public int getItemCount() {
        return boardDtoList.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(boardDtoList.get(position));
    }

/*
    public void delete(int position) {
        boardList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,boardList.size());

    }
*/

    public class Holder extends RecyclerView.ViewHolder {
        private TextView titleTv;
        private TextView userNameTv;
        private TextView dateTv;
        private ImageView class_icon;
        private int position;

        public Holder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.itemTitle);
            userNameTv = itemView.findViewById(R.id.itemUserName);
            dateTv = itemView.findViewById(R.id.itemDate);
            class_icon = itemView.findViewById(R.id.class_icon);
            itemView.setClickable(true);
            itemView.setOnClickListener(view -> {
                position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, BoardViewActivity.class);
                    intent.putExtra("id", boardDtoList.get(position).getId());
                    intent.putExtra("title", boardDtoList.get(position).getTitle());
                    intent.putExtra("password", boardDtoList.get(position).getPassword());
                    intent.putExtra("userName", boardDtoList.get(position).getUserName());
                    intent.putExtra("text", boardDtoList.get(position).getText());
                    intent.putExtra("date", boardDtoList.get(position).getDate());
                    intent.putExtra("imagePath", boardDtoList.get(position).getImagePath());
                    intent.putExtra("classification", boardDtoList.get(position).getClassification());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });

        }

        public void bind(BoardDto boardDto) {
            if (boardDto.getClassification().equals("정보")) {
                class_icon.setImageResource(R.drawable.info_icon);
            } else {
                class_icon.setImageResource(R.drawable.star_icon);
            }
            titleTv.setText(boardDto.getTitle());
            userNameTv.setText(boardDto.getUserName());
            dateTv.setText(boardDto.getDate());
        }


    }

}



