package com.luo.project.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.luo.project.R;

import java.util.List;

/**
 * WifiActivity
 * <p/>
 * Created by luoyingxing on 16/11/17.
 */
public class WifiActivity extends AppCompatActivity {
    private TextView textView;
    private Button button_one;
    private Button button_two;
    private Button button_three;
    private Button button_four;
    private Button button_five;

    private Button scan;
    private Button start;
    private Button stop;
    private Button check;
    private WifiAdmin mWifiAdmin;
    // 扫描结果列表
    private List<ScanResult> list;
    private ScanResult mScanResult;
    private StringBuffer sb = new StringBuffer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);


        textView = (TextView) findViewById(R.id.tv_wifi_text);
        button_one = (Button) findViewById(R.id.btn_wifi_one);
        button_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllNetWorkList();
            }
        });

        button_two = (Button) findViewById(R.id.btn_wifi_two);
        button_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiAdmin.openWifi();
                Toast.makeText(WifiActivity.this, "当前wifi状态为：" + mWifiAdmin.checkState(), Toast.LENGTH_LONG).show();
            }
        });

        button_three = (Button) findViewById(R.id.btn_wifi_three);
        button_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiAdmin.closeWifi();
                Toast.makeText(WifiActivity.this, "当前wifi状态为：" + mWifiAdmin.checkState(), Toast.LENGTH_LONG).show();
            }
        });

        button_four = (Button) findViewById(R.id.btn_wifi_four);
        button_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WifiActivity.this, "当前wifi状态为：" + mWifiAdmin.checkState(), Toast.LENGTH_LONG).show();
            }
        });

        button_five = (Button) findViewById(R.id.btn_wifi_five);
        button_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mWifiAdmin = new WifiAdmin(WifiActivity.this);

        Log.e("WifiActivity", "onCreate");
//        test();
    }


    public void getAllNetWorkList() {
        // 每次点击扫描之前清空上一次的扫描结果
        if (sb != null) {
            sb = new StringBuffer();
        }
        //开始扫描网络
        mWifiAdmin.startScan();
        list = mWifiAdmin.getWifiList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                //得到扫描结果
                mScanResult = list.get(i);
                sb = sb.append(mScanResult.BSSID + "  ").append(mScanResult.SSID + "   ")
                        .append(mScanResult.capabilities + "   ").append(mScanResult.frequency + "   ")
                        .append(mScanResult.level + "\n\n");
                textView.append(System.currentTimeMillis() + sb.toString() + "\n");
            }
            textView.setText(System.currentTimeMillis() + "扫描到的wifi网络：\n" + sb.toString());
        }
    }


    private WifiManager wifiManager;
    private List<ScanResult> scanResults;

    private Receiver receiver;

    private MyWifi myWifi = new MyWifi();


    private void test() {
        Log.e("WifiActivity", "test");
        //TODO 下面这个暂时注释掉，打包异常
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        IntentFilter filter =new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//        registerReceiver(receiver, filter);

        myWifi.openWifi(this, wifiManager);
        myWifi.registerReceiver();


//        wifiManager.startScan();


    }

    /**
     * 打开WIFI
     */
    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null &&
                    intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

                Log.e("WifiActivity", "scanResults ");
                scanResults = wifiManager.getScanResults();
                Log.e("WifiActivity", "scanResults " + scanResults.size());
            }
        }
    }

}