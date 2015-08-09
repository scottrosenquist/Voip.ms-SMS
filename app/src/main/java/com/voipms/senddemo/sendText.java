package com.voipms.senddemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class sendText extends Activity {

    private EditText destinationEditText;
    private EditText messageEditText;
    private EditText charactersRemainingEditText;
    private Button sendButton;
    TextView resultTextView;
    ColorStateList defaultHintTextColor;
    String email;
    String password;
    String did;
    String destination;
    String message;
    String charactersRemaining;
    String apiURL="https://voip.ms/api/v1/rest.php?";
    boolean messageTooLong=false;
    boolean messageEmpty=true;
    Date date;
    DateFormat timeFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_text);


        sendButton=(Button)findViewById(R.id.send);
        messageEditText=(EditText)findViewById(R.id.message);
        //message.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(getResources().getInteger(R.integer.messageMaxLength)) });
        charactersRemainingEditText=(EditText)findViewById(R.id.charactersRemaining);
        //message.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        defaultHintTextColor=charactersRemainingEditText.getHintTextColors();

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //charactersRemainingEditText.setHint(""+(getResources().getInteger(R.integer.messageMaxLength)-messageEditText.length()));

                if((getResources().getInteger(R.integer.messageMaxLength)-messageEditText.length())>10)
                {
                    charactersRemainingEditText.setHint("");
//                    messageEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                    if(messageTooLong==true) {
                        charactersRemainingEditText.setHintTextColor(defaultHintTextColor);
                        sendButton.setEnabled(true);
                        messageTooLong=false;
                    }
                    if(messageEmpty&&messageEditText.length()==0){
                        sendButton.setEnabled(false);
                        messageEmpty=false;
                    }
                    else{
                        sendButton.setEnabled(true);
                        messageEmpty=true;
                    }


                }
//                else if((getResources().getInteger(R.integer.messageMaxLength)-messageEditText.length())>0)
//                {
//                    charactersRemainingEditText.setHint(""+(getResources().getInteger(R.integer.messageMaxLength)-messageEditText.length()));
//                    //messageEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
//
//                }
                else if((getResources().getInteger(R.integer.messageMaxLength)-messageEditText.length())>=0)
                {
                    charactersRemainingEditText.setHint(""+(getResources().getInteger(R.integer.messageMaxLength)-messageEditText.length()));
//                    messageEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    if(messageTooLong==true) {
                        charactersRemainingEditText.setHintTextColor(defaultHintTextColor);
                        sendButton.setEnabled(true);
                        messageTooLong=false;
                    }
                }
                else {
                    charactersRemainingEditText.setHint("" + (messageEditText.length() - getResources().getInteger(R.integer.messageMaxLength)));
                    if(messageTooLong==false) {
                        charactersRemainingEditText.setHintTextColor(Color.RED);
                        sendButton.setEnabled(false);
                        messageTooLong=true;
                    }
                }



            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings=new Intent(this,SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendSMS(View view){
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(this);
        destinationEditText=(EditText)findViewById(R.id.destination);
        resultTextView=(TextView)findViewById(R.id.resultTextView);
        email=sharedPref.getString("pref_email","");
        password=sharedPref.getString("pref_password","");
        did=sharedPref.getString("pref_did","");
        destination=destinationEditText.getText().toString();
        message=messageEditText.getText().toString();
        //"https://voip.ms/api/v1/rest.php?api_username=john@domain.com&api_password=password&method=getServersInfo");
        if(email != null && !email.isEmpty() && password != null && !password.isEmpty() && did != null && !did.isEmpty() && destination != null && !destination.isEmpty() && message != null && !message.isEmpty()&& message.length()<=160){
            String urlString=apiURL+"api_username="+email+"&api_password="+password+"&method=sendSMS&did="+did+"&dst="+destination+"&message="+ Uri.encode(message);
            new CallAPI().execute(urlString);
            //System.out.println("Message sent.");
            messageEditText.setText("");
            sendButton.setEnabled(false);

            date=new Date(System.currentTimeMillis());
            timeFormat= new SimpleDateFormat("h:mm aa");


            resultTextView.setText("Message sent at "+timeFormat.format(date)+".");
        }

    }

    private class CallAPI extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params){

            String urlString=params[0]; // URL to call

            String resultToDisplay = "";

            InputStream in = null;

            // HTTP Get
            try {

                URL url = new URL(urlString);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                in = new BufferedInputStream(urlConnection.getInputStream());

            } catch (Exception e ) {

                System.out.println(e.getMessage());

                return e.getMessage();

            }
            System.out.println(in);
            return resultToDisplay;
        }


    }


}
