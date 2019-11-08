package ch.heigvd.iict.sym.lab.comm;

import android.os.AsyncTask;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AsynchRequest extends AsyncTask<String, String, String> {

    private CommunicationEventListener communicationEventListener;
    OkHttpClient client = new OkHttpClient();

    public AsynchRequest(){

    }

    @Override
    protected String doInBackground(String... params) {
        Request.Builder builder = new Request.Builder()
                .url(params[1]).post(RequestBody.create(MediaType.parse(params[2]),params[0]));

        //-----Uncomment to make serialization task-----
        builder.addHeader("Content-Type","application/json");
        //-----Uncomment to make compression task-------
        //builder.addHeader("Network", "CSD");
        //builder.addHeader("X-Content-Encoding", "deflate");
        //----------------------------------------------

        Request request=builder.build();
        try{
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setCommunicationEventListener(CommunicationEventListener listener) {
        this.communicationEventListener = listener;
    }

    @Override
    protected  void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected  void onPostExecute(String s){
        super.onPostExecute(s);
        communicationEventListener.handleServerResponse(s);
    }
}