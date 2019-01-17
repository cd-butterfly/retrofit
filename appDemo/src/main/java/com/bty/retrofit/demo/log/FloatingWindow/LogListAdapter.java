package com.bty.retrofit.demo.log.FloatingWindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bty.retrofit.demo.R;

import java.util.ArrayList;
import java.util.List;

public class LogListAdapter extends BaseAdapter {

    private Context context;
    public List<OkHttpRequestLog> data = new ArrayList<>();

    public LogListAdapter(Context context) {
        this.context = context;
        data.addAll(LogManager.INSTANCE.data);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.debug_list_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if ("200".equals(data.get(i).statusCode)) {
            view.setBackgroundColor(Color.WHITE);
        } else {
            view.setBackgroundColor(Color.parseColor("#ff3939"));
        }

        String[] urlStr = data.get(i).url.split("/");
        viewHolder.textViewUrl.setText(urlStr[urlStr.length - 1]);
        viewHolder.textViewTime.setText(data.get(i).timeStamp);

        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, LogDetailActivity.class);
            intent.putExtra("log", data.get(i));
            intent.putExtra("name", urlStr[urlStr.length - 1]);
            context.startActivity(intent);
        });

        return view;
    }

    class ViewHolder {
        TextView textViewUrl;
        TextView textViewTime;

        public ViewHolder(View view) {
            this.textViewUrl = view.findViewById(R.id.text_url);
            this.textViewTime = view.findViewById(R.id.timeStamp);
        }
    }
}
