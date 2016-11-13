package android.cesi.todo.fragment;

import android.cesi.todo.R;
import android.cesi.todo.Session;
import android.cesi.todo.adapter.UserAdapter;
import android.cesi.todo.helper.JsonParser;
import android.cesi.todo.helper.NetworkHelper;
import android.cesi.todo.model.HttpResult;
import android.cesi.todo.model.User;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;

public class UserListFragment extends Fragment {

    View v;
    String token;
    ListView userList;
    UserAdapter adapter;
    ProgressBar progressBar;

    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_user_list, container, false);

        token = Session.getInstance().getToken();
        if (token == null) {
            Toast.makeText(UserListFragment.this.getActivity(), "Non connecté", Toast.LENGTH_SHORT).show();
            UserListFragment.this.getActivity().finish();
        }

        progressBar = (ProgressBar) v.findViewById(R.id.user_list_progress_bar);
        userList = (ListView) v.findViewById(R.id.user_list);

        adapter = new UserAdapter(inflater.getContext());
        userList.setAdapter(adapter);

        refresh();

        return v;
    }

    public void refresh() {
        loading(true);
        new GetUsersAsyncTask(UserListFragment.this.getActivity()).execute();
    }

    private void loading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);

        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    protected class GetUsersAsyncTask extends AsyncTask<Void, Void, List<User>> {

        Context context;

        public GetUsersAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected List<User> doInBackground(Void... params) {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return null;
            }

            HttpResult result = NetworkHelper.doGet(UserListFragment.this.getActivity().getString(R.string.get_users_url), null, token);
            try {
                return JsonParser.getUsers(result.json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final List<User> users) {
            loading(false);
            adapter.setUsers(users);
        }
    }
}
