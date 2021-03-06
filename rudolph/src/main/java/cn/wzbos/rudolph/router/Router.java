package cn.wzbos.rudolph.router;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import cn.wzbos.rudolph.Rudolph;
import cn.wzbos.rudolph.Interceptor;
import cn.wzbos.rudolph.RouteCallback;
import cn.wzbos.rudolph.RouteType;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public abstract class Router<T> {
    String rawUrl;
    Bundle bundle;
    Class<?> target;
    RouteCallback callback;
    String routePath;
    RouteType routeType;
    String routeTag;
    Map<String, Type> queryParameters;

    public String getRawUrl() {
        return rawUrl;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public Class<?> getTarget() {
        return target;
    }

    public String getRoutePath() {
        return routePath;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public String getRouteTag() {
        return routeTag;
    }

    public Map<String, Type> getQueryParameters() {
        return queryParameters;
    }

    public Uri getUriData() {
        if (rawUrl.contains("://")) {
            return Uri.parse(rawUrl);
        }

        if(!TextUtils.isEmpty(Rudolph.getScheme())){
            return Uri.parse(Rudolph.getScheme() + "://" + rawUrl);
        }

        return Uri.EMPTY;
    }

    Router(RouteBuilder builder) {
        this.rawUrl = builder.rawUrl;
        this.callback = builder.callback;
        this.bundle = builder.args;
        this.target = builder.target;
        this.routePath = builder.routePath;
        this.routeType = builder.routeType;
        this.routeTag = builder.routeTag;
        this.queryParameters = builder.queryParameters;
    }

    boolean intercept(Context context) {

        List<Interceptor> interceptors = Rudolph.getInterceptors();
        if (null != interceptors && interceptors.size() > 0) {
            for (Interceptor interceptor : interceptors) {
                if (interceptor.intercept(context, this)) {
                    return true;
                }
            }
        }

        return false;
    }

    public abstract T open();
}
