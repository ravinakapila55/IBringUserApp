package com.iBring_user.app.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class URLHelper
{
    /*Publishable key
pk_test_zHMIETZXplX2JW2pcR1jxrGe00rJvbEdsU
Secret key
sk_test_Cy70sBYgT7pGdGyRnh6CnDwc009CcRl7pR */

//   Application WEB Connectivity Details
//   public static final String BASE_URL = "http://service.tranxit.co/";

//   public static final String BASE_URL="http://178.128.116.149/xuber/public/";

//    public static final String BASE_URL="http://15.206.8.59/mehdi/public/";

//    http://178.128.116.149/ibring/public/api/user/setting




    public static final String BASE_URL="http://178.128.116.149/ibring/public/";
    public static final String BASE_URL1="http://178.128.116.149/ibring/public/";
    public static final String BASE_URL_STORAGE_COURIER="http://178.128.116.149/ibring/storage/app/public/";
    public static final String BASE_URL_CHAT="http://178.128.116.149:7003/";

    public static final String BASE_URL_RESTAURANT_IMAGE="http://178.128.116.149/ibring/storage/app/public/";
    public static final String BASE_URL_RESTAURANT_MENU_IMAGE="http://localhost/ibring/storage/app/public/menu/image";
    public static final int CLIENT_ID = 5;
    public static final String CLIENT_SECRET_KEY = "gehZMcyHIkaSuxQado1EzmGyDSAi3NqzHwEqTp9N";


//  public static final String STRIPE_TOKEN = "pk_test_0G4SKYMm8dK6kgayCPwKWTXy";
//  public static final String STRIPE_TOKEN = "pk_test_zHMIETZXplX2JW2pcR1jxrGe00rJvbEdsU";

    // Help
    public static final String STRIPE_TOKEN = "pk_test_MaQhP2b8XqG9R7PTL6VUmipt";
    public static final String HELP_REDIRECT_URL = BASE_URL+"";
    public static final String CHAT_SERVER_URL ="http://13.235.235.100:8090";
    public final static String SOCKET_URL_LIVE_TRACK="http://13.235.235.100:9002/";

    public static final String APP_LINK = "https://play.google.com/store/apps/details?id=com.xuber_for_services.app";

//   public static final String CHAT_SERVER_URL ="http://178.128.116.149:7003";
//   public static final String CHAT_SERVER_URL ="http://192.168.1.49:7003";
//   public final static String SOCKET_URL_LIVE_TRACK="http://192.168.1.49:9002/";
//   public final static String SOCKET_URL_LIVE_TRACK="http://68.183.91.242:9002/";

//   public final static String PLACES_API="AIzaSyAK5It4p1CiJ2gFzWRbfs24Cibo2QTcPRU";
//   public final static String PLACES_API="AIzaSyCVYWiFBvYwSD2X4OKvqUSE23tkrvnMl8k";

    // Image load options URL

    public static final String BASE_IMAGE_LOAD_URL_WITH_STORAGE = "http://178.128.116.149/ibring/storage/app/public/";
    public static final String BASE_IMAGE_FOOD_MENU = "http://178.128.116.149/ibring-hotel/public/menu/";
    public final static String PLACES_API="AIzaSyAAgL20f3ydrQcvvl77vldDinOqPNjv0LY";


 // WEB API LIST
 // public static final String LOGIN = BASE_URL +"oauth/token";

    public static final String PAY = BASE_URL1 +"choose-plan";
    public static final String LOGIN = BASE_URL +"api/user/login";
    public static final String REGISTER = BASE_URL +"api/user/signup";
    public static final String GET_USER_PROFILE = BASE_URL +"api/user/details";

    public static final String GET_PROVIDER_PROFILE = BASE_URL +"api/user/provider";
    public static final String USER_PROFILE_UPDATE = BASE_URL +"api/user/update/profile";
    public static final String GET_SERVICE_LIST_API = BASE_URL +"api/user/services";
    public static final String REQUEST_STATUS_CHECK_API = BASE_URL +"api/user/request/check";

    public static final String ESTIMATED_FARE_DETAILS_API = BASE_URL +"api/user/estimated/fare";
    public static final String SEND_REQUEST_API = BASE_URL +"api/user/send/request";
    public static final String CANCEL_REQUEST_API = BASE_URL +"api/user/cancel/request";
    public static final String PAY_NOW_API = BASE_URL +"api/user/payment";

    public static final String RATE_PROVIDER_API = BASE_URL +"api/user/rate/provider";
    public static final String CARD_PAYMENT_LIST = BASE_URL +"api/user/card";
    public static final String ADD_CARD_TO_ACCOUNT_API = BASE_URL +"api/user/card";
    public static final String DELETE_CARD_FROM_ACCOUNT_API = BASE_URL +"api/user/card/destory";

    public static final String GET_HISTORY_API = BASE_URL +"api/user/trips";
    public static final String GET_HISTORY_DETAILS_API = BASE_URL +"api/user/trip/details";
    public static final String ADD_CARD = BASE_URL +"api/user/add/money";
    public static final String COUPON_LIST_API = BASE_URL +"api/user/promocodes";

    public static final String ADD_COUPON_API = BASE_URL +"api/user/promocode/add";
    public static final String CHANGE_PASSWORD_API = BASE_URL +"api/user/change/password";
    public static final String UPCOMING_TRIP_DETAILS = BASE_URL +"api/user/upcoming/trip/details";
    public static final String UPCOMING_TRIPS = BASE_URL +"api/user/upcoming/trips";

    public static final String GET_PROVIDERS_LIST_API = BASE_URL +"api/user/show/providers";
    public static final String RESET_PASSWORD = BASE_URL +"api/user/reset/password";
    public static final String FORGET_PASSWORD = BASE_URL +"api/user/forgot/password";
    public static final String GET_HELP_DETAILS = BASE_URL + "api/user/help";

    public static final String RESTAURANT_LISTING = BASE_URL + "api/user/restaurant";
    public static final String RESTAURANT_MENU_LISTING = BASE_URL + "api/provider/menu-list";
    public static final String LOGOUT = BASE_URL + "api/user/logout";
    public static final String GET_CAB_TYPE = "api/provider/cab-list";

    public static final String GET_APP_SERVICES = "api/provider/service-list";
    public static final String GET_FARE = "api/provider/fare-matrix";
    public static final String GET_CONERSATION_ID = "api/chat/conversation/id";
    public static final String PLACED_ORDER = "api/user/place-order";

    public static final String FOOD_HISTORY = "api/user/food-history";
    public static final String CANCEL_REASON = "api/user/get-cancel-reason";
    public static final String GET_PARCEL_TYPE = "api/user/parcel-type";
    public static final String GET_CITY_LIST = "api/user/city-zone";
    public static final String SEND_COURIER_REQUEST = "api/user/send-courier";
    public static final String COURIER_HISTORY = "api/user/courier-history";

    public static final String ESTIMATED_COURIER_PRICE = "api/user/courier-fare";
    public static final String SETTINGS = "api/user/setting";
    public static final String GET_OUTSTANDING_BOOKING = "api/user/get-unpaid-drive";
    public static final String GET_CONVERSATION = "api/chat/message";

    public static final String CHAT_TIME_FORMAT ="dd-mm-yyyy hh:mm a";

    //http://178.128.116.149/ibring/api/user/food-history
    //http://178.128.116.149/ibring/public/api/user/get-cancel-reasons
//http://178.128.116.149/ibring/public/api/user/city-zone
    //api/user/send-courier


    public static String getFormatedTime(String dateStr, String strReadFormat, String strWriteFormat)
    {
        try
        {
            String formattedDate = dateStr;

            DateFormat readFormat = new SimpleDateFormat(strReadFormat);
            readFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            DateFormat writeFormat = new SimpleDateFormat(strWriteFormat);
            writeFormat.setTimeZone(TimeZone.getDefault());
            Date date = null;
            try
            {
                date = readFormat.parse(dateStr);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            if (date != null)
            {
                formattedDate = writeFormat.format(date);
            }

            return formattedDate;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return dateStr;
        }
    }

    public static String getFormatedDate(String dateStr, String strReadFormat, String strWriteFormat)
    {
        String formattedDate = dateStr;
        DateFormat readFormat = new SimpleDateFormat(strReadFormat);
        DateFormat writeFormat = new SimpleDateFormat(strWriteFormat);
        Date date = null;
        try
        {
            date = readFormat.parse(dateStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();

        }

        if (date != null)
        {
            formattedDate = writeFormat.format(date);
        }

        return formattedDate;
    }

}
