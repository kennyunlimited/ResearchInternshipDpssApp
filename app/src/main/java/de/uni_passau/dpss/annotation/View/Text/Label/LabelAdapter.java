package de.uni_passau.dpss.annotation.View.Text.Label;


/*
1. This class is an adapter to the Recycle view for all the Labels(text).
*/


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.uni_passau.dpss.annotation.Model.Text.Label;
import de.uni_passau.dpss.annotation.R;

public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.NoteHolder>{

    private List<Label> labels = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public LabelAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.label_item, parent, false);
        return new LabelAdapter.NoteHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull LabelAdapter.NoteHolder holder, int position) {
        String currentNoteText = labels.get(position).getLabel();
        holder.textViewLabel.setText(currentNoteText);
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    public Label getLabelAt(int position){
        return labels.get(position);
    }

    public void setlabels(List<Label> labels) {
        this.labels = labels;
        notifyDataSetChanged();
    }

    public void cancelModification(){
        notifyDataSetChanged();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewLabel;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewLabel = itemView.findViewById(R.id.text_view_label_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(labels.get(position));
                    }

                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Label label);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

}
