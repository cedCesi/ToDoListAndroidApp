package android.cesi.todo;

import android.cesi.todo.helper.JsonParser;
import android.cesi.todo.helper.NetworkHelper;
import android.cesi.todo.helper.PreferenceHelper;
import android.cesi.todo.model.HttpResult;
import android.cesi.todo.helper.Tools;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button connexionBtn;
    Button newAccountBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = (EditText) findViewById(R.id.to_do_sign_in_name);
        password = (EditText) findViewById(R.id.to_do_sign_in_password);
        connexionBtn = (Button) findViewById(R.id.to_do_sign_in_connexion);
        newAccountBtn = (Button) findViewById(R.id.to_do_sign_in_new_account);
        progressBar = (ProgressBar) findViewById(R.id.to_do_sign_in_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        connexionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading(true);
                new ToDoSignInAsyncTask(v.getContext()).execute(username.getText().toString(), password.getText().toString());
            }
        });

        newAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    private void loading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            connexionBtn.setVisibility(View.INVISIBLE);
            newAccountBtn.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            connexionBtn.setVisibility(View.VISIBLE);
            newAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    protected class ToDoSignInAsyncTask extends AsyncTask<String, Void, String> {

        Context context;

        public ToDoSignInAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            if (!NetworkHelper.isInternetAvailable(context)) {
                return "";
            }

            try {
                Map<String, String> map = new HashMap<>();
                map.put("username", params[0]);
                map.put("pwd", params[1]);

                HttpResult result = NetworkHelper.doPost(context.getString(R.string.sign_in_url), map, null);

                if (result.code == 200) {
                    return JsonParser.getToken(result.json);
                }

                return null;
            } catch (Exception e) {
                Log.e("NetworkHelper", e.getMessage());
                return null;
            }
        }

        @Override
        public void onPostExecute(final String token) {
            loading(false);
            if ("".equals(token)) {
                Tools.getCustomToast(SignInActivity.this, R.string.no_network_available, Toast.LENGTH_SHORT).show();
            } else if (token == null) {
                Tools.getCustomToast(SignInActivity.this, R.string.sign_in_error, Toast.LENGTH_SHORT).show();
            } else {
                PreferenceHelper.setToken(SignInActivity.this, token);
                Tools.getCustomToast(SignInActivity.this, R.string.successfully_signed_in, Toast.LENGTH_SHORT).show();
                Session.getInstance().setToken(token);
                startActivity(new Intent(context, ToDoListActivity.class));
            }
        }
    }
}
