package ch.heigvd.iict.sym.lab.comm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Compressed extends AppCompatActivity {

    private User user;

    private EditText editTextUsername, editTextPassword, editTextResponse;
    private Button buttonLogin;

    private final Gson gson = new GsonBuilder().create();

    private final String SERVER_URL =  "http://sym.iict.ch/rest/txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialization_json__xml);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextResponse);
        editTextResponse = findViewById(R.id.editTextPassword);
    }
}
