package com.example.timsapp.ui.home.ActualWO;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timsapp.R;
import com.example.timsapp.ui.home.Composite.ItemCompositeAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ActualWOHomeAdapter extends RecyclerView.Adapter<ActualWOHomeAdapter.NoteVH> {
    private List<ActualWOHomeMaster> mNoteList;

    private ItemCompositeAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(ItemCompositeAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public ActualWOHomeAdapter(List<ActualWOHomeMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actual_wo,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(ActualWOHomeMaster note) {
        if (!mNoteList.contains(note)) {
            mNoteList.add(note);
            notifyItemInserted(mNoteList.size());
        }
    }
    @Override
    public int getItemCount() {
        return mNoteList != null ? mNoteList.size() : 0;
    }
    class NoteVH extends RecyclerView.ViewHolder {

        public TextView target, atno, product;

        public NoteVH(View itemView, final ItemCompositeAdapter.OnItemClickListener listener) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);
            target = itemView.findViewById(R.id.target);
            atno = itemView.findViewById(R.id.atno);
            product = itemView.findViewById(R.id.product);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v,position);
                        }
                    }
                }
            });

        }


        public void bindData(ActualWOHomeMaster note) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            target.setText(formatter.format(note.target));
            atno.setText(note.at_no);
            product.setText(note.product);
        }

        public void filterList(ArrayList<ActualWOHomeMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
