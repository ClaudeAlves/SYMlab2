package ch.heigvd.iict.sym.lab.comm;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SysCommManager extends AsyncTask<String, String, String> {
    private OkHttpClient client = new OkHttpClient();


    public void sendRequest(String request, String url) throws Exception {
        String params[] = {request, url, "text/plain"};
        execute(params);
    }


    public void setCommunicationEventListener (CommunicationEventListener l) {

    }

    @Override
    protected String doInBackground(String... params) {
        System.out.println("Passe par la \n");
        Request.Builder builder = new Request.Builder()
                .url(params[1]).post(RequestBody.create(MediaType.parse(params[2]),params[0]));


        //-----Uncomment to make serialization task-----
        builder.addHeader("Content-Type","test/plain");
        //-----Uncomment to make compression task-------
        //builder.addHeader("Network", "CSD");
        //builder.addHeader("X-Content-Encoding", "deflate");
        //----------------------------------------------

        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
