package ch.heigvd.iict.sym.lab.comm;

import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import okhttp3.Response;

public class Serialization_json_XML extends AppCompatActivity implements View.OnClickListener {

    private Person person;

    private EditText editTextFirstname, editTextLastname, editTextResponse, editTextPhone, editTextGender, editTextMiddlename;
    private Button buttonLogin, buttonXML;

    private final String SERVER_URL_JSON =  "http://sym.iict.ch/rest/json";
    private final String SERVER_URL_XML =  "http://sym.iict.ch/rest/xml";

    private boolean typeransmission = false; //true = XML, false = JSON


    private final Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialization_json__xml);

        //Binding the components with the view
        editTextFirstname = findViewById(R.id.editTextFirstname);
        editTextLastname = findViewById(R.id.editTextLastname);
        editTextMiddlename = findViewById(R.id.editTextMiddlename);
        editTextGender= findViewById(R.id.editTextGender);
        editTextPhone = findViewById(R.id.editTextPhone);

        editTextResponse = findViewById(R.id.editTextResponse);

        buttonXML = findViewById(R.id.buttonXML);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonXML.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){
            person = new Person(editTextFirstname.getText().toString(), editTextLastname.getText().toString(), editTextMiddlename.getText().toString(), editTextGender.getText().toString(), editTextPhone.getText().toString());
            String serializedPerson = gson.toJson(person);
            sendRequest(serializedPerson, SERVER_URL_JSON, false);
        }else if(view == buttonXML){
            String xmlToSend = prepareXML();
            sendRequest(xmlToSend, SERVER_URL_XML, true);
        }
    }

    public boolean verifyServerResponse(String response){

        if(!response.substring(0, 4).equals("Error")){
            Toast.makeText(getApplicationContext(),"Serialization is finished: success",Toast.LENGTH_LONG).show();
            return true;
        }else{
            Toast.makeText(getApplicationContext(),"Serialization is finished: failure " + "(" + response + ")",Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public String prepareXML(){

        String xmlToSend = "";

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            DOMSource domSource = new DOMSource(document);

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0 ");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://sym.iict.ch/directory.dtd");

            Element elDirectory = document.createElement("directory");
            Element elPerson = document.createElement("person");

            Element elName = document.createElement("name");
            elName.setTextContent(editTextFirstname.getText().toString());

            Element elFirstName  = document.createElement("firstname");
            elFirstName.setTextContent(editTextLastname.getText().toString());

            Element elMiddleName = document.createElement("middlename");
            elFirstName.setTextContent(editTextMiddlename.getText().toString());

            Element elGender = document.createElement("gender");
            elFirstName.setTextContent(editTextGender.getText().toString());

            Element elPhone = document.createElement("phone");
            elPhone.setTextContent(editTextPhone.getText().toString());
            elPhone.setAttribute("type","mobile");

            document.appendChild(elDirectory);

            elDirectory.appendChild(elPerson);

            elPerson.appendChild(elName);
            elPerson.appendChild(elFirstName);
            elPerson.appendChild(elMiddleName);
            elPerson.appendChild(elGender);
            elPerson.appendChild(elPhone);


            StringWriter stringWriter = new StringWriter();
            transformer.transform(domSource, new StreamResult(stringWriter));
            xmlToSend = stringWriter.getBuffer().toString();


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return xmlToSend;
    }

    public void sendRequest(String request, String url, boolean typeTransmisssion){
        final SysCommManager asynchHandler = new SysCommManager();

        asynchHandler.setCommunicationEventListener(response -> {


            verifyServerResponse(response);
            editTextResponse.setVisibility(View.VISIBLE);
            editTextResponse.setText(response);

            return true;
        });

        if(typeTransmisssion){
            typeTransmisssion = true;
            asynchHandler.execute(request, url, "application/xml");
        }else {
            typeTransmisssion = false;
            asynchHandler.execute(request, url, "application/json");
        }
    }
}


