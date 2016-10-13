package com.os.onestep.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.os.onestep.R;
import com.os.onestep.activities.AddNotesActivity;
import com.os.onestep.beans.ViewNotesBean;

import java.util.ArrayList;

/**
 * Created by dhruvishah on 13/10/16.
 */
public class ViewNotesAdapter extends RecyclerView.Adapter<ViewNotesAdapter.RecyclerViewHolder> {
    private ArrayList<ViewNotesBean> notesBeanArrayList;
    private Context mContext;
    LayoutInflater inflater;

    public ViewNotesAdapter(ArrayList<ViewNotesBean> notesBeanArrayList, Context mContext) {
        this.notesBeanArrayList = notesBeanArrayList;
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.cardview_viewnotes, parent, false);

        RecyclerViewHolder viewHolder=new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
      final ViewNotesBean notesBean= notesBeanArrayList.get(position);
        holder.tv_date.setText(notesBean.getDate());
        holder.tv_noteheading.setText(notesBean.getNoteHeading());
        holder.tv_notedesc.setText(notesBean.getNoteDescription());

        holder.btn_editnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,AddNotesActivity.class);
                i.putExtra("notesBean", notesBeanArrayList.get(position));
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesBeanArrayList.size();
    }
    public void updateData(ArrayList<ViewNotesBean> directoryBeanArrayList) {
        notesBeanArrayList.clear();
        notesBeanArrayList.addAll(directoryBeanArrayList);
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date, tv_noteheading,tv_notedesc;
        Button  btn_editnote;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            tv_date = (TextView) itemView.findViewById(R.id.tv_setdate);
            tv_noteheading = (TextView) itemView.findViewById(R.id.tv_note_title);
            tv_notedesc = (TextView) itemView.findViewById(R.id.tv_note_desc);
            btn_editnote= (Button) itemView.findViewById(R.id.btn_edit);

        }
    }
}
