package android.cesi.todo.adapter;

import android.app.Activity;
import android.cesi.todo.R;
import android.cesi.todo.model.User;
import android.cesi.todo.helper.Tools;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private final Context context;

    List<User> users;

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (users == null) {
            return 0;
        }
        return users.size();
    }

    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_user, viewGroup, false);
            vh = new ViewHolder();
            vh.user = (TextView) view.findViewById(R.id.user_name);
            vh.date = (TextView) view.findViewById(R.id.user_date);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        vh.user.setText(getItem(i).getUserName());
        vh.date.setText(context.getResources().getString(R.string.signed_up)
                + " " + Tools.getListDateFormat().format(getItem(i).getDate()));

        return view;
    }

    class ViewHolder {
        TextView user;
        TextView date;
    }

}