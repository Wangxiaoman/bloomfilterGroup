package com.paradigm.constants;

import java.util.Arrays;
import java.util.List;

public class Constants {
	
	public static final int PHONE_CODE_EXPIRE_SECOND = 600;

    public final static int SMS_CODE_MIN = 100000;
    public final static int SMS_CODE_MAX = 999999;
    
    public final static long TOKEN_EXPIRE_SECONDS = 3*24*60*60;
    
    public static final String REQUEST_BUSINESS = "business";
    
    public static final String TIME_SEPARATOR = "-";
    
    public static final String TIME_FOMART = "yyyyMMdd";
    public static final String TIME_END_FOMART = "yyyyMMddHHmmss";
    
    public static final String ANDROID_SDK_QCLOUD_URL = "http://recom-1252788780.cos.ap-beijing.myqcloud.com/sdk/androidsdk.zip";
    public static final String IOS_SDK_QCLOUD_URL = "http://recom-1252788780.cos.ap-beijing.myqcloud.com/sdk/iossdk.zip";
    
    public static final long ONE_YEAR_MS= 3600 * 24 * 31 * 1000L;
    
    public static final int BUSINESS_MAX_ITEM_SET = 30;
    public static final int ITEM_BATCH_MAX_NUMBER = 100;
    public static final int BUSINESS_NAME_MAX_COUNT = 25;
    
    public static final int SCENE_NAME_MAX_LENGTH = 25;
    
    public static final int ITEM_TITLE_MAX_LENGTH = 120;
    public static final int ITEM_CONTENT_MAX_LENGTH = 2000;
    public static final int ITEM_CONTENT_PIC_MAX_LENGTH = 10000;
    public static final int ITEM_SUMMARY_MAX_LENGTH = 200;
    public static final int ITEM_TAG_MAX_LENGTH = 120;
    public static final int ITEM_CATAGORY_MAX_LENGTH = 250;
    public static final int ITEM_URL_MAX_LENGTH = 250;
    public static final int ITEM_COVER_URL_MAX_LENGTH = 2040;
    public static final int ITEM_TAG_URL_MAX_LENGTH = 1000;
    
    public static final int ITEM_MAX_NUMBER = 500000;
    public static final int ITEM_PAGE_MAX_NUMBER = 10000;
    
    public static final int SCENE_ITEM_RULE_MAX = 4000;
    
    public static final String X_INNER_TOKEN = "0b0b16df318c7d7a1dde3ce5e9e1bfe6";
    public static final String HTTP = "http";
    
    /** js name */
    public static final String SHOW_JS = "showJs";
    public static final String DETAIL_PAGE_SHOW_JS = "detailShowJs";
    public static final String RECOMMEND_JS = "recommendJs";
    public static final String RENDER_JS = "renderJs";
    
    /** request */
    public static final String REQUEST_HEAD = "application/json; charset=utf-8";
    public static final String RESULT_STATUSES = "statuses";
    
    // api log
    public static final String CONTEXT = "context";
    public static final String CONTEXT_EXIST = "contextExist";
    public static final String ITEM_SET_ID = "itemSetId";
    public static final String ITEM_URL = "url";
    public static final String DATE_FIELD = "date";
    public static final String ITEM_ID_FIELD = "itemId";
    public static final String REQUEST_USERID_4PARADIGM = "para4id";
    public static final String ACCESS_TOKEN_FIELD = "clientToken";
    public static final String REQUEST_ID = "requestID";
    public static final String VERSION_FIELD = "version";
    public static final String SCENE_ID_FIELD = "sceneId";
    public static final String REMOTE_PORT = "remotePort";
    public static final String PRIVATE_IP = "privateIp";
    public static final String PUBLIC_IP = "pubIp";
    public static final String API_LOG_POST = "apiLogPost";
    
    // log field
    public static final String LOG_NAME_ACTIONS = "actions";
    public static final String LOG_NAME_ACTION = "action";
    public static final String LOG_NAME_ACTION_ID = "actionId";
    public static final String LOG_NAME_ACTION_TIME = "actionTime";
    public static final String LOG_NAME_UID = "uid";//not login user id
    public static final String LOG_NAME_USERID = "userId";
    public static final String LOG_NAME_CUSTOM_USER_ID = "customUserId";
    public static final String LOG_NAME_REQUESTID = "requestId";



    //scene_rule json 字段
    public static final String PUBLISHER_ID = "publisher_id";
    public static final String SORT_ASC = "::sort.asc";
    public static final String SORT_DESC = "::sort.desc";
    public static final String PUBLISHER_ID_STR = "您的内容库中不存在发布者ID为“%s”项，请您确认并修改";
    
    
    public static final String PLUG_ITEM_SET_NAME_PREFIX="插件物料-";
    public static final String BUSINESS_RANK_PLUG = "plug";
    
    // 用户行为事件
    // loadBoards - 花瓣的图片画板关系
    public static final List<String> LOG_USER_ACTION_NAMES = Arrays.asList("show", "detailpageshow",
            "collect", "share", "follow", "like", "dislike", "duration", "appforeground","comment",
            "appstarted", "appbackground", "csdnstatistics", "csdncall", "save", "loadboards","activate");
    public static final String ACTIVATE_ACTION = "activate";
    
    public static enum SdkType {
        // sdk类型
		DEFAULT, ANDROID, IOS, PC
	}
    
    public static enum ItemUploadType {
        // 物料上报类型
		DEFAULT, API, CRAWL,FRONT_CRAWL
	}
    
    public static enum ItemSetCheck {
        // 物料操作方式
		DEFAULT, NOTBIND, BIND, ONLINE
	}
    
    public static enum TimeUnit {
        // 时间单位
		DEFAULT, HOUR, DAY, MONTH
	}
    
    public static enum ItemOpKeyType {
        // 物料操作key为itemid还是url
		DEFAULT, ITEMID, URL
	}
    public static enum BusinessSource{
        // 是否插件
        NO_PLUG, PLUG
    }
}