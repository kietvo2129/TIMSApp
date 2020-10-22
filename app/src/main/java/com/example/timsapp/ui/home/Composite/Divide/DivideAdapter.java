package com.example.timsapp.ui.home.Composite.Divide;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timsapp.R;
import com.example.timsapp.ui.home.Manufacturing.ManufacturingActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DivideAdapter extends RecyclerView.Adapter<DivideAdapter.NoteVH> {
    private List<DivideMaster> mNoteList;
    
    
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onDiv(int position);


    }

    public void setOnItemClickListener(DivideAdapter.OnItemClickListener listener) {
        mListener = listener;
    }



    public DivideAdapter(List<DivideMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_divide,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(DivideMaster note) {
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
        RelativeLayout div;

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
            div= itemView.findViewById(R.id.div);
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
            div.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDiv(position);
                        }
                    }
                }
            });


        }


        public void bindData(DivideMaster note) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            no.setText(note.no);
            mt_cd.setText(note.mt_cd);
            num_gr_qty.setText(formatter.format(note.sl_tru_ng + note.gr_qty));
            num_gr_qtyreal.setText(formatter.format(note.gr_qty));
            pqc.setText(ManufacturingActivity.qc_code);
            container.setText(note.bb_no);
            mtcnt.setText(formatter.format(note.cnt));
            if (note.gr_qty >0){
                div.setEnabled(true);
                div.setBackgroundColor(Color.parseColor("#594691"));
            }else {
                div.setEnabled(false);
                div.setBackgroundColor(Color.parseColor("#adafb1"));
            }

        }

        public void filterList(ArrayList<DivideMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
