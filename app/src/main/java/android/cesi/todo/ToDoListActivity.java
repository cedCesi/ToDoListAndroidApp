package android.cesi.todo;

import android.cesi.todo.fragment.ToDoListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ToDoListActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        NavigationView nav = (NavigationView) findViewById(R.id.to_do_list_nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_to_do_list);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_to_do_users:
                        drawerLayout.closeDrawers();
                        Intent i = new Intent(ToDoListActivity.this, UserListActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.menu_to_do_disconnect:
                        //startActivity(new Intent(ToDoListActivity.this, SignInActivity.class));
                        finish();
                        return true;
                    case R.id.menu_to_do_refresh:
                        ((ToDoListFragment) getFragmentManager().findFragmentById(R.id.to_do_fragment)).refresh();
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });
    }
}
