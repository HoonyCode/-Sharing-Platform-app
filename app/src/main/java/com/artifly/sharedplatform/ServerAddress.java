package com.artifly.sharedplatform;

public class ServerAddress {
    private String serverURL;

    private ServerAddress() {
        serverURL = "http://172.30.1.7:8080/api/";
    }

    public String getServerURL() {
        return serverURL;
    }

    private static ServerAddress instance = null;
    public static synchronized ServerAddress getInstance(){
        if(null == instance){
            instance = new ServerAddress();
        }
        return instance;
    }
}
