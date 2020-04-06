package com.ee.cp.http.bean;


import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.ObservableField;

public class User {
    private ObservableField<String> imgUrl = new ObservableField<>();
    private ObservableField<String> username = new ObservableField<>();
    private ObservableField<String> password = new ObservableField<>();

    public ObservableField<String> getImgUrl() {
        return imgUrl;
    }

    @BindingAdapter("user.imgUrl")
    public static void setImgUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    public ObservableField<String> getUsername() {
        return username;
    }

    public ObservableField<String> getPassword() {
        return password;
    }

    @BindingConversion
    public static String addString(String text){
        return text + "xiaweizi";
    }
}