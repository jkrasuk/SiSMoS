package com.jk.sismos.main.data.remote;

public class ApiUtils {

    public static final String BASE_URL = "http://so-unlam.net.ar/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}