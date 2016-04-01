package com.kai.myzhihu.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kai.myzhihu.R;
import com.kai.myzhihu.bean.BeforeNews;
import com.kai.myzhihu.bean.LatestNews;
import com.kai.myzhihu.bean.Storie;
import com.kai.myzhihu.bean.ThemeContent;
import com.kai.myzhihu.bean.TopStorie;
import com.kai.myzhihu.utils.DateUtil;
import com.kai.myzhihu.widget.MTextSliderView;

import java.util.List;

/**
 * Created by 凯 on 2016/3/25 9:00
 */
public class NewsRcAdapter extends RecyclerView.Adapter<NewsRcAdapter.MViewHolder> {

    private BeforeNews beforeNews;
    private LatestNews latestNews;
    private List<Storie> stories;
    private ThemeContent themeContent;


    private Context context;
    private int RC_TYPE = 0;
    private final int RC_TYPE_HOME = 0;
    private final int RC_TYPE_CATEGORY = 1;
    private final int ITEM_TYPE_HOME_HEAD = 0;
    private final int ITEM_TYPE_CATEGORY_HEAD = 1;
    private final int ITEM_TYPE_CONTENT = 2;


    private LayoutInflater inflater;
    private List<TopStorie> top_stories;
    private boolean sliderLayoutIsComplete = false;

