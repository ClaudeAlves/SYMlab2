package ch.heigvd.iict.sym.lab.comm;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SysCommManager {
    public void sendRequest(String request, String url) throws Exception {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(request.getBytes());
        os.flush();
        os.close();
    }
    public void setCommunicationEventListener (CommunicationEventListener l) {

    }
}
