package com.example.namgiwon.band;

/**
 * Created by namgiwon on 2017. 11. 17..
 */

public class SharedMemory {
    private static SharedMemory sharedMemory = null;

    public static synchronized SharedMemory getinstance(){
        if(sharedMemory == null){
            sharedMemory = new SharedMemory();
        }
     return sharedMemory;
    }
    private String resultString;
    private UserInfo userinfo;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }





}
