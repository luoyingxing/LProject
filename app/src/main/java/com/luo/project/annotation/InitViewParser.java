package com.luo.project.annotation;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

/**
 * InitViewParser
 * <p/>
 * Created by luoyingxing on 16/9/28.
 */
public class InitViewParser {

    public static void inject(Object object) {

        try {
            parse(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parse(Object object) throws Exception {
        final Class<?> clazz = object.getClass();
        View view = null;
        Field[] fields = clazz.getDeclaredFields(); //遍历Activity中的所有成员变量
        for (Field field : fields) {
            Log.e("-- parse -- ", field.getName());
            if (field.isAnnotationPresent(InitViewById.class)) {
                InitViewById injectView = field.getAnnotation(InitViewById.class);

                int id = injectView.id();
                if (id < 0) {
                    throw new Exception("id must not be null");
                } else {
                    field.setAccessible(true);
                    if (object instanceof View) {
                        view = ((View) object).findViewById(id);
                    } else if (object instanceof Activity) {
                        view = ((Activity) object).findViewById(id);
                        if (field.isAnnotationPresent(OnClick.class)){
                            view.setOnClickListener((View.OnClickListener) object);
                        }
                    }
                    field.set(object, view);
                }

            }

        }

    }

}
