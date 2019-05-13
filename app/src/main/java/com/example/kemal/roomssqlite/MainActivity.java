package com.example.kemal.roomssqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText ed_name, ed_surname;
    private Button btn_add;

    private AppDatabase appDatabase;
    List<User> user;
    private ArrayAdapter adapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        appDatabase = AppDatabase.getAppDatabase(this);
        listView = findViewById(R.id.list);

        ed_name = findViewById(R.id.ed_name);
        ed_surname = findViewById(R.id.ed_surname);
        btn_add = findViewById(R.id.btn_add);

        getUsers();


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed_name.getText().toString();
                String surname = ed_surname.getText().toString();

                addUser(name, surname);

            }
        });

    }

    private void addUser(String name, String surname) {
        User user = new User();
        user.setFirstName(name);
        user.setLastName(surname);
        appDatabase.userDao().insertAll(user);

        getUsers();
    }

    private void getUsers() {


        user = appDatabase.userDao().getAll();
        String[] userList = new String[user.size()];
        for (int i = 0; i < user.size(); i++) {
            userList[i] = user.get(i).getFirstName() + " " + user.get(i).getLastName();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, userList);
        listView.setAdapter(adapter);

        //Listviewde tıklanan itemi siler
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                appDatabase.userDao().delete(user.get(i));
                Toast.makeText(MainActivity.this, "Record deleted", Toast.LENGTH_SHORT).show();
                getUsers();
            }
        });
    }
}
