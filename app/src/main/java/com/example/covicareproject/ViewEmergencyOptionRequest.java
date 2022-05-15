package com.example.covicareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewEmergencyOptionRequest extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lv;

    SharedPreferences sh;
    RequestQueue rq;
    String lid="",utype="";

    public static int pos;

    public static ArrayList<String> date,member_name,house_name,address,emergencyoption,rid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emergency_option_request);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lid=sh.getString("lid","");
        utype=sh.getString("utype","");
        rq= Volley.newRequestQueue(this);

        lv=(ListView) findViewById(R.id.lv);

        lv.setOnItemClickListener(this);

        date=new ArrayList<>();
        member_name=new ArrayList<>();
        emergencyoption=new ArrayList<>();
        house_name=new ArrayList<>();
        address=new ArrayList<>();
        rid=new ArrayList<>();

        ViewEmergencyRequest();
    }

    private void ViewEmergencyRequest(){

        StringRequest ja=new StringRequest(Request.Method.POST, sh.getString("url", "") + "ViewEmergencyRequest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    String status=job.getString("status");
                    if (status.equalsIgnoreCase("success")){

                        JSONArray jar=job.getJSONArray("results");
                        for(int i=0;i<jar.length();i++)
                        {
                            JSONObject jo=jar.getJSONObject(i);
                            date.add(jo.getString("date"));
                            member_name.add(jo.getString("member_name"));
                            house_name.add(jo.getString("house_name"));
                            emergencyoption.add(jo.getString("emergency_option"));
                            address.add(jo.getString("address"));
                            rid.add(jo.getString("requestid"));
                        }

                        ArrayAdapter<String> ad=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,member_name);
                        lv.setAdapter(ad);

                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error..1..."+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error..1..."+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String,String> getParams(){
                Map<String,String>p= new HashMap<String,String>();
                p.put("lid",lid);
                p.put("utype",utype);
                return p;


            }
        };
        rq.add(ja);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pos=i;
        Intent ii=new Intent(getApplicationContext(),EmergencyRequestDetails.class);
        startActivity(ii);
    }
}