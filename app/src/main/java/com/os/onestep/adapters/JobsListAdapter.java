package com.os.onestep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.os.onestep.R;
import com.os.onestep.beans.EstJobBean;

import java.util.ArrayList;

public class JobsListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<EstJobBean> mJobsArrayList;

    public JobsListAdapter(Context context,ArrayList<EstJobBean> jobsArrayList) {
        this.mContext = context;
        this.mJobsArrayList = jobsArrayList;
    }

    @Override
    public int getCount() {
        return mJobsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mJobsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            view = inflater.inflate(R.layout.layout_listviewitem, null);
            EstJobBean estJobBean = mJobsArrayList.get(position);
            TextView quantityText = (TextView) view.findViewById(R.id.quantityText);
            TextView coatsText = (TextView) view.findViewById(R.id.coatsText);
            TextView finishText = (TextView) view.findViewById(R.id.finishText);
            TextView ratesText = (TextView) view.findViewById(R.id.ratesText);
            TextView galsText = (TextView) view.findViewById(R.id.galsText);
            TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            TextView jobHeaderText = (TextView) view.findViewById(R.id.jobHeaderText);
            quantityText.setText(estJobBean.getQuantity());
            coatsText.setText(estJobBean.getCoats());
            finishText.setText(estJobBean.getFinish());
            ratesText.setText(estJobBean.getRates());
            galsText.setText(estJobBean.getGals());
            timeTextView.setText(estJobBean.getTime());
            jobHeaderText.setText(estJobBean.getJob());
        } else {
            view = inflater.inflate(R.layout.layout_listviewitem, null);
            EstJobBean estJobBean = mJobsArrayList.get(position);
            TextView quantityText = (TextView) view.findViewById(R.id.quantityText);
            TextView coatsText = (TextView) view.findViewById(R.id.coatsText);
            TextView finishText = (TextView) view.findViewById(R.id.finishText);
            TextView ratesText = (TextView) view.findViewById(R.id.ratesText);
            TextView galsText = (TextView) view.findViewById(R.id.galsText);
            TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            TextView jobHeaderText = (TextView) view.findViewById(R.id.jobHeaderText);
            quantityText.setText(estJobBean.getQuantity());
            coatsText.setText(estJobBean.getCoats());
            finishText.setText(estJobBean.getFinish());
            ratesText.setText(estJobBean.getRates());
            galsText.setText(estJobBean.getGals());
            timeTextView.setText(estJobBean.getTime());
            jobHeaderText.setText(estJobBean.getJob());
        }
        return view;
    }
}
