package com.luo.project.db;

import com.luo.project.entity.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * CrawlUtils
 * <p>
 * Created by luoyingxing on 2017/8/25.
 */

public class CrawlUtils {

    public static CrawlUtils getInstance() {
        return SingleHolder.singleObject;
    }

    private static class SingleHolder {
        private static CrawlUtils singleObject = new CrawlUtils();
    }

    public List<Info> crawlInfoList() {
        List<Info> list = new ArrayList<>();
        list.add(new Info(1, "壁纸_海量高清精选", "http://img3.imgtn.bdimg.com/it/u=4150026489,1943935114&fm=26&gp=0.jpg"));
        list.add(new Info(2, "壁纸_海量高清精选", "http://img4.imgtn.bdimg.com/it/u=2787880723,3061645111&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img3.imgtn.bdimg.com/it/u=165337335,1524178590&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img0.imgtn.bdimg.com/it/u=442340884,1909164320&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img3.imgtn.bdimg.com/it/u=3407380450,178139315&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img3.imgtn.bdimg.com/it/u=1576567003,3635369844&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img4.imgtn.bdimg.com/it/u=766879173,2974934941&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img5.imgtn.bdimg.com/it/u=2950298496,1136505419&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img2.imgtn.bdimg.com/it/u=1447822691,2273074389&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img4.imgtn.bdimg.com/it/u=1711927594,1272105846&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img1.imgtn.bdimg.com/it/u=1542403978,833282943&fm=26&gp=0.jpg"));
        list.add(new Info(3, "壁纸_海量高清精选", "http://img4.imgtn.bdimg.com/it/u=156396060,3599398840&fm=26&gp=0.jpg"));
        return list;
    }

}