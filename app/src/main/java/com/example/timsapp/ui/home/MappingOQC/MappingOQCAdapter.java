package com.example.timsapp.ui.home.MappingOQC;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timsapp.R;
import com.example.timsapp.ui.home.Manufacturing.ManufacturingActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MappingOQCAdapter extends RecyclerView.Adapter<MappingOQCAdapter.NoteVH> {
    private List<MappingOQCMaster> mNoteList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onpass(int position);

        void onreturn(int position);

    }

    public void setOnItemClickListener(MappingOQCAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public MappingOQCAdapter(List<MappingOQCMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oqc_mapping,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(MappingOQCMaster note) {
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

        TextView mt_cd, num_gr_qty, qc_check, container, mt_no, no, pass, tvreturn;

        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);

            no = itemView.findViewById(R.id.no);
            mt_cd = itemView.findViewById(R.id.mt_cd);
            num_gr_qty = itemView.findViewById(R.id.num_gr_qty);
            qc_check = itemView.findViewById(R.id.qc_check);
            mt_no = itemView.findViewById(R.id.mt_no);
            container = itemView.findViewById(R.id.container);
            pass = itemView.findViewById(R.id.pass);
            tvreturn = itemView.findViewById(R.id.tvreturn);
            qc_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v, position);
                        }
                    }
                }
            });
            pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onpass(position);
                        }
                    }
                }
            });
            tvreturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onreturn(position);
                        }
                    }
                }
            });


        }


        public void bindData(MappingOQCMaster note) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            no.setText(getAdapterPosition() + 1 + "");
            mt_cd.setText(note.mt_cd);
            num_gr_qty.setText(formatter.format(note.gr_qty));
            qc_check.setText(ManufacturingActivity.qc_code);
            container.setText(note.bb_no);
            mt_no.setText(note.mt_no);
        }

        public void filterList(ArrayList<MappingOQCMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
