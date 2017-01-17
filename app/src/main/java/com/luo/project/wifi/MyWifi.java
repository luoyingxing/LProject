package com.luo.project.wifi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import java.util.List;


public class MyWifi implements WifiUtil.IWifiOpen {

    private Context mContext;
    WifiManager wm = null;
    private ScanResult mTemp = null;

    IntentFilter mFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

    WifiActionReceiver mWifiActionReceiver = new WifiActionReceiver();


    public void openWifi(Context context, WifiManager wn) {
        this.wm = wn;
        mContext = context;

        if (!WifiUtil.isWifiOpen(context)) {
            WifiUtil.openWifi(context, MyWifi.this);
        } else {
            mHandler.post(mCallBack);
        }
    }

    private Runnable mCallBack = new Runnable() {

        @Override
        public void run() {

            Log.d("MyWifi", "mCallBack...");

            wm.startScan();

            mHandler.postDelayed(this, 500);
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    new ScanResultTask().execute((Void) null);

                    break;
                case 1:

                    break;
                case 2:

                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onWifiOpen(final int state) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state == WifiManager.WIFI_STATE_DISABLED) {
                } else {
                    mHandler.post(mCallBack);
                }
            }
        });
    }

    public void conn() {
        WifiInfo mInfo = WifiUtil.getConnectedWifiInfo(mContext);
        if (mTemp != null) {
            if (mInfo != null) {
                if (mInfo.getSSID() != null && (mInfo.getSSID().equals(mTemp.SSID) || mInfo.getSSID().equals("\"" + mTemp.SSID + "\""))) {
                    Toast.makeText(mContext, "�Ѿ����ӱ�����wifi", Toast.LENGTH_LONG).show();
//					unregisterReceiver();
                } else {
                    connectAp(mTemp);
                }
            } else {
                connectAp(mTemp);
            }
        } else {
            Toast.makeText(mContext, "�����ڷ��丽��", Toast.LENGTH_LONG).show();
        }

    }


    public void connectAp(ScanResult mResult) {
        Log.d("MyWifi", WifiUtil.getWifiCipher(mResult.capabilities).toString());
        Log.d("MyWifi", mResult.capabilities);
        Log.d("MyWifi", WifiUtil.getEncryptString(mResult.capabilities));

        List<WifiConfiguration> mList = WifiUtil.getConfigurations(mContext);
        WifiConfiguration mCfg = null;
        //δ������Ϣ
        if (mList == null || mList.isEmpty()) {
            if (WifiUtil.getEncryptString(mResult.capabilities).equals("OPEN")) {
                //��Ӹ�wifi��Ϣ����
                WifiUtil.addNetWork(WifiUtil.createWifiConfig(mResult.SSID, "", WifiUtil.getWifiCipher(mResult.capabilities)), mContext);
            } else {
                WifiUtil.addNetWork(WifiUtil.createWifiConfig(mResult.SSID, "hahahaha123", WifiUtil.getWifiCipher(mResult.capabilities)), mContext);
            }
        } else { //����������Ϣ
            boolean flag = false;
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).SSID.equals("\"" + mResult.SSID + "\"")) {
                    Log.d("MyWifi", " ssid = " + mResult.SSID);
                    WifiUtil.addNetWork(mList.get(i), mContext);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (WifiUtil.getEncryptString(mResult.capabilities).equals("OPEN")) {
                    WifiUtil.addNetWork(WifiUtil.createWifiConfig(mResult.SSID, "", WifiUtil.getWifiCipher(mResult.capabilities)), mContext);
                } else {
                    WifiUtil.addNetWork(WifiUtil.createWifiConfig(mResult.SSID, "hahahaha123", WifiUtil.getWifiCipher(mResult.capabilities)), mContext);
                }
            }
        }

        WifiInfo mInfo = WifiUtil.getConnectedWifiInfo(mContext);
        while (mInfo == null || mInfo.getBSSID() == null || mInfo.getSSID() == null) {
            goScanResult();
        }

    }

    private void goScanResult() {
        mHandler.sendEmptyMessage(0);
    }

    class ScanResultTask extends AsyncTask<Void, Void, List<ScanResult>> {
        @Override
        protected List<ScanResult> doInBackground(Void... params) {
            List<ScanResult> mResults = WifiUtil.getWifiScanResult(mContext);
            if (mResults != null) {
                for (ScanResult mRs : mResults) {
                    if (mRs.SSID.equals("HAHAHAHA") || mRs.SSID.equals("\"HAHAHAHA\"")) {
                        mTemp = mRs;
//						conn();//�ҵ��÷���wifiʱ���Զ����ӣ�
                        break;
                    }
                }
            }
            return mResults;
        }

        @Override
        protected void onPostExecute(List<ScanResult> result) {
            super.onPostExecute(result);
        }
    }

    public void registerReceiver() {
        if (mWifiActionReceiver != null && mFilter != null)
            mContext.registerReceiver(mWifiActionReceiver, mFilter);
    }

    public void unregisterReceiver() {
        if (mWifiActionReceiver != null)
            mContext.unregisterReceiver(mWifiActionReceiver);

    }

    public void removeCallbacks() {
        if (mCallBack != null) {
            mHandler.removeCallbacks(mCallBack);
        }
    }

    class WifiActionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                goScanResult();
            }
        }

    }


}
