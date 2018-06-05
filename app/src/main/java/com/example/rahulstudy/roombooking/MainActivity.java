package com.example.rahulstudy.roombooking;

import android.app.Dialog;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity
{
    private static final Integer[] IMAGES= {R.drawable.room1,R.drawable.room2,R.drawable.room3};
    private String[] titles={"Comfortable Rooms","Hot Water","Attached Bathrooms"};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private String[] userTypes;

    ViewPager mViewPager;
    TextView pictitle;
    EditText rooms;
    Button bookingButton;

    public int roomqty;
    Dialog confirmationPopup;
    TextView status;

    private String userType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    public void init()
    {
        initializeConfirmPopup();
        initializeSpinner();
        status=(TextView)findViewById(R.id.status);
        bookingButton=(Button)findViewById(R.id.BookingButton);
        rooms=findViewById(R.id.rooms);
        pictitle=(TextView)findViewById(R.id.pictitle);
        mViewPager = (ViewPager) findViewById(R.id.viewPage);
        ImageAdapter adapterView = new ImageAdapter(this);
        mViewPager.setAdapter(adapterView);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pictitle.setText(titles[position]);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    Spinner spinner;
    public void initializeSpinner()
    {
        Resources res=getResources();
        userTypes=res.getStringArray(R.array.UserType);
        spinner = (Spinner) findViewById(R.id.userType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.UserType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                userType=userTypes[position];
            }
        });
    }

    EditText registernumber,userpassword,email;
    public void initializeConfirmPopup()
    {
         confirmationPopup=new Dialog(this);
        confirmationPopup.setContentView(R.layout.login_popup);
         registernumber=confirmationPopup.findViewById(R.id.registernumber);
         userpassword=confirmationPopup.findViewById(R.id.password);
         email=confirmationPopup.findViewById(R.id.email);
    }

    public void confirmBooking(View view)
    {
        Boolean isRoomsValid=false;
        if(userType!="")
        {
            Toast.makeText(this,"Please select the type of user",Toast.LENGTH_SHORT).show();
        }
        int NumberOfRooms=0;
        try{
            NumberOfRooms=Integer.parseInt(rooms.getText().toString());
            if(NumberOfRooms>0) {
                isRoomsValid = true;
                roomqty=NumberOfRooms;
            }

        }
        catch(Exception e)
        {Toast.makeText(this,"Please input the number of rooms required",Toast.LENGTH_SHORT).show();}

        if(isRoomsValid)
        {
            confirmationPopup.show();
        }

    }

    public void verifyDetails(View view)
    {
        String email=this.email.getText().toString();
        String password=this.userpassword.getText().toString();
        String registernumber=this.registernumber.getText().toString();

        if(!email.equals(""))
        {
            //Email otp opens over here
            //For temporaray purpose we use firebase direct registration over here

        }
        else if(!registernumber.equals(""))
        {
            if(!password.equals(""))
            {
                //register number and password validation happens over here
                //Firebase is updated over here
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("requests");
                String key=myRef.push().getKey();
                UserData user=new UserData();
                user.setRegister(registernumber);
                user.setPassword(password);
                user.setUserType(userType);
                user.setRooms(roomqty);
                myRef.child(key).setValue(user);
                confirmationPopup.dismiss();

                //Disabling components after registration
                bookingButton.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                rooms.setVisibility(View.INVISIBLE);

                //Inputting the details for the status view now
                status.setText("Your request is under process");

            }
            else
            {Toast.makeText(this,"Please enter valid password!!",Toast.LENGTH_SHORT).show();}
        }
        else
        {
            Toast.makeText(this,"Invalid Verfication details!!",Toast.LENGTH_SHORT).show();
        }



    }


    public void RequestListener()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rooms");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    if(dataSnapshot.child("user").getValue().equals(email))
                    {
                        status.setText("You Have been Permitted Room : 1A");
                    }
                }
            }

            //this is what i am goinh to do for making a change
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //I kind of dont like where this is going

    }

}
