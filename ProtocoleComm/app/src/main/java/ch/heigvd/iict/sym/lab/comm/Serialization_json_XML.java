package ch.heigvd.iict.sym.lab.comm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Serialization_json_XML extends AppCompatActivity implements View.OnClickListener {

    private User user;

    private EditText editTextUsername, editTextPassword, editTextResponse;
    private Button buttonLogin;

    private final Gson gson = new GsonBuilder().create();

    private final String SERVER_URL =  "http://sym.iict.ch/rest/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialization_json__xml);

        //Binding the components with the view
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextResponse = findViewById(R.id.editTextResponse);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){
            user = new User(editTextUsername.getText().toString(), editTextPassword.getText().toString());
            String serializedUser = gson.toJson(user);
            sendRequest(serializedUser, SERVER_URL);
        }
    }

    public boolean verifyServerResponse(String response){
        User tmp = gson.fromJson(response, User.class);

        if(tmp.equals(user)){
            Toast.makeText(getApplicationContext(),"Serialization is finished: success",Toast.LENGTH_LONG).show();
            return true;
        }else{
            Toast.makeText(getApplicationContext(),"Serialization is finished: failure",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void sendRequest(String request, String url){
        final SysCommManager asynchHandler = new SysCommManager();

        asynchHandler.setCommunicationEventListener(response -> {
            verifyServerResponse(response);
            editTextResponse.setVisibility(View.VISIBLE);
            editTextResponse.setText(response);
            return true;
        });
        asynchHandler.execute(request, url, "application/json");
    }
}

