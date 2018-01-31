package com.example.redsa.mychat2;

/*Simple chat application
* Amari A. Vaughn*/


import android.content.Context;
import android.database.DataSetObserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button send;
    private EditText messageText;
    private ListView Messagelist;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<View> messages=new ArrayList();



    //Root database reference
    private DatabaseReference data;

    //Message data
    private String Usernode="Annonymousname";/* yes this is spelled wrong*/

    private String Messagenode="Message";
    private String Timestamp;

    //random anonymous username
    public Random rand=new Random();
    private int Username=rand.nextInt(1000 - 0 + 1) + 0;

    // For inflating user messages
     private LayoutInflater inflater;
     private ViewGroup parent ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parent = (ViewGroup)findViewById(R.id.Ims);

        messageText=(EditText)findViewById(R.id.entermessg);
        send=(Button)findViewById(R.id.sender);




        //Messages
       // arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,messages);

        //Messagelist.setAdapter(arrayAdapter);




        //Reference to chat room
        data= FirebaseDatabase.getInstance().getReference().child("Messages");


        //Updates when new message is added;
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Iterable<DataSnapshot> b= dataSnapshot.getChildren();

                String username="";
                String message="";

                // gets most recent message
                for( DataSnapshot v: b) {

                       username=v.child(Usernode).getValue(String.class);
                        message=v.child(Messagenode).getValue(String.class);
                    if(!(username==null)) {


                           addMessage(username,message);

                        ///messages.add(username + '\n' + message);
                    }

                }



                ///  arrayAdapter.notifyDataSetChanged();

                String TAG="sjbsdbh";
                Log.i(TAG, "onDataChange: "+username);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String message=messageText.getText().toString().trim();
                if(!message.isEmpty()) {


                    DatabaseReference newmess = data.push();

                    newmess.child("Message").setValue(message);
                    newmess.child("Annonymousname").setValue("User "+ Username);

                }
            }
        });

    }


    // adds to message list
    public void addMessage(String Username,String Message ){


        messages.add(inflater.inflate(R.layout.singlemessage, null));

        View a=((ViewGroup)messages.get(messages.size()-1)).getChildAt(0);
        ((TextView)a).setText(Username);

        View b=((ViewGroup)messages.get(messages.size()-1)).getChildAt(1);
        ((TextView)b).setText(Message);


        parent.addView(messages.get(messages.size()-1));


    }

    @Override
    public void onClick(View v) {

    }
}
