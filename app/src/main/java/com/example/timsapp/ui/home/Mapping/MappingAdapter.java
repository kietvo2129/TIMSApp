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

public class MappingAdapter extends RecyclerView.Adapter<MappingAdapter.NoteVH> {
    private List<MappingMaster> mNoteList;
    
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onQuantityChange(int position,TextView edittext);
        void onQCCheck(int position,TextView edittext);

    }

    public void setOnItemClickListener(MappingAdapter.OnItemClickListener listener) {
        mListener = listener;
    }



    public MappingAdapter(List<MappingMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mapping,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(MappingMaster note) {
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

        TextView mt_cd,num_gr_qty,num_gr_qtyreal,pqc,container,no,mtcnt;

        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);

            no= itemView.findViewById(R.id.no);
            mt_cd = itemView.findViewById(R.id.mt_cd);
            num_gr_qty = itemView.findViewById(R.id.num_gr_qty);
            num_gr_qtyreal = itemView.findViewById(R.id.num_gr_qtyreal);
            pqc = itemView.findViewById(R.id.pqc);
            container= itemView.findViewById(R.id.container);
            mtcnt= itemView.findViewById(R.id.mtcnt);
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
            num_gr_qtyreal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onQuantityChange(position,num_gr_qtyreal);
                        }
                    }
                }
            });
            pqc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onQCCheck(position,num_gr_qtyreal);
                        }
                    }
                }
            });
        }


        public void bindData(MappingMaster note) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            if (ManufacturingActivity.RollCode.equals("200")){
                num_gr_qtyreal.setEnabled(true);
                num_gr_qtyreal.setBackgroundColor(Color.parseColor("#009688"));
            }else {
                num_gr_qtyreal.setEnabled(false);
                num_gr_qtyreal.setBackgroundColor(Color.parseColor("#a9a9a9"));
            }
            no.setText(note.no);
            mt_cd.setText(note.mt_cd);
            num_gr_qty.setText(formatter.format(note.sl_tru_ng + note.gr_qty));
            num_gr_qtyreal.setText(formatter.format(note.gr_qty));
            //pqc.setText(ManufacturingActivity.qc_code);
            container.setText(note.bb_no);
            mtcnt.setText(formatter.format(note.cnt));
        }

        public void filterList(ArrayList<MappingMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
