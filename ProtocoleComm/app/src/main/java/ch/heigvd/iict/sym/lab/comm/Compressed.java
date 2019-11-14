package ch.heigvd.iict.sym.lab.comm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import static android.os.FileUtils.copy;

public class Compressed extends AppCompatActivity implements View.OnClickListener {

    private User user;

    private EditText editTextUsername, editTextPassword, editTextResponse;
    private Button buttonLogin;

    private final Gson gson = new GsonBuilder().create();

    private final String SERVER_URL =  "http://sym.iict.ch/rest/txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compressed);

        //Binding the components with the view
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword=findViewById(R.id.editTextPassword);
        editTextResponse=findViewById(R.id.editTextResponse);

        buttonLogin=findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin)
        {

            user = new User(editTextUsername.getText().toString(), editTextPassword.getText().toString());
            String data = gson.toJson(user);

            try {
                sendRequest((compress(data)), SERVER_URL);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String compress(String string) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);
        DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater);
        dos.write(string.getBytes());
        dos.finish();

        return baos.toByteArray().toString();
    }

    public static String decompress(String string) throws Exception {
        if(string == null) {
            return null;
        }

        byte[] compressedData = string.getBytes();
        InflaterInputStream inputStream = new InflaterInputStream(new ByteArrayInputStream(compressedData));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length * 2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            copy(inputStream, outputStream);
        }else{
            throw new Exception("SDK version is too low");
        }
        byte[] decompressedData = outputStream.toByteArray();
        return new String(decompressedData, "UTF8");

    }

    public boolean verifyServerResponse(String response) throws Exception {

        User temp = gson.fromJson(new String(decompress(response)), User.class);

        if(temp.equals(user)){
            Toast.makeText(getApplicationContext(),"Compression treatment is finished: success",Toast.LENGTH_LONG).show();
            return true;
        }else{
            Toast.makeText(getApplicationContext(),"Compression treatment is finished: failure",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void sendRequest(String request, String url) {
        final SysCommManager asynchHandler = new SysCommManager();

        asynchHandler.setCommunicationEventListener(response -> {

            try {
                verifyServerResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            editTextResponse.setVisibility(View.VISIBLE);
            editTextResponse.setText(response);

            return true;
        });
        asynchHandler.execute(request, url, "text/plain");

    }
}
