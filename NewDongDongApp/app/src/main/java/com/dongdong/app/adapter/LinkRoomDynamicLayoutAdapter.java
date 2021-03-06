package com.dongdong.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd121.community.R;
import com.dongdong.app.base.BaseApplication;
import com.dongdong.app.bean.FunctionBean;
import com.dongdong.app.widget.DynamicItemContainView;

public class LinkRoomDynamicLayoutAdapter extends BaseAdapter {

    private static final int FUNCTION_TEMP_COUNT = 14;
    public static final int COMMON_VIEWPAGER = 100;
    private static final int AD_VIEWPAGER = 101;

    private static final int TYPE_DRAG_VIEW = 0;
    private static final int TYPE_FIXEDVIEW_ITEM = 1;
    private static final int TYPE_MAX_COUNT = 2;

    private Context mContext;
    private List<FunctionBean> mFuncs;

    private LayoutInflater mInflater;

//	public LinkRoomDynamicLayoutAdapter(Context context) {
//	}

    public LinkRoomDynamicLayoutAdapter(Context context, List<FunctionBean> funcs) {
        mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFuncs = funcs;
        // LogUtils.v("LinkRoomDynamicLayoutAdapter size:" + mFuncs.size());
        int needView = mFuncs.size() % FUNCTION_TEMP_COUNT;
        // LogUtils.v("LinkRoomDynamicLayoutAdapter size:" + mFuncs.size()+ "; needView:" + needView);
        if (needView != 0) {
            for (int i = 0; i < FUNCTION_TEMP_COUNT - needView; i++) {
                mFuncs.add(new FunctionBean("transprent", 0, Integer.MAX_VALUE));
            }
        }
    }

    @Override
    public int getCount() {
        return mFuncs.size();
    }

    @Override
    public Object getItem(int position) {
        return mFuncs.get(position < 9 ? (position - 1) : (position - 2));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 || position == 9) ? TYPE_FIXEDVIEW_ITEM
                : TYPE_DRAG_VIEW;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case TYPE_DRAG_VIEW:
                    convertView = mInflater.inflate(R.layout.dragview_item, null);
                    holder.name = (TextView) convertView.findViewById(R.id.tv_func_name);
                    holder.icon = (ImageView) convertView.findViewById(R.id.iv_func_icon);
                    convertView.setTag(holder);
                    break;
                case TYPE_FIXEDVIEW_ITEM:
                    convertView = mInflater.inflate(R.layout.dragview_viewpager_item, null);
                    convertView.setTag(holder);
                    break;
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (type) {
            case TYPE_DRAG_VIEW:
                FunctionBean function = mFuncs.get(position < 9 ? (position - 1)
                        : (position - 2));
                int funcId = function.getFuncId();
                if (funcId > 0) {
                    String funName = mContext
                            .getString(getStringIdByFunFuncId(funcId));
                    if (funName.equals("")) {
                        holder.name.setVisibility(View.GONE);
                    } else {
                        holder.name.setText(funName);
                        function.setName(funName);
                    }
                    holder.icon.setImageResource(getIconResIdByFunFuncId(funcId));
                    ((DynamicItemContainView) convertView).setName(funName);
                } else {
                    ((DynamicItemContainView) convertView).setDragState(false);
                }
                //LogUtils.i("position : " + position + "; funcId:" + funcId);
                break;
            case TYPE_FIXEDVIEW_ITEM:
                ((DynamicItemContainView) convertView).setDragState(false);
                if (position > 1) {
                    convertView.findViewById(R.id.view_top_line).setVisibility(
                            View.VISIBLE);
                    convertView.findViewById(R.id.view_bottom_line).setVisibility(
                            View.VISIBLE);
                    convertView.findViewById(R.id.ll_point).setVisibility(
                            View.VISIBLE);
                } else if (position == 0) {
                    convertView.findViewById(R.id.ll_point).setVisibility(
                            View.VISIBLE);
                }
                break;
        }
        //LogUtils.i("position : " + position);
        return convertView;
    }

    private int getIconResIdByFunFuncId(int funcId) {
        switch (funcId) {
            case BaseApplication.FUNCTION_MESSAGE:
                return R.mipmap.ic_dd_message;
            case BaseApplication.FUNCTION_MONITOR:
                return R.mipmap.ic_dd_monitor;
            case BaseApplication.FUNCTION_APPLYKEY:
                return R.mipmap.ic_dd_applykey;
            case BaseApplication.FUNCTION_SHAPEOPENDOOR:
                return R.mipmap.ic_dd_shapeopendoor;
            case BaseApplication.FUNCTION_REPAIR:
                return R.mipmap.ic_dd_repair;
            case BaseApplication.FUNCTION_VISITORPHOTO:
                return R.mipmap.ic_dd_homesafe;
            case BaseApplication.FUNCTION_OPENDOOR:
                return R.mipmap.ic_dd_visitorrecord;
            case BaseApplication.FUNCTION_PHONE:
                return R.mipmap.ic_dd_phone;
            case BaseApplication.FUNCTION_DD_FUNCTION_FINANCE:
                return R.mipmap.ic_dd_finance;
            case BaseApplication.FUNCTION_DD_FUNCTION_PARKING:
                return R.mipmap.ic_dd_carstop;
        }
        return R.mipmap.ic_dd_more_service;
    }

    private int getStringIdByFunFuncId(int funcId) {
        switch (funcId) {
            case BaseApplication.FUNCTION_MESSAGE:
                return R.string.message;
            case BaseApplication.FUNCTION_MONITOR:
                return R.string.monitor;
            case BaseApplication.FUNCTION_APPLYKEY:
                return R.string.applykey;
            case BaseApplication.FUNCTION_SHAPEOPENDOOR:
                return R.string.shapeopendoor;
            case BaseApplication.FUNCTION_REPAIR:
                return R.string.repair;
            case BaseApplication.FUNCTION_VISITORPHOTO:
                return R.string.visitorphoto;
            case BaseApplication.FUNCTION_OPENDOOR:
                return R.string.opendoor;
            case BaseApplication.FUNCTION_PHONE:
                return R.string.phone;
            case BaseApplication.FUNCTION_DD_FUNCTION_FINANCE:
                return R.string.dd_function_finance;
            case BaseApplication.FUNCTION_DD_FUNCTION_PARKING:
                return R.string.dd_function_parking;
        }
        return R.string.dd_function_more;
    }

    private static class ViewHolder {
        TextView name;
        ImageView icon;
    }
}
