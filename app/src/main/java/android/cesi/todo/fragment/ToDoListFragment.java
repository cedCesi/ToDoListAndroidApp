package android.cesi.todo.fragment;

import android.app.Fragment;
import android.cesi.todo.R;
import android.cesi.todo.Session;
import android.cesi.todo.UserListActivity;
import android.cesi.todo.adapter.TaskListAdapter;
import android.cesi.todo.helper.JsonParser;
import android.cesi.todo.helper.NetworkHelper;
import android.cesi.todo.model.HttpResult;
import android.cesi.todo.model.Task;
import android.cesi.todo.helper.Tools;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ToDoListFragment extends Fragment {

    View v;
    String token;
    EditText taskToSend;
    ImageButton sendBtn;
    ListView taskList;
    TaskListAdapter adapter;
    ImageButton userBtn;
    GetTasksAsyncTask getTasksAsyncTask;
    ProgressBar progressBar;

    Timer timer;
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            refresh();
        }
    };

    public ToDoListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        try {
            timer.schedule(task, 1500, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        getTasksAsyncTask.cancel(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        token = Session.getInstance().getToken();
        taskToSend = (EditText) v.findViewById(R.id.to_do_text);
        taskList = (ListView) v.findViewById(R.id.to_do_list);
        progressBar = (ProgressBar) v.findViewById(R.id.to_do_list_progress_bar);
        sendBtn = (ImageButton) v.findViewById(R.id.to_do_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading(true);

                new SendTaskAsyncTask(v.getContext()).execute(taskToSend.getText().toString());
                taskToSend.setText("");
            }
        });

        userBtn = (ImageButton) v.findViewById(R.id.user_btn);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            userBtn.setVisibility(View.GONE);
        } else {
            userBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ToDoListFragment.this.getActivity(), UserListActivity.class);
                    startActivity(i);
                }
            });
        }
        adapter = new TaskListAdapter(inflater.getContext(), this);
        taskList.setAdapter(adapter);

        refresh();

        return v;
    }

    public void refresh() {
        if (getTasksAsyncTask == null || getTasksAsyncTask.getStatus() != AsyncTask.Status.RUNNING) {
            getTasksAsyncTask = new GetTasksAsyncTask(ToDoListFragment.this.getActivity());
            getTasksAsyncTask.execute();
        }
    }

    public void updateTask(String toDoId) {
        loading(true);
        new UpdateTaskAsyncTask(ToDoListFragment.this.getActivity()).execute(toDoId);
    }

    protected class GetTasksAsyncTask extends AsyncTask<Void, Void, List<Task>> {

        Context context;

        public GetTasksAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected List<Task> doInBackground(Void... params) {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return null;
            }

            HttpResult result = NetworkHelper.doGet(ToDoListFragment.this.getActivity().getString(R.string.to_do_list_url), null, token);
            try {
                return JsonParser.getTasks(result.json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final List<Task> notes) {
            adapter.setTasks(notes);
        }
    }

    protected class UpdateTaskAsyncTask extends AsyncTask<String, Void, Integer> {

        Context context;

        public UpdateTaskAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected Integer doInBackground(String... parametres) {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return null;
            }

            Map<String, String> map = new HashMap<>();
            map.put("done", "true");
            HttpResult result = NetworkHelper.doPost(ToDoListFragment.this.getActivity().getString(R.string.send_note_url) + "/" + parametres[0], map, token);
            return result.code;
        }

        @Override
        public void onPostExecute(Integer status) {
            loading(false);
            if (status == 200) {
                Tools.getCustomToast(ToDoListFragment.this.getActivity(), R.string.task_updated, Toast.LENGTH_SHORT).show();
            } else {
                Tools.getCustomToast(ToDoListFragment.this.getActivity(), R.string.task_not_updated, Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected class SendTaskAsyncTask extends AsyncTask<String, Void, Integer> {

        Context context;

        public SendTaskAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected Integer doInBackground(String... parametres) {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return null;
            }

            Map<String, String> map = new HashMap<>();
            map.put("note", parametres[0]);
            HttpResult result = NetworkHelper.doPost(ToDoListFragment.this.getActivity().getString(R.string.send_note_url), map, token);
            return result.code;
        }

        @Override
        public void onPostExecute(Integer status) {
            loading(false);
            if (status == 200) {
                Tools.getCustomToast(ToDoListFragment.this.getActivity(), R.string.task_sent, Toast.LENGTH_SHORT).show();
            } else {
                Tools.getCustomToast(ToDoListFragment.this.getActivity(), R.string.task_not_sent, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
