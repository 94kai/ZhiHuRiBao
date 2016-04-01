package com.kai.myzhihu.fragment;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 凯 on 2016/3/25 16:25
 */
public class FragmentFactory {
    private static FragmentFactory fragmentFactory;

    private static Map<String,Fragment> fragments=new HashMap<String,Fragment>();


    /**
     *
     * @param
     * @return
     * create at 2016/3/25 16:36 by 凯
    */
    public static <T extends Fragment>Fragment getFragment(Class<T> clazz){

        if(fragments.containsKey(clazz.getSimpleName())){
            return (T)fragments.get(clazz.getSimpleName());
        }else{
            try {
                T t = clazz.newInstance();
                fragments.put(clazz.getSimpleName(),t);
                return t;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
