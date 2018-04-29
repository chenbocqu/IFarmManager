/**
 * @Fun http请求是Bean回调接口
 * @Time 2016-11-25
 */
package com.qican.ifarmmanager.listener;

import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

public abstract class BeanCallBack<T> extends Callback<T> {
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {

        Log.i("IFMAPP_DEBUG", "parseNetworkResponse: " + response.body().string());

//        if ("lose efficacy".equals(response.body().string()))
//            throw new Exception("TOKEN_ERR");

        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            //如果用户写了泛型，就会进入这里，否者不会执行
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type beanType = parameterizedType.getActualTypeArguments()[0];
            if (beanType == String.class) {
                //如果是String类型，直接返回字符串
                return (T) response.body().string();
            } else {
                // 如果是 Bean List Map ，则解析完后返回
                return new Gson().fromJson(response.body().string(), beanType);
            }
        } else {
            //如果没有写泛型，直接返回Response对象
            return (T) response;
        }
    }
}
