package com.example.timsapp.ui.home.Composite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timsapp.R;

import java.util.ArrayList;
import java.util.List;

public class ItemStaffAdapter extends RecyclerView.Adapter<ItemStaffAdapter.NoteVH> {
    private List<ItemStaffMaster> mNoteList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(ItemStaffAdapter.OnItemClickListener listener) {
        mListener = listener;
    }



    public ItemStaffAdapter(List<ItemStaffMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(ItemStaffMaster note) {
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

        TextView type,code,name,mission,Machine,no;

        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);
            no= itemView.findViewById(R.id.no);

            code = itemView.findViewById(R.id.code);
            name = itemView.findViewById(R.id.name);
            mission = itemView.findViewById(R.id.mission);
            type = itemView.findViewById(R.id.type);
            Machine= itemView.findViewById(R.id.Machine);
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


        public void bindData(ItemStaffMaster note) {
            no.setText(note.no);
            type.setText("");
            code.setText(note.userid);
            name.setText(note.uname);
            mission.setText(note.position_cd);
            Machine.setText(note.mc_no);
        }

        public void filterList(ArrayList<ItemStaffMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
