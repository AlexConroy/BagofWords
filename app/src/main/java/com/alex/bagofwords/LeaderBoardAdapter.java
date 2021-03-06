package com.alex.bagofwords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class LeaderBoardAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public LeaderBoardAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(LeaderBoardUser object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        ContactHolder contactHolder;
        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout, parent, false);
            contactHolder = new ContactHolder();
            contactHolder.numberTextEdit = (TextView) row.findViewById(R.id.tx_number);
            contactHolder.usernameTextEdit = (TextView) row.findViewById(R.id.tx_username);
            contactHolder.scoreTextEdit = (TextView) row.findViewById(R.id.tx_score);
            row.setTag(contactHolder);

        } else {
            contactHolder = (ContactHolder) row.getTag();
        }

        LeaderBoardUser leaderBoardUser = (LeaderBoardUser) this.getItem(position);
        contactHolder.numberTextEdit.setText(leaderBoardUser.getNumber());
        contactHolder.usernameTextEdit.setText(leaderBoardUser.getUsername());
        contactHolder.scoreTextEdit.setText(leaderBoardUser.getScore());

        return row;
    }

    static class ContactHolder {
        TextView numberTextEdit;
        TextView usernameTextEdit;
        TextView scoreTextEdit;
    }
}

