package me.jbusdriver.base.common;

import me.jbusdriver.base.ACache;

/**
 * Created by Administrator on 2017/4/9.
 */

public class C {

    //推荐component
    public static class C_RECOMMEND {
        public static final String Recommend_Open = "recommend/open";
        public static final String Recommend_Like_It = "recommend/like/it";
        public static final String Recommend_Like_Count = "recommend/like/count";
    }

    //推荐component
    public static class C_IMAGE_BROWSER {
        public static final String Browser_Images = "browser/images";

    }

    //movie detail
    public static class C_MOVIE_DETAIL {
        public static final String Open_Movie_Detail = "movie/detail/open";

    }

    public static class Cache {
        public static final int DAY = ACache.TIME_DAY; //缓存的固定时间
        public static final int WEEK = ACache.TIME_DAY * 7; //缓存的固定时间

        public static final String ANNOUNCE_URL = "announceUrl"; //app api地址
        public static final String ANNOUNCE_VALUE = "announce_value"; // api 值
        public static final String BUS_URLS = "bus_urls"; //bus 地址
        public static final String IMG_HOSTS = "img_hosts"; //bus 地址

    }


    public static class SavedInstanceState {
        public static final String RECREATION_SAVED_STATE = "RECREATION_SAVED_STATE";
        public static final String LOADER_ID_SAVED_STATE = "LOADER_ID_SAVED_STATE";
        public static final String LOADER_SAVED_STATES = "LOADER_SAVED_STATES:";
    }

    public static class BundleKey {
        public static final String Key_1 = "Key_1";
        public static final String Key_2 = "Key_2";
    }
}
