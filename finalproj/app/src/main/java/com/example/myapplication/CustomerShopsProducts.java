package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;
import io.realm.RealmResults;

@EActivity(R.layout.activity_customer_shops_products)
public class CustomerShopsProducts extends AppCompatActivity {

    Realm realm;
    @ViewById(R.id.shopProductsRecyclerView)
    RecyclerView rV;

    @ViewById(R.id.textView7)
    TextView title;


    @AfterViews
    public void init(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rV.setLayoutManager(mLayoutManager);

        realm = Realm.getDefaultInstance();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String prefsID = prefs.getString("viewShopUUID", null);

        // query by shop UUID
        RealmResults<Products> list = realm.where(Products.class).equalTo("shop_uuid", prefsID).findAll();

        System.out.println(list);
        System.out.println(prefsID);
        System.out.println(realm.where(Products.class).findAll());


        ShopProductsAdapter adapter = new ShopProductsAdapter(this, list, true);
        rV.setAdapter(adapter);

        Shops result = realm.where(Shops.class)
                .equalTo("uuid", prefsID)
                .findFirst();

        String name = result.getShopName();

        title.setText(name + "'s Products");


    }

    @Click(R.id.customerShopsProductsBackButton)
    public void back(){
        finish();
        CustomerShops_.intent(this).start();
    }

    public void add(Products u){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        String uuid = u.getUuid();

        System.out.println(uuid);
        edit.putString("productUUID", uuid);
        edit.apply();

        finish();
        CustomerShopsSpecificProduct_.intent(this).start();
    }


}