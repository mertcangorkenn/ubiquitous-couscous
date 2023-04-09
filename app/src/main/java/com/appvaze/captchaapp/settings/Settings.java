package com.appvaze.captchaapp.settings;

public class Settings {
    /**
     * Change the amount of coins added on right captcha
     **/
    public static final Integer COINS_TO_BE_ADDED = 3;
    /**
     * Length of captcha
     **/
    public static final Integer LENGTH_OF_CAPTCHA = 7;
    /**
     * Show rating dialog after every n coins added
     **/
    public static final Integer COINS_FOR_RATING = 5;
    /**
     * Daily Captcha fill limit
     **/
    public static final Integer DAILY_CAPTCHA_LIMIT = 100;
    /**
     * Coins added after rating
     **/
    public static final Integer COINS_AFTER_RATING = 50;
    /**
     * Minimum coins to withdraw
     **/
    public static final Integer MINIMUM_COINS_TO_WITHDRAW = 300;
    /**
     * Show Interstitial Ad after how many captcha
     **/
    public static final Integer INTERSTITIAL_AD_INTERVAL  = 5;
    /**
     * Interstitial AD id
     **/
    public static final String INTERSTITIAL_AD_ID = "ca-app-pub-4771849901653081/4393409065";
    /**
     * Banner AD id - Change that from - res-> layout -> content_default.xml -> line 18. (ads:adUnitId="ca-app-pub-3940256099942544/6300978111")
     **/
    public static final String BANNER_AD_ID = "ca-app-pub-3940256099942544/10331737 12";
    /**
     * Save coins to firebase, If you want to save read & write operation make SAVE_DATA_TO_FIREBASE = false. Then coins will be stored in local database
     **/
    public static final Boolean SAVE_DATA_TO_FIREBASE = true;
}
