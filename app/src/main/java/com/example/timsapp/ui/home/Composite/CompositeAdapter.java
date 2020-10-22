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

public class CompositeAdapter extends RecyclerView.Adapter<CompositeAdapter.NoteVH> {
    private List<CompositeMaster> mNoteList;
    



    public CompositeAdapter(List<CompositeMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_composite,
                parent, false);
        NoteVH evh = new NoteVH(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(CompositeMaster note) {
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

        TextView code,start_dt,end_dt,type,no,staff_id,staff_tp_nm,use_yn;

        public NoteVH(View itemView) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);
            code = itemView.findViewById(R.id.code);
            start_dt = itemView.findViewById(R.id.start_dt);
            end_dt = itemView.findViewById(R.id.end_dt);
            type = itemView.findViewById(R.id.type);
            no= itemView.findViewById(R.id.no);
            staff_id= itemView.findViewById(R.id.staff_id);
            staff_tp_nm= itemView.findViewById(R.id.staff_tp_nm);
            use_yn= itemView.findViewById(R.id.use_yn);
        }


        public void bindData(CompositeMaster note) {
            code.setText(note.uname);
            start_dt.setText(note.start_dt);
            end_dt.setText(note.end_dt);
            type.setText(note.staff_tp);
            no.setText(note.no);
            staff_id.setText(note.staff_id);
            staff_tp_nm.setText(note.staff_tp_nm);
            use_yn.setText(note.use_yn);
        }

        public void filterList(ArrayList<CompositeMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
