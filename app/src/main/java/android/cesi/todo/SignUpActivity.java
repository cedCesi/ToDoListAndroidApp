package android.cesi.todo;

import android.cesi.todo.helper.NetworkHelper;
import android.cesi.todo.helper.Tools;
import android.cesi.todo.model.HttpResult;
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

public class SignUpActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button newAccountBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.to_do_sign_up_name);
        password = (EditText) findViewById(R.id.to_do_sign_up_password);
        newAccountBtn = (Button) findViewById(R.id.to_do_sign_up_new_account);
        progressBar = (ProgressBar) findViewById(R.id.to_do_sign_up_progress_bar);

        newAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading(true);
                new ToDoSignUpAsyncTask().execute(username.getText().toString(), password.getText().toString());
            }
        });

    }

    private void loading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            newAccountBtn.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            newAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    protected class ToDoSignUpAsyncTask extends AsyncTask<String, Void, HttpResult> {

        @Override
        protected HttpResult doInBackground(String... params) {
            if (!NetworkHelper.isInternetAvailable(SignUpActivity.this)) {
                return null;
            }

            try {
                Map<String, String> map = new HashMap<>();
                map.put("username", params[0]);
                map.put("pwd", params[1]);

                HttpResult result = NetworkHelper.doPost(SignUpActivity.this.getString(R.string.sign_up_url), map, null);

                return result;

            } catch (Exception e) {
                Log.e("NetworkHelper", e.getMessage());
                return null;
            }
        }

        @Override
        public void onPostExecute(final HttpResult response) {
            loading(false);
            if (response.code == 200) {
                Tools.getCustomToast(SignUpActivity.this,
                        SignUpActivity.this.getString(R.string.successfully_signed_up),
                        Toast.LENGTH_LONG).show();
                SignUpActivity.this.finish();
            } else {
                Tools.getCustomToast(SignUpActivity.this,
                        SignUpActivity.this.getString(R.string.sign_up_error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
