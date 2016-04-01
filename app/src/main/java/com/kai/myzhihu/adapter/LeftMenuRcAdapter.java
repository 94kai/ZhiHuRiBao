package com.kai.myzhihu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kai.myzhihu.R;
import com.kai.myzhihu.bean.Theme;
import com.kai.myzhihu.bean.Themes;

import java.util.List;

/**
 * Created by 凯 on 2016/3/27 15:18
 */
public class LeftMenuRcAdapter extends RecyclerView.Adapter<LeftMenuRcAdapter.MViewHolder> {
    private Themes themes;
    private LayoutInflater inflater;
    private int lastSelectPosition=0;
    private int currentSelectThemeId = -1;//首页为-1

    public LeftMenuRcAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MViewHolder(inflater.inflate(R.layout.item_main_left_menu, parent, false));
    }


    @Override
    public void onBindViewHolder(MViewHolder holder, final int position) {
        if (position == 0) {
            holder.item_theme_home.setVisibility(View.VISIBLE);
            holder.item_theme_other.setVisibility(View.GONE);
            if (currentSelectThemeId == -1) {
                holder.item_theme_home.setEnabled(true);
                lastSelectPosition=position;
            } else {
                holder.item_theme_home.setEnabled(false);
            }
        } else {
            holder.item_theme_other.setVisibility(View.VISIBLE);
            holder.item_theme_home.setVisibility(View.GONE);
            holder.item_theme_other.setText(themes.getOthers().get(position - 1).getName());
            if (currentSelectThemeId == themes.getOthers().get(position - 1).getId()) {
                holder.item_theme_other.setEnabled(true);
                lastSelectPosition=position;
            } else {
                holder.item_theme_other.setEnabled(false);
            }
        }

        holder.root_left_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftMenuRcItemClickListener != null) {
                    onLeftMenuRcItemClickListener.onLeftMenuRcItemClick(themes.getOthers(), position);
                }
                if(position>0){
                    currentSelectThemeId = (int) themes.getOthers().get(position - 1).getId();
                }else{
                    currentSelectThemeId=-1;
                }
                notifyItemChanged(position);
                notifyItemChanged(lastSelectPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (themes != null) {
            if (themes.getOthers() != null) {
                return themes.getOthers().size() + 1;
            }
            return 0;
        } else {
            return 0;
        }
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        private TextView item_theme_home;
        private TextView item_theme_other;
        private LinearLayout root_left_menu_item;

        public MViewHolder(View itemView) {
            super(itemView);
            item_theme_home = (TextView) itemView.findViewById(R.id.item_theme_home);
            item_theme_other = (TextView) itemView.findViewById(R.id.item_theme_other);
            root_left_menu_item = (LinearLayout) itemView.findViewById(R.id.root_left_menu_item);
        }
    }


    public void setThemes(Themes themes) {
        this.themes = themes;
        notifyDataSetChanged();
    }

    public int getCurrentSelectThemeId() {
        return currentSelectThemeId;
    }

    public void setCurrentSelectThemeId(int currentSelectThemeId) {
        this.currentSelectThemeId = currentSelectThemeId;
    }

    private OnLeftMenuRcItemClickListener onLeftMenuRcItemClickListener;

    public interface OnLeftMenuRcItemClickListener {
        /**
         * 如果position是0 代表点击的是首页  否则  从others中取 需要position-1
         * create t 2016/3/27 16:50 by 凯
         */
        public void onLeftMenuRcItemClick(List<Theme> others, int position);
    }

    public void setOnLeftMenuRcItemClickListener(OnLeftMenuRcItemClickListener onLeftMenuRcItemClickListener) {
        this.onLeftMenuRcItemClickListener = onLeftMenuRcItemClickListener;
    }
}
