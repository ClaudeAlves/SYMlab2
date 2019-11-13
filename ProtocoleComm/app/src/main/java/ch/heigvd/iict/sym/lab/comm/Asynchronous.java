package ch.heigvd.iict.sym.lab.comm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class Asynchronous extends AppCompatActivity {

    private final String URL = "http://sym.iict.ch/rest/txt";
    private final String TYPE = "text/plain";

    final Activity asynchActivity = this;
    private Button asyncSend = null;
    private EditText toSend = null, textResponse = null;
    private SysCommManager sysCommMan = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchronous);
        this.asynchActivity.setTitle("Asynchronous");
        this.asyncSend = findViewById(R.id.asyncSend);
        this.toSend = findViewById(R.id.asyncToSend);
        this.textResponse = findViewById(R.id.asyncResponse);

        asyncSend.setOnClickListener((v) -> {
            sysCommMan = new SysCommManager();

            sysCommMan.setCommunicationEventListener(response -> {
                textResponse.setText(response);
                return true;
            });

            try {
                sysCommMan.sendRequest(toSend.getText().toString(), URL, TYPE);
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