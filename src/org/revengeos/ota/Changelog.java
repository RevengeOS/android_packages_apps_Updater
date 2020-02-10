/*
 * Copyright (C) 2020 Revenge OS
 */
/*
 * This file is part of OpenDelta.
 *
 * OpenDelta is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenDelta is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenDelta. If not, see <http://www.gnu.org/licenses/>.
 */

package org.revengeos.ota;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Changelog extends AppCompatActivity {

    private String url;
    ArrayList<String> mchangelog;
    RecyclerView mrc;
    RecyclerView.LayoutManager mlayoutmanager;
    RecyclerView.Adapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        url = ("https://raw.githubusercontent.com/RevengeOS-Devices/official_devices/r10.0/"
                + getSystemProperty("ro.product.device")
                + "/changelog.txt");
        Log.d("url", url);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_log);

        mchangelog = new ArrayList<>();

        mrc = findViewById(R.id.recycler_view);
        mrc.setHasFixedSize(true);
        mlayoutmanager = new LinearLayoutManager(this);
        mrc.setLayoutManager(mlayoutmanager);

        new Thread(new Runnable()
        {
            public void run()
            {
                final List<String> changelist =  GetChangelog(url); // format your URL
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try{
                            for (int i=0;i<changelist.size();i++) {
                                mchangelog.add("- " + changelist.get(i));
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(Changelog.this, "Cannot fetch changelog", 3000).show();
                        }

                        madapter = new ChangeAdapter(mchangelog);
                        mrc.setAdapter(madapter);

                    }
                });
            }
        }).start();


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
