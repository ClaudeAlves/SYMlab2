package ch.heigvd.iict.sym.lab.comm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Asynchronous extends AppCompatActivity {

    private Button send_button;
    private EditText send_text;
    private final String SERVER_URL =  "http://sym.iict.ch/rest/json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchronous);
        //xasynch_activity.setTitle("Asynchronous");
        //actionBar.setDisplayHomeAsUpEnabled(true);
        send_text = findViewById(R.id.edit_send_text);
        send_button = findViewById(R.id.button_send_text);
    }

    public void onClick(View view) {
        if(view == send_button){
            String sending = new User(editTextUsername.getText().toString(), editTextPassword.getText().toString());
            String serializedUser = gson.toJson(user);
            sendRequest(serializedUser, SERVER_URL);
        }
    }
//    public boolean onOptionsItemSelected(MenuItem item){
//        Intent myIntent = new Intent(getApplicationContext(), Asynchronous.class);
//        startActivityForResult(myIntent, 0);
//        return true;
//    }
}

