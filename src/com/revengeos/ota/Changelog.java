/*
 * Copyright (C) 2020 Revenge OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.revengeos.ota;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Changelog extends AppCompatActivity {

    private String urldevice;
    private String urlrom;
    ArrayList<String> mchangelogdevice;
    ArrayList<String> mchangelogrom;
    RecyclerView mrc;
    RecyclerView mrc1;
    RecyclerView.LayoutManager mlayoutmanager;
    RecyclerView.LayoutManager mlayoutmanager1;
    RecyclerView.Adapter madapter;
    RecyclerView.Adapter madapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        urldevice = ("https://raw.githubusercontent.com/RevengeOS-Devices/official_devices/r10.0/"
                + getSystemProperty("ro.product.device")
                + "/changelog.txt");

        urlrom = ("https://raw.githubusercontent.com/RevengeOS-Devices/official_devices/r10.0/changelog.txt");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_log);

        Toolbar toolbar1 = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mchangelogdevice = new ArrayList<>();
        mchangelogrom = new ArrayList<>();

        mrc = findViewById(R.id.recycler_view);
        mrc.setHasFixedSize(true);
        mlayoutmanager = new LinearLayoutManager(this);
        mrc.setLayoutManager(mlayoutmanager);

        mrc1 = findViewById(R.id.recycler_view1);
        mrc1.setHasFixedSize(true);
        mlayoutmanager1 = new LinearLayoutManager(this);
        mrc1.setLayoutManager(mlayoutmanager1);

        new Thread(() -> {
            final List<String> changelistdevice =  GetChangelog(urldevice);
            Log.d("Parsing", urldevice);
            final List<String> changelistrom =  GetChangelog(urlrom);
            Log.d("Parsing", urlrom);

            runOnUiThread(new Runnable()
            {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < changelistdevice.size(); i++) {
                            mchangelogdevice.add("- " + changelistdevice.get(i));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Changelog.this, "Cannot fetch device changelog", 3000).show();
                    }

                    madapter = new ChangeAdapter(mchangelogdevice);
                    mrc.setAdapter(madapter);

                    try {
                        for (int i = 0; i < changelistrom.size(); i++) {
                            mchangelogrom.add("- " + changelistrom.get(i));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Changelog.this, "Cannot fetch device changelog", 3000).show();
                    }

                    madapter1 = new ChangeAdapter(mchangelogrom);
                    mrc1.setAdapter(madapter1);
                }
            });
        }).start();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<String> GetChangelog(String urlString) {
        URLConnection feedUrl;
        List<String> changelist = new ArrayList<>();

        try {
            feedUrl = new URL(urlString).openConnection();
            InputStream is = feedUrl.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                changelist.add(line);
            }
            is.close();

            return changelist;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getSystemProperty(String key) {
        String value = null;

        try {
            value = (String) Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class).invoke(null, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

}
