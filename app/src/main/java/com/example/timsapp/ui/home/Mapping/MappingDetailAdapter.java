package com.example.timsapp.ui.home.Mapping;

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

public class MappingDetailAdapter extends RecyclerView.Adapter<MappingDetailAdapter.NoteVH> {
    private List<MappingDetailMaster> mNoteList;
    
    
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onR(int position, TextView edittext);
        void onF(int position, TextView edittext);
        void ondelete(int position, TextView edittext);
        void qcCheck(int position, TextView edittext);

    }

    public void setOnItemClickListener(MappingDetailAdapter.OnItemClickListener listener) {
        mListener = listener;
    }



    public MappingDetailAdapter(List<MappingDetailMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mapping_detail,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(MappingDetailMaster note) {
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

        TextView mt_cd,gr_qty,use_yn,Used,Remain,no,mt_no,bb_no;
        TextView r,f,delete,qc_check;

        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);

            no= itemView.findViewById(R.id.no);
            mt_cd = itemView.findViewById(R.id.mt_cd);
            gr_qty = itemView.findViewById(R.id.gr_qty);
            use_yn = itemView.findViewById(R.id.use_yn);
            Used = itemView.findViewById(R.id.Used);
            Remain= itemView.findViewById(R.id.Remain);
            mt_no= itemView.findViewById(R.id.mt_no);
            bb_no= itemView.findViewById(R.id.bb_no);
            r= itemView.findViewById(R.id.r);
            f= itemView.findViewById(R.id.f);
            delete= itemView.findViewById(R.id.delete);
            qc_check = itemView.findViewById(R.id.qc_check);
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
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onR(position,r);
                        }
                    }
                }
            });
            f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onF(position,f);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.ondelete(position,delete);
                        }
                    }
                }
            });
            qc_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.qcCheck(position,delete);
                        }
                    }
                }
            });
        }


        public void bindData(MappingDetailMaster note) {

            if (!ManufacturingActivity.RollCode.equals("100")){
                r.setVisibility(View.GONE);
            }else {
                r.setVisibility(View.VISIBLE);
            }


            DecimalFormat formatter = new DecimalFormat("#,###,###");
            no.setText(note.no);
            mt_cd.setText(note.mt_cd);
            use_yn.setText(note.use_yn);
            gr_qty.setText(formatter.format(note.gr_qty));
            Used.setText(formatter.format(note.Used));
            if (note.Remain == null){
                Remain.setText("0");
            }else {
                Remain.setText(formatter.format(Integer.parseInt(note.Remain)));
            }
            mt_no.setText(note.mt_no);
            bb_no.setText(note.bb_no);
            qc_check.setText(ManufacturingActivity.qc_code);
            if (note.getUse_yn().equals("Y")){
                delete.setEnabled(true);
                delete.setBackgroundColor(Color.parseColor("#ff1b1b"));
                r.setEnabled(true);
                r.setBackgroundColor(Color.parseColor("#009688"));
            }else {
                delete.setEnabled(false);
                delete.setBackgroundColor(Color.parseColor("#A39F9F"));
                r.setEnabled(false);
                r.setBackgroundColor(Color.parseColor("#C6BABA"));
            }
        }

        public void filterList(ArrayList<MappingDetailMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