    public NewsRcAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HOME_HEAD) {
            View item_main_home_head_view = inflater.inflate(R.layout.item_main_home_head, parent, false);
            item_main_home_head_view.setTag("item_main_home_head_view");
            return new MViewHolder(item_main_home_head_view);
        } else if (viewType == ITEM_TYPE_CATEGORY_HEAD) {
            View item_main_category_head_view = inflater.inflate(R.layout.item_main_category_head, parent, false);
            item_main_category_head_view.setTag("item_main_category_head_view");
            return new MViewHolder(item_main_category_head_view);
        } else {
            View item_main_content_view = inflater.inflate(R.layout.item_main_content, parent, false);
            item_main_content_view.setTag("item_main_content_view");
            return new MViewHolder(item_main_content_view);
        }
    }


    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_HOME_HEAD) {
            //主页的头item
            bindHomeHead(holder, 0);
            return;
        } else if (getItemViewType(position) == ITEM_TYPE_CATEGORY_HEAD) {
            //分类的头item
            bindCategoryHead(holder, 0);
            return;
        } else {
            //内容item
            bindContent(holder, position - 1);
        }
    }

    /**
     * 绑定分类的头item
     * create at 2016/3/25 12:38 by 凯
     */
    private void bindCategoryHead(MViewHolder holder, int position) {
        if (themeContent != null) {
            holder.theme_background.setImageURI(Uri.parse(themeContent.getBackground()));
            holder.theme_description.setText(themeContent.getDescription());
        }
    }

    /**
     * 绑定主页的头item
     * create at 2016/3/25 12:37 by 凯
     */
    private void bindHomeHead(MViewHolder holder, int position) {
        if (latestNews != null) {
            if (!sliderLayoutIsComplete) {
                holder.sliderLayout.removeAllSliders();
                top_stories = latestNews.getTop_stories();
                int topPosition = 0;
                for (final TopStorie topStorie : top_stories) {
                    MTextSliderView textSliderView = new MTextSliderView(context);
                    textSliderView
                            .description(topStorie.getTitle())
                            .image(topStorie.getImage());
                    holder.sliderLayout.addSlider(textSliderView);
                    sliderLayoutIsComplete = true;
                    holder.sliderLayout.startAutoCycle();
                    final int finalTopPosition = topPosition;
                    textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            // TODO: 2016/3/31 10:39 轮播广告点击事件
                            if (onSliderLayoutItemClickListener != null) {
                                onSliderLayoutItemClickListener.onSliderLayoutItemClick((int) topStorie.getId(), finalTopPosition);
                            }
                        }
                    });
                    topPosition++;
                }
                Log.e("NewsRcAdapter", "bindHomeHead:这个只有home被刷新 或者第一次显示被调用 否则代表有错误 ");
            }

        }

    }

    /**
     * 绑定主页内容
     * create at 2016/3/25 12:35 by 凯
     */
    private void bindContent(final MViewHolder holder, final int position) {
        if (stories != null && stories.size() > 0) {
            holder.tv_item_title.setText(stories.get(position).getTitle());
            List<String> images = stories.get(position).getImages();


            if (images != null && images.size() > 0) {
                holder.rl_item_img.setVisibility(View.VISIBLE);
                holder.dv_item__img.setImageURI(Uri.parse(images.get(0)));

                if (stories.get(position).isMultipic()) {
                    holder.multipic.setVisibility(View.VISIBLE);
                } else {
                    holder.multipic.setVisibility(View.GONE);
                }
            } else {
                holder.rl_item_img.setVisibility(View.GONE);
            }


            if (stories.get(position).isFirst() && RC_TYPE == RC_TYPE_HOME) {
                holder.tv_item_home_date.setVisibility(View.VISIBLE);

                String smartTitle = getSmartTitleFromDate(position);

                holder.tv_item_home_date.setText(smartTitle);
            } else {
                holder.tv_item_home_date.setVisibility(View.GONE);
            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(stories.get(position), position);
                    }
                }
            });
        }
    }

    public String getSmartTitleFromDate(int position) {// TODO: 2016/3/28 9:51 重写该方法  滑动获取当前事件
        if (position < 0) {
            return "首页";
        }
        if (stories != null) {


            String date = stories.get(position).getDate();
            if (date != null) {
                if (date.equals("-1")) {
                    return "今日新闻";
                } else {
                    return DateUtil.nyrToyrx(date);
                }
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (stories != null) {
            return 1 + stories.size();
        }
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0 && RC_TYPE == RC_TYPE_HOME) {
            return ITEM_TYPE_HOME_HEAD;
        } else if (position == 0 && RC_TYPE == RC_TYPE_CATEGORY) {
            return ITEM_TYPE_CATEGORY_HEAD;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_item_img;
        private TextView tv_item_home_date;
        private TextView tv_item_title;
        private SimpleDraweeView dv_item__img;
        private SliderLayout sliderLayout;
        private CardView cardView;
        private ImageView multipic;
        private SimpleDraweeView theme_background;
        private TextView theme_description;
        private LinearLayout theme_ll_edit;
        private TextView theme_tv_edit;

        public MViewHolder(View itemView) {
            super(itemView);
            switch ((String) itemView.getTag()) {
                case "item_main_home_head_view":
                    sliderLayout = (SliderLayout) itemView.findViewById(R.id.sliderLayout);
                    break;
                case "item_main_content_view":
                    multipic = (ImageView) itemView.findViewById(R.id.multipic);
                    cardView = (CardView) itemView.findViewById(R.id.cardView_content);
                    tv_item_home_date = (TextView) itemView.findViewById(R.id.tv_item_date);
                    tv_item_title = (TextView) itemView.findViewById(R.id.tv_item_title);
                    dv_item__img = (SimpleDraweeView) itemView.findViewById(R.id.dv_item_img);
                    rl_item_img = (RelativeLayout) itemView.findViewById(R.id.rl_item_img);
                    break;
                case "item_main_category_head_view":
                    theme_background = (SimpleDraweeView) itemView.findViewById(R.id.theme_background);
                    theme_description = (TextView) itemView.findViewById(R.id.theme_description);
                    theme_ll_edit = (LinearLayout) itemView.findViewById(R.id.theme_ll_edit);
                    theme_tv_edit = (TextView) itemView.findViewById(R.id.theme_tv_edit);
                    break;
            }
        }

    }


    /**
     * 每次设置“之前新闻” 都会把这些内容加在 List<Storie> stories后面 并且把这个当前新闻的第一条新闻设置为isfirst
     *
     * @author 凯
     * create at 2016/3/25 12:23
     */
    public void setBeforeNews(BeforeNews beforeNews) {
        addDatas(beforeNews.getStories(), beforeNews.getDate());
        this.beforeNews = beforeNews;
    }

    public LatestNews getLatestNews() {
        return latestNews;
    }

    /**
     * 每次设置“最新新闻” 都会把List<Storie> stories清空 并且用最新新闻来覆盖他，并且把第一条新闻设置为isfirst
     *
     * @author 凯
     * create at 2016/3/25 12:24
     */
    public void setLatestNews(LatestNews latestNews) {
        RC_TYPE = RC_TYPE_HOME;
        stories = latestNews.getStories();
        stories.get(0).setFirst(true);

        for (Storie storie : stories) {
            storie.setDate("-1");
        }

        sliderLayoutIsComplete = false;
        this.latestNews = latestNews;
    }

    public int getRC_TYPE() {
        return RC_TYPE;
    }

    public void setRC_TYPE(int RC_TYPE) {
        this.RC_TYPE = RC_TYPE;
    }

    public void addDatas(List<Storie> newStories, String date) {
        newStories.get(0).setFirst(true);
        if (date != null) {
            for (Storie newStorie : newStories) {
                newStorie.setDate(date);
            }
        }
        this.stories.addAll(newStories);
    }

    public interface OnItemClickListener {
        public void onItemClick(Storie storie, int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<Storie> getStories() {
        return stories;
    }

    public ThemeContent getThemeContent() {
        return themeContent;
    }

    public void setThemeContent(ThemeContent themeContent) {
        this.themeContent = themeContent;
        RC_TYPE = RC_TYPE_CATEGORY;
        stories = themeContent.getStories();
        notifyDataSetChanged();
//        notifyItemRangeInserted(0,getItemCount());
    }

    public void clearStories() {
        if (stories != null) {
            int size = stories.size() + 1;
            stories = null;
            notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * 获取最后一条新闻的id
     * create at 2016/3/28 8:34 by 凯
     */
    public long getLastNewsId() {
        if (stories != null && stories.size() > 0) {
            return stories.get(stories.size() - 1).getId();
        } else {
            return 0;
        }
    }

    private OnSliderLayoutItemClickListener onSliderLayoutItemClickListener;

    public interface OnSliderLayoutItemClickListener {
        public void onSliderLayoutItemClick(int id, int topPosition);
    }

    public void setOnSliderLayoutItemClickListener(OnSliderLayoutItemClickListener onSliderLayoutItemClickListener) {
        this.onSliderLayoutItemClickListener = onSliderLayoutItemClickListener;
    }


    public List<String> getStorieIds(List<String> ids, int stories_type) {
        if (stories_type == 0) {
            //正常的
            if (stories != null && stories.size() > 0) {
                ids.clear();
                for (Storie storie : stories) {
                    ids.add(String.valueOf(storie.getId()));
                }
            }
        } else {
            //轮播图
            if (top_stories != null && top_stories.size() > 0) {
                ids.clear();
                for (TopStorie topStorie : top_stories) {
                    ids.add(String.valueOf(topStorie.getId()));
                }
            }
        }
        return ids;
    }

}
