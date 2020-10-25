package smart.network.patasuadmin.app;

public class Appconfig {

    //Key values
    public static final String shopIdKey = "shopIdKey";
    public static final String mypreference = "mypref";

    public static final String ip = "http://thestockbazaar.com";


    public static String URL_IMAGE_UPLOAD = ip + "/admin/fileUpload.php";
    public static String URL_FEED_UPLOAD = ip + "/admin/fileFeed.php";
    //Store
    public static final String CREATE_SHOP = ip + "/admin/patasuadmin/create_shop.php";
    public static final String UPDATE_SHOP = ip + "/admin/patasuadmin/update_shop.php";
    public static final String ALL_SHOP = ip + "/admin/patasuadmin/get_all_shop.php";
    public static final String DELETE_SHOP = ip + "/admin/patasuadmin/delete_shop.php";
    //Staff
    public static final String STAFF_CREATE = ip+"/admin/patasuadmin/create_staff.php";
    public static final String STAFF_UPDATE = ip + "/admin/patasuadmin/update_staff.php";
    public static final String STAFF_GET_ALL = ip+"/admin/patasuadmin/dataFetchAll.php";
    public static final String STAFF_DELETE = ip+"/admin/patasuadmin/delete_staff.php";
    //Stack
    public static final String STACK_CREATE = ip+"/admin/patasuadmin/create_product.php";
    public static final String STACK_UPDATE = ip + "/admin/patasuadmin/update_product.php";
    public static final String STACK_GET_ALL = ip+"/admin/patasuadmin/dataFetchAll_product.php";
    public static final String STACK_DELETE = ip+"/admin/patasuadmin/delete_product.php";

}
