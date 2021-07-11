package com.samuel.roti.server;

public class BaseURL {
    public static String baseUrl = "http://192.168.43.94:3000/";
    //user
    public static String login    = baseUrl + "user/login";
    public static String register = baseUrl + "user/registrasi";
    //kue
    public static String inputKuee = baseUrl + "kue/input";
    public static String dataKue = baseUrl + "kue/datakue";
    public static String editDataKue = baseUrl + "kue/ubah/";
    public static String hapusDataKue = baseUrl + "kue/hapus/";
}
