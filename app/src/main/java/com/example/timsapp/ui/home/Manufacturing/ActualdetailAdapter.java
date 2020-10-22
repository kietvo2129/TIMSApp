package com.example.timsapp.ui.home.Manufacturing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.timsapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ActualdetailAdapter extends RecyclerView.Adapter<ActualdetailAdapter.NoteVH> {
    private List<ActualWOdetailMaster> mNoteList;



    public ActualdetailAdapter(List<ActualWOdetailMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actual_wo_detail,
                parent, false);
        NoteVH evh = new NoteVH(v);
        return evh;
    }




    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(ActualWOdetailMaster note) {
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

        public TextView bb_no,no;

        public NoteVH(View itemView) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);

            bb_no = itemView.findViewById(R.id.bb_no);

            no = itemView.findViewById(R.id.no);

        }


        public void bindData(ActualWOdetailMaster note) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            bb_no.setText(note.MaterialCode);
            no.setText(note.no);
        }

        public void filterList(ArrayList<ActualWOdetailMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
