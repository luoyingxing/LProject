package com.luo.project.gallery;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.luo.project.R;
import com.luo.project.adapter.CommonAdapter;
import com.luo.project.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class GalleryActivity extends AppCompatActivity {
    private Button button;
    private GridView gridView;

    private CommonAdapter<PhotoInfo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        button = (Button) findViewById(R.id.button);
        gridView = (GridView) findViewById(R.id.grid_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });

        adapter = new CommonAdapter<PhotoInfo>(this, new ArrayList<PhotoInfo>(), R.layout.item_provider_list) {
            @Override
            public void convert(ViewHolder helper, PhotoInfo item) {
                ImageView imageView = helper.getView(R.id.image_view);
                imageView.setImageURI(Uri.parse(item.getPhotoPath()));
            }
        };
        gridView.setAdapter(adapter);
    }

    private int REQUEST_CODE_GALLERY = 102;
    private String photoPath = "/storage/emulated/0/magicLamp/img/1492506454713..jpg";

    private void click() {
        //单选打开相册:
//        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, new ResultCallback());

        //多选打开相册
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, 9, new ResultCallback());

        //拍照
//        GalleryFinal.openCamera(REQUEST_CODE_GALLERY, new ResultCallback());

        //裁剪
//        GalleryFinal.openCrop(REQUEST_CODE_GALLERY, photoPath, new ResultCallback());

        //图片编辑
//        GalleryFinal.openEdit(REQUEST_CODE_GALLERY, photoPath, new ResultCallback());
    }

    private class ResultCallback implements GalleryFinal.OnHanlderResultCallback {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            for (int i = 0; i < resultList.size(); i++) {
                Log.e("GalleryActivity", "Success  " + resultList.get(i).getPhotoPath());
            }
            adapter.addAll(resultList);
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Log.e("GalleryActivity", "Failure  errorMsg = " + errorMsg);
        }
    }
}
