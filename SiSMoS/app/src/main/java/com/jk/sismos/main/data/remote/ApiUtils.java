package com.jk.sismos.main.data.remote;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://so-unlam.net.ar/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}