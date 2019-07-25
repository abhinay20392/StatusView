package com.example.statusviewexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.statusview.CustomViews.StatusView;
import com.example.statusview.Modal.StatusModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusView statusView = findViewById(R.id.statusView);
        statusView.resetStatusVisits();
        ArrayList<StatusModel> uris = new ArrayList<>();
        uris.add(new StatusModel("https://images.pexels.com/photos/87840/daisy-pollen-flower-nature-87840.jpeg?cs=srgb&dl=plant-flower-macro-87840.jpg&fm=jpg" , "Ankit Kumar","12:00 PM"));
        uris.add(new StatusModel("https://bornrealist.com/wp-content/uploads/2017/11/Here-Are-Top-10-Cute-Animals-That-Might-Actually-Kill-You.jpg" ,"Panda Man","01:00 AM"));
        uris.add(new StatusModel("https://www.planwallpaper.com/static/images/animals-4.jpg","Steve","Yesterday"));
        uris.add(new StatusModel("https://static.boredpanda.com/blog/wp-content/uuuploads/albino-animals/albino-animals-3.jpg","Grambon","10:15 PM"));
        statusView.setImageUris(uris);
    }
}
