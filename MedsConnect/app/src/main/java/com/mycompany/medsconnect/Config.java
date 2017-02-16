package com.mycompany.medsconnect;


public class Config {

    //Address of our scripts of the CRUD
    public static final String URL_ADD="http://medconnect.16mb.com/addUser.php";
    public static final String URL_GET_USER = "http://medconnect.16mb.com/getUser.php?email=";
    public static final String URL_UPDATE_USER = "http://medconnect.16mb.com/updateUser.php";
    public static final String URL_IMAGES = "http://www.medconnect.16mb.com/getAllImages.php";
    public static final String URL_IMAGE_EMAIL = "http://medconnect.16mb.com/getImageUser.php?id=";
    public static final String URL_UPLOAD = "http://medconnect.16mb.com/upload.php";
    public static final String URL_SEND = "http://medconnect.16mb.com/send.php";
    public static final String URL_RECEIVE = "http://medconnect.16mb.com/getAllPres.php?email=";
    public static final String URL_ADDORDER = "http://medconnect.16mb.com/addOrder.php";
    public static final String URL_ADDMAINORDER = "http://medconnect.16mb.com/addMainOrder.php";
    public static final String URL_ADDORDEREDPRODUCTS = "http://medconnect.16mb.com/addOrderedProducts.php";
    public static final String URL_GETORDER = "http://medconnect.16mb.com/getOrder.php";
    public static final String URL_GETORDEREDPRODUCTS = "http://medconnect.16mb.com/getOrderedProducts.php?orderid=";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TYPE = "type";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASS = "password";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_PRODUCT = "product";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_ORDERID = "orderid";
    public static final String KEY_COST = "cost";
    public static final String KEY_ORDER_DETAILS = "details";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_ORDERID = "orderid";
    public static final String TAG_COST = "cost";
    public static final String TAG_NAME = "name";
    public static final String TAG_ADDRESS = "address";
    public static final String TAG_TYPE = "type";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PASS = "password";
    public static final String TAG_IMAGE = "image";
    public static final String TAG_PRODUCT = "product";
    public static final String TAG_QUANTITY = "quantity";
    public static final String TAG_ORDER_DETAILS = "details";


    //employee id to pass with intent
    public static final String USER_ID = "user_id";


    // flag to identify whether to show single line
    // or multi line text in push notification tray
    public static boolean appendNotificationMessages = true;

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";


    public static final String BASE_URL = "http://medconnect.16mb.com/v1";
    public static final String LOGIN = BASE_URL + "/user/login";
    public static final String USER = BASE_URL + "/user/_ID_";
    public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms";
    public static final String CHAT_THREAD = BASE_URL + "/chat_rooms/_ID_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_rooms/_ID_/message";

}