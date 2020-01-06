package ploywide.com.wheretoeat.API;

class AppConfig {

    private static String BASE_URL = "http://128.199.218.11";

    static String URL_INIT_CONNECTION = BASE_URL + "/connect";

    static String URL_GET_CHOICES= BASE_URL + "/choices";

    static String URL_CREATE_CHOICES= BASE_URL + "/choices/create";

    static String URL_SPIN = BASE_URL + "/spin";

    static String URL_GET_CHOICE_PROFILE = BASE_URL + "/choice_profile";

    static String URL_CREATE_CHOICE_PROFILE = BASE_URL + "/choice_profile/create";

    static String URL_DELETE_CHOICE = BASE_URL + "/choices/";
}
