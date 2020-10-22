package com.example.timsapp.ui.home.Mapping.QCcheck;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timsapp.R;

import java.util.ArrayList;
import java.util.List;


public class QCcheckDetailChildAdapter extends RecyclerView.Adapter<QCcheckDetailChildAdapter.NoteVH> {
    private List<QCCheckDetailChildMaster> mNoteList;
    
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onwarningClick(int position, RecyclerView recyclerView);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public QCcheckDetailChildAdapter(List<QCCheckDetailChildMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_detail_qc_check,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }


    @Override
    public int getItemCount() {
        return mNoteList != null ? mNoteList.size() : 0;
    }

    class NoteVH extends RecyclerView.ViewHolder {

        public TextView subj, value, num_value;

        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);
            //title = (TextView) itemView.findViewById(R.id.title);
            subj = itemView.findViewById(R.id.subj);
            value = itemView.findViewById(R.id.value);
            num_value = itemView.findViewById(R.id.num_value);
        }


        public void bindData(QCCheckDetailChildMaster note) {
            subj.setText(note.check_subject);
            value.setText(note.check_value);
            num_value.setText(note.check_qty);
        }
    }

    public void filterList(ArrayList<QCCheckDetailChildMaster> filteredList) {
        mNoteList = filteredList;
        notifyDataSetChanged();
    }
}
