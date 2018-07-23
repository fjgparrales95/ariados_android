package com.ariados.ariadosclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ariados.ariadosclient.models.FriendRequest;
import com.ariados.ariadosclient.utils.ApiRequest;
import com.ariados.ariadosclient.utils.Utiles;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button bt_search;
    Button bt_friends;
    Button bt_map;
    Button bt_posts;
    TextView text_welcome;
    TextView txt_requests;
    String SESSION_KEY;
    ImageButton bt_requests;
    ApiRequest request;
    ArrayList<FriendRequest> friend_requests;
    JSONArray response_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_search = findViewById(R.id.bt_search);
        bt_posts = findViewById(R.id.bt_posts);
        bt_map = findViewById(R.id.bt_map);
        bt_friends = findViewById(R.id.bt_friends);
        bt_requests = findViewById(R.id.bt_requests);
        text_welcome = findViewById(R.id.text_welcome);
        txt_requests = findViewById(R.id.txt_requests);

        SESSION_KEY = getIntent().getStringExtra("SESSION_KEY");

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar de "layout/activity" al pulsar este botón
                Intent intent_trainers = new Intent(MainActivity.this, TrainersActivity.class);
                intent_trainers.putExtra("SESSION_KEY", SESSION_KEY);
                MainActivity.this.startActivity(intent_trainers);
            }
        });

        bt_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar de "layout/activity" al pulsar este botón
                Intent intent_friends = new Intent(MainActivity.this, FriendsActivity.class);
                intent_friends.putExtra("SESSION_KEY", SESSION_KEY);
                MainActivity.this.startActivity(intent_friends);
            }
        });

        // Para que aparezca el texto y el botón de las friend request en el caso en que existan
        try {
            friend_requests = new ArrayList<>();

            // Hacemos la llamada asíncrona con execute, pero mediante .get() rompemos la asincronía para poder obtener los valores seteados.
            request = new ApiRequest();
            request.execute("/trainers/get_friend_requests/", "GET", "", SESSION_KEY).get();

            if (request.getSuccess() && request.getIsArray()) {
                response_array = request.getResponseArray();
                ArrayList<JSONObject> json_list = Utiles.castToJSONList(response_array);
//                for(JSONObject json: json_list){
//                    friend_requests.add(new FriendRequest(json));
//                }
                if (json_list.size() > 0) {
                    bt_requests.setVisibility(View.VISIBLE);
                    txt_requests.setVisibility(View.VISIBLE);
                    txt_requests.setText("You have " + json_list.size() + " pending friend requests");
                }
            }

        } catch (Exception e) {
            request.cancel(true);
            e.printStackTrace();
        }

        // el listener de onclick del botón de las friend requests
        bt_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar de "layout/activity" al pulsar este botón
                Intent intent_requests = new Intent(MainActivity.this, FriendRequestsActivity.class);
                intent_requests.putExtra("SESSION_KEY", SESSION_KEY);
                MainActivity.this.startActivity(intent_requests);
            }
        });

        // el listener de onclick del botón del mapa
        bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar de "layout/activity" al pulsar este botón
                Intent intent_map = new Intent(MainActivity.this, MapActivity.class);
                intent_map.putExtra("SESSION_KEY", SESSION_KEY);
                MainActivity.this.startActivity(intent_map);
            }
        });

        // el listener de onclick del botón del mapa
        bt_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar de "layout/activity" al pulsar este botón
                Intent intent_posts = new Intent(MainActivity.this, PostsActivity.class);
                intent_posts.putExtra("SESSION_KEY", SESSION_KEY);
                MainActivity.this.startActivity(intent_posts);
            }
        });
    }
}
