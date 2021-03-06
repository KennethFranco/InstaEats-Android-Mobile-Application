package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

@EActivity(R.layout.activity_customer_register)
public class CustomerRegister extends AppCompatActivity {

    Realm realm;
    SharedPreferences prefs;
    @ViewById(R.id.customerRegisterUsername)
    EditText customerRegisterU;

    @ViewById(R.id.customerRegisterPassword)
    EditText customerRegisterP;

    @ViewById(R.id.customerRegisterConfirmPassword)
    EditText customerRegisterCP;

    @ViewById(R.id.customerRegisterSigninButton)
    Button customerRegisterSigninB;

    @ViewById(R.id.customerRegisterBackLink)
    TextView customerRegisterCancelB;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @AfterViews
    public void init(){
        realm = Realm.getDefaultInstance();
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        if (customerRegisterU.getText().toString().equals("") || customerRegisterP.getText().toString().equals("") || customerRegisterCP.getText().toString().equals(""))
        {
            customerRegisterSigninB.setEnabled(false);
            customerRegisterSigninB.setTextColor(Color.parseColor("#8b8b8b"));
            customerRegisterSigninB.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray));
        }
        else{
            customerRegisterSigninB.setEnabled(true);
        }

        customerRegisterU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0 || customerRegisterP.getText().toString().equals("") || customerRegisterCP.getText().toString().equals("")){
                    customerRegisterSigninB.setEnabled(false);
                    customerRegisterSigninB.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray));
                    customerRegisterSigninB.setTextColor(Color.parseColor("#8b8b8b"));
                } else {
                    customerRegisterSigninB.setTextColor(Color.parseColor("#ffffff"));
                    customerRegisterSigninB.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.blue));
                    customerRegisterSigninB.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        customerRegisterP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0 || customerRegisterU.getText().toString().equals("") || customerRegisterCP.getText().toString().equals("")){
                    customerRegisterSigninB.setEnabled(false);
                    customerRegisterSigninB.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray));
                    customerRegisterSigninB.setTextColor(Color.parseColor("#8b8b8b"));
                } else
                {
                    customerRegisterSigninB.setTextColor(Color.parseColor("#ffffff"));
                    customerRegisterSigninB.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.blue));
                    customerRegisterSigninB.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        customerRegisterCP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0 || customerRegisterU.getText().toString().equals("") || customerRegisterP.getText().toString().equals("")){
                    customerRegisterSigninB.setEnabled(false);
                    customerRegisterSigninB.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray));
                    customerRegisterSigninB.setTextColor(Color.parseColor("#8b8b8b"));
                } else
                {
                    customerRegisterSigninB.setTextColor(Color.parseColor("#ffffff"));
                    customerRegisterSigninB.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.blue));
                    customerRegisterSigninB.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Click(R.id.customerRegisterSigninButton)
    public void signin(){
        String checkUsername = customerRegisterU.getText().toString();
        String pw1 = customerRegisterP.getText().toString();
        String pw2 = customerRegisterCP.getText().toString();

        if (checkUsername.equals("")) {
            Toast t = Toast.makeText(this, "Name must not be blank", Toast.LENGTH_LONG);
            t.show();
        } else if(pw1.equals(pw2)) {
            RealmResults<Users> result = realm.where(Users.class).equalTo("username", ""+checkUsername).findAll();

            if (result.isEmpty()){
                Users u = new Users();
                u.setUuid(UUID.randomUUID().toString());
                u.setUsername(checkUsername);
                u.setPassword(pw1);
                u.setAddress("");
                u.setFullName("");
                u.setContactNumber("");

                u.setFirstTime(true);
                prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean("userFT", true);
                edit.putString("uuid", u.getUuid());
                edit.apply();

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(u);
                realm.commitTransaction();

                Toast t = Toast.makeText(this, "Successfully created an account! You may now login!", Toast.LENGTH_LONG);
                t.show();
                finish();
                CustomerLogin_.intent(this).start();
            }
            else{
                Toast t = Toast.makeText(this, "The username you are trying to use already exists.", Toast.LENGTH_LONG);
                t.show();
            }

        } else {
            Toast t = Toast.makeText(this, "Confirm password does not match", Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Click(R.id.customerRegisterBackLink)
    public void cancel(){

        finish();
        MainActivity_.intent(this).start();
    }

    @Click(R.id.customerRegisterLoginLink)
    public void Login(){

        finish();
        CustomerLogin_.intent(this).start();
    }
}