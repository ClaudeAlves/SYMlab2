package ch.heigvd.iict.sym.lab.comm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GraphQL extends AppCompatActivity {

    final Activity GraphQlActivity = this;
    private Spinner spinner = null;
    private LinearLayout verticalLayout = null;

    private static final String URL = "http://sym.iict.ch/api/graphql";
    private static final String TYPE = "application/json";
    private static final String GETALLAUTHORSREQUEST =
            "{\"query\": \"{allAuthors{id first_name last_name}}\"}";

    private static final String GETALLPOSTSREQUEST =
            "{\"query\": \"{author(id: %d){posts{title content}}}\"}";


    private List authorList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_ql);
        this.spinner = findViewById(R.id.GraphQlSpinner);
        GraphQlActivity.setTitle("GraphQL");
        verticalLayout = findViewById(R.id.GraphQlTextViews);

        GenerateAuthorList();

        this.spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                getPostsbyAuthor(((Author)parent.getSelectedItem()).getId());
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
    private void GenerateAuthorList() {

        SysCommManager sysCommManager = new SysCommManager();

        sysCommManager.setCommunicationEventListener(response -> {
            try {
                JSONArray authorsArray = new JSONObject(response).getJSONObject("data").getJSONArray("allAuthors");

                JSONObject author;

                int size = authorsArray.length();
                for(int i = 0; i < size; i++ ) {
                    author = authorsArray.getJSONObject(i);

                    authorList.add(new Author(author.getInt("id"), author.getString("first_name"), author.getString("last_name")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, authorList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            return true;
        });
        try {
            sysCommManager.sendRequest(GETALLAUTHORSREQUEST, URL, TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPostsbyAuthor(int authorID) {
        SysCommManager sysCommManager = new SysCommManager();
        List<Post> postList = new ArrayList();

        sysCommManager.setCommunicationEventListener(response -> {

            Log.println(Log.INFO,"GraphQLAuteur",response);
            try {
                JSONArray postsArray = new JSONObject(response).getJSONObject("data").getJSONObject("author").getJSONArray("posts");
                JSONObject post;
                int size = postsArray.length();
                for(int i = 0; i < size; i++ ) {
                    post = postsArray.getJSONObject(i);
                    postList.add(new Post(
                            post.getString("title"),
                            post.getString("content"))
                    );
                }
                putPostListIntoLayout(postList);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        });
        try {
            sysCommManager.sendRequest(String.format(GETALLPOSTSREQUEST, authorID), URL, TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putPostListIntoLayout(List<Post> postList) {
        verticalLayout.removeAllViews();
        verticalLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView scroller;
        TextView content;
        for(Post p : postList) {
            content = new TextView(this);
            scroller = new ScrollView(this);
            content.setText(p.toString());
            content.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            scroller.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            scroller.addView(content);
            verticalLayout.addView(scroller);
        }
    }
    private class Author {

        private int id;
        private String firstName, lastName;

        public Author(int id, String firstName, String lastName){
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return  this.firstName + " " + this.lastName ;
        }
    }
    private class Post {
        private String title;
        private String post;

        public Post(String title, String post) {
            this.title = title;
            this.post = post;
        }

        @Override
        public String toString() {
            return title +"\n\n"+ post;
        }
    }
}
