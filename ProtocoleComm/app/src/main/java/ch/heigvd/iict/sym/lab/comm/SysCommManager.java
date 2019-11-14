/**
 * @author Claude-André Alves, Catel Torres Arzur Gabriel, Thomas Benjamin
 *
 * @about class qui permet de faire des requêtes sur un serveur grâce à la méthode sendRequest qui
 *          prend en paramètre la requête l'url ansi que le type d'acceptation.
 */
package ch.heigvd.iict.sym.lab.comm;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SysCommManager extends AsyncTask<String, String, String> {
    private OkHttpClient client = new OkHttpClient();
    CommunicationEventListener communicationEventListener;


    public void sendRequest(String request, String url, String accept) throws Exception {
        String params[] = {request, url, accept};
        execute(params);
    }


    public void setCommunicationEventListener (CommunicationEventListener l) {
        this.communicationEventListener = l;
    }

    @Override
    protected String doInBackground(String... params) {
        Request.Builder builder = new Request.Builder()
                .url(params[1]).post(RequestBody.create(MediaType.parse(params[2]),params[0]));

         //-----Uncomment to make compression task-------
        //builder.addHeader("X-Network", "CSD");
        //builder.addHeader("X-Content-Encoding", "deflate");
        //builder.addHeader("Accept", "text/plain");
        //----------------------------------------------
        builder.addHeader("Content-Type",params[2]);


        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            int statusCode = response.code();
            switch(statusCode) {
                case 200:
                    return response.body().string();
                case 400:
                    return "Error 400 - Bad request";
                case 401:
                    return "Error 401 - Unauthorized request";
                case 404:
                    return "Error 404 - Not found";
                case 500:
                    return "Error 500 - Error server";
                case 503:
                    return "Error 503 - Error server";
                case 504:
                    return "Error 504 - Server not responding";
                default:
                    return "Error " + statusCode + " - Error unknown";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
