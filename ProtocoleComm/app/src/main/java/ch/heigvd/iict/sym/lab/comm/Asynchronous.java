package ch.heigvd.iict.sym.lab.comm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Asynchronous extends AppCompatActivity {

    final Activity asynch_activity = this;
    private Button asyncSend = null;
    private EditText toSend = null;
    private EditText textResponse = null;
    private SysCommManager sysCommMan = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchronous);
        this.asynch_activity.setTitle("Asynchronous");
        this.asyncSend = findViewById(R.id.button_send_text);
        this.toSend = findViewById(R.id.toSend);
        this.textResponse = findViewById(R.id.serverResponse);


        asyncSend.setOnClickListener((v) -> {
            sysCommMan = new SysCommManager();

            sysCommMan.setCommunicationEventListener(new CommunicationEventListener() {
                @Override
                public boolean handleServerResponse(String response) {
                    textResponse.setText(response);
                    return true;
                }
            });

            try {
                sysCommMan.sendRequest(toSend.getText().toString(), "http://sym.iict.ch/rest/txt", "text/plain");
            } catch (Exception e) {
                System.out.println(e);
            }
        });

    }



    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Asynchronous.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}