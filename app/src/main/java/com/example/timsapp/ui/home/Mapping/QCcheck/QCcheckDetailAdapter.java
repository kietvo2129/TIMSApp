package com.example.timsapp.ui.home.Mapping.QCcheck;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timsapp.R;

import java.util.ArrayList;
import java.util.List;


public class QCcheckDetailAdapter extends RecyclerView.Adapter<QCcheckDetailAdapter.NoteVH> {
    private List<QCCheckdetailMaster> mNoteList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onwarningClick(int position, RecyclerView recyclerView);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public QCcheckDetailAdapter(List<QCCheckdetailMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_qc_check,
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

        public TextView fo_no, mt_no, checkqty, okqty, decqty;
        RelativeLayout rl;
        RecyclerView rycviewchild;

        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);
            //title = (TextView) itemView.findViewById(R.id.title);
            fo_no = itemView.findViewById(R.id.fo_no);
            mt_no = itemView.findViewById(R.id.mt_no);
            checkqty = itemView.findViewById(R.id.checkqty);
            okqty = itemView.findViewById(R.id.okqty);
            decqty = itemView.findViewById(R.id.decqty);
            rl = itemView.findViewById(R.id.rl);
            rycviewchild = itemView.findViewById(R.id.rycviewchild);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onwarningClick(position,rycviewchild);
                            if (rycviewchild.getVisibility() == View.VISIBLE) {
                                rycviewchild.setVisibility(View.GONE);
                            } else {
                                rycviewchild.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });
        }


        public void bindData(QCCheckdetailMaster note) {
            rycviewchild.setVisibility(View.GONE);
            fo_no.setText(note.fq_no);
            mt_no.setText(note.fq_no);
            checkqty.setText(note.check_qty);
            okqty.setText(note.ok_qty);
            decqty.setText(note.defect_qty);
        }
    }

    public void filterList(ArrayList<QCCheckdetailMaster> filteredList) {
        mNoteList = filteredList;
        notifyDataSetChanged();
    }
}
