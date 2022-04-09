package com.example.frbpro;

import android.app.Application;

public class application extends Application {
        private String userName;
        private String ill;

        public String getId(){
            return this.userName;
        }

        public void setId(String c){
            this.userName = c;
        }

    public String getIll() {
        return ill;
    }

    @Override
        public void onCreate(){
            this.userName = "monster";
            this.ill = "尿酸";
            super.onCreate();
        }



}
