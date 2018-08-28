package com.luo.project.auto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.luo.project.Constant;
import com.luo.project.nohttp.NoHttpActivity;
import com.luo.project.utils.FileUtils;

/**
 * <p/>
 * Created by luoyingxing on 2018/8/28.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //标准的写法是需要判别Action的类型的
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //你想执行的操作
            FileUtils.savePref(Constant.AUTO_RUN_APP, "3");


            Intent intent1 = new Intent(context, NoHttpActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);

        }
    }
}