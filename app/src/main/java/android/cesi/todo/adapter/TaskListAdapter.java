package android.cesi.todo.adapter;

import android.app.Activity;
import android.cesi.todo.R;
import android.cesi.todo.fragment.ToDoListFragment;
import android.cesi.todo.model.Task;
import android.cesi.todo.helper.Tools;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TaskListAdapter extends BaseAdapter {

    private final Context context;
    private List<Task> tasks;
    private ToDoListFragment toDoListFragment;

    public TaskListAdapter(Context context, ToDoListFragment toDoFragment) {
        this.context = context;
        this.toDoListFragment = toDoFragment;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (tasks == null) {
            return 0;
        }
        return tasks.size();
    }

    @Override
    public Task getItem(int i) {
        return tasks.get(i);
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
            view = inflater.inflate(R.layout.item_task, viewGroup, false);
            vh = new ViewHolder();
            vh.user = (TextView) view.findViewById(R.id.to_do_user);
            vh.note = (TextView) view.findViewById(R.id.to_do_text);
            vh.done = (CheckBox) view.findViewById(R.id.done);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        vh.user.setText(getItem(i).getUsername() + " "
                + context.getResources().getString(R.string.wrote) + " "
                + Tools.SDF.format(getItem(i).getDate()));
        vh.note.setText(getItem(i).getNote());

        if (getItem(i).getDone()) {
            vh.done.setChecked(true);
        } else {
            vh.done.setChecked(false);
        }

        vh.done.setTag(i);

        vh.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index = (Integer) view.getTag();

                CheckBox cb = (CheckBox) view.findViewById(R.id.done);
                // an already done task can't be unchecked
                if (!cb.isChecked()) {
                    cb.setChecked(true);
                    Tools.getCustomToast(((Activity) context), R.string.no_update_allowed, Toast.LENGTH_SHORT).show();
                } else {
                    toDoListFragment.updateTask(TaskListAdapter.this.getItem(index).getId());
                }
            }
        });

        return view;
    }

    class ViewHolder {
        TextView user;
        TextView note;
        CheckBox done;
    }
}
