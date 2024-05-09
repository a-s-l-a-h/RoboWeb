package com.example.roboweb;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    MainActivity m = new MainActivity();
    private Context context;
    public WebAppInterface (Context context){
        this.context = context;
    }

    @JavascriptInterface
    public void leftFromWeb(String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

        m.left();

    }
    @JavascriptInterface
    public void stopFromWeb(String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
        m.stop();
    }
    @JavascriptInterface
    public void leftBackFromWeb(){
        m.leftBack();
    }
    @JavascriptInterface
    public void rightBackFromWeb(){
        m.rightBack();
    }
    @JavascriptInterface
    public void rightFromWeb(){
        m.right();
    }
    @JavascriptInterface
    public void forwardFromWeb(){
        m.forward();
    }
    @JavascriptInterface
    public void backWardFromWeb(){
        m.backWard();
    }



}
