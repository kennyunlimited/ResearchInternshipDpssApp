package de.uni_passau.dpss.annotation.View.Text.Word;

/*
1. This class is an adapter to the Recycle view for all the Words(text).
*/


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.uni_passau.dpss.annotation.Model.Text.Word;
import de.uni_passau.dpss.annotation.R;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.NoteHolder> {
    private List<Word> words = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Word currentWord = words.get(position);
        holder.textViewWord.setText(currentWord.getWord());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setWords(List<Word> words) {
        this.words = words;
        notifyDataSetChanged();
    }

    public Word getWordAt(int position){
        return words.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewWord;
        private TextView textViewLabel;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.text_view_word);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    listener.onItemClick(words.get(position));
                }
            });
        }
    }

    public interface onItemClickListener{
        void onItemClick(Word word);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

}