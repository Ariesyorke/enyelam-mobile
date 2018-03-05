package com.nyelam.android.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Event;
import com.nyelam.android.data.NYEventsModule;
import com.nyelam.android.data.NYHotOffersModule;
import com.nyelam.android.data.NYModule;
import com.nyelam.android.data.NYPopularDiveSpotModule;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonic on 10/01/2018.
 */

public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_HEADER = 1;
    public static final int VIEW_TYPE_SLIDER = 5;

    private Context context;
    private List<NYModule> modules;
    private EventsAdapter eventsAdapter;
    private HotOffersAdapter hotOffersAdapter;
    //private HotOffersAdapter hotOffersAdapter;
    //private PopularDiveSpotsAdapter popularDiveSpotsAdapter;
    //private ProductDetailActivity BestSellerAdapter;
    private HomeFragment fragment;

    public HomePageAdapter(Context context, HomeFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    public void addModul(NYModule module) {
        if (modules == null) {
            modules = new ArrayList<>();
        }
        modules.add(module);
    }

    public void addModules(List<NYModule> modules) {
        if (this.modules == null) {
            this.modules = new ArrayList<>();
        }
        this.modules.addAll(modules);

        NYLog.e("HOMEPAGE ADAPTER "+modules.get(0).toString());
    }

    public void clear() {
        if (modules != null)
            modules.clear();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        NYLog.e("HOMEPAGE CEK VIEWTYPE "+viewType);

        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new SectionHeader(context, parent);
            case VIEW_TYPE_SLIDER:
                return new EventsItem(context, parent);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int levelOneIndex = getLevelOneIndex(position);
        NYModule item = modules.get(levelOneIndex);

        if (holder instanceof SectionHeader) {
            ((SectionHeader) holder).tvTitle.setText(item.getModuleName());
        } else if (holder instanceof EventsItem) {
            EventsItem eventsItem = ((EventsItem) holder);
            NYEventsModule eventsModule = (NYEventsModule) item;
            eventsAdapter = new EventsAdapter(context, eventsModule);
            eventsItem.twoWayView.setAdapter(eventsAdapter);
        } else if (holder instanceof HotOffersItem) {
            HotOffersItem hotOffersItem = ((HotOffersItem) holder);
            NYHotOffersModule hotOffersModule = (NYHotOffersModule) item;
            hotOffersAdapter = new HotOffersAdapter(context, hotOffersModule);
            hotOffersItem.twoWayView.setAdapter(eventsAdapter);
        }

    }











    private int getViewType(int position) {
        int index = 0;
        for (int i = 0; i < modules.size(); i++) {
            if (index == position) {
                return VIEW_TYPE_HEADER;
            }
            index++;

            if (modules.get(i) instanceof NYEventsModule) {
                NYEventsModule eventsModule = (NYEventsModule) modules.get(i);
                List<Event> events = eventsModule.getEvents();
                for (int c = 0; c < events.size(); c++) {
                    if (index == position) {
                        return VIEW_TYPE_SLIDER;
                    }
                    index++;
                }
            } else if (modules.get(i) instanceof NYHotOffersModule) {
                NYHotOffersModule hotOffersModule = (NYHotOffersModule) modules.get(i);
                List<DiveService> diveServices = hotOffersModule.getDiveServices();
                for (int c = 0; c < diveServices.size(); c++) {
                    if (index == position) {
                        return VIEW_TYPE_SLIDER;
                    }
                    index++;
                }
            } else if (modules.get(i) instanceof NYPopularDiveSpotModule) {
                NYPopularDiveSpotModule popularDiveSpotModule = (NYPopularDiveSpotModule) modules.get(i);
                List<DiveSpot> diveSpots = popularDiveSpotModule.getDiveSpots();
                for (int c = 0; c < diveSpots.size(); c++) {
                    if (index == position) {
                        return VIEW_TYPE_SLIDER;
                    }
                    index++;
                }
            }
        }
        return index;
    }







    private int getLevelOneIndex(int position) {
        int index = 0;
        for (int i = 0; i < modules.size(); i++) {
            if (index == position) {
                return i;
            }
            index++;

            if (modules.get(i) instanceof NYEventsModule) {
                NYEventsModule eventsModule = (NYEventsModule) modules.get(i);
                List<Event> events = eventsModule.getEvents();
                for (int c = 0; c < events.size(); c++) {
                    if (index == position) {
                        return i;
                    }
                    index++;
                }
            }

        }
        return index;
    }


    @Override
    public int getItemViewType(int position) {
        int viewType = getViewType(position);
        return viewType;
    }


    @Override
    public int getItemCount() {
        int count = 0;
        if (modules != null && !modules.isEmpty()) {
            count += modules.size();
            for (NYModule module : modules) {
                if (module instanceof NYEventsModule) {
                    NYEventsModule eventsModule = (NYEventsModule) module;

                    NYLog.e("HOMEPAGE MODULE 1 "+eventsModule.toString());
                    NYLog.e("HOMEPAGE MODULE 2 "+eventsModule.getEvents().toString());

                    if(module.getModuleType().equals("grid")) {
                        if (eventsModule.getEvents() != null && !eventsModule.getEvents().isEmpty()) {
                            count += eventsModule.getEvents().size();
                        }
                    } else if(module.getModuleType().equals("slider")) {
                        count++;
                    }
                }/* else if (module instanceof OCategoryModule) {
                    OCategoryModule categoryModule = (OCategoryModule) module;
                    if (categoryModule.getCategories() != null && !categoryModule.getCategories().isEmpty()) {
                        count += categoryModule.getCategories().size();
                    }
                }*/

                // TODO: cek ini
            }
        }
        return count;
    }





    public static class SectionHeader extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvSeeAll;

        public SectionHeader(final Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.section_header, parent, false));

            tvTitle = itemView.findViewById(R.id.header_textView);
            tvSeeAll = itemView.findViewById(R.id.see_all_textView);

            tvSeeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NYHelper.handlePopupMessage(context, context.getString(R.string.coming_soon), null);
                }
            });
        }
    }





    public static class EventsItem extends RecyclerView.ViewHolder {
        public TwoWayView twoWayView;

        public EventsItem(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_module_slide, parent, false));

            twoWayView = itemView.findViewById(R.id.two_way_view);
            twoWayView.setOrientation(TwoWayView.Orientation.HORIZONTAL);
        }
    }


    static class EventsAdapter extends BaseAdapter {
        private static final int VIEW_TYPE_ITEM = 0;
        private static final int[] VIEW_TYPES = new int[]{VIEW_TYPE_ITEM};
        private Context context;
        private NYEventsModule events;

        public EventsAdapter(Context context, NYEventsModule events) {
            this.context = context;
            this.events = events;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (events.getEvents() != null && !events.getEvents().isEmpty()) {
                count += events.getEvents().size();
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_ITEM) {
                return events.getEvents().get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            if (events.getModuleName() != null && position < events.getEvents().size()) {
                return VIEW_TYPE_ITEM;
            }
            return IGNORE_ITEM_VIEW_TYPE;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_ITEM){
                return onCreateEventItem(view, (Event) getItem(position));
            }

            return view;
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPES.length;
        }

        private View onCreateEventItem(View view, Event event){
            if (view == null){
                view = new EventsHorizontalGridItemView(context);
            }

            EventsHorizontalGridItemView bestSellerHorizontal = (EventsHorizontalGridItemView) view;
            bestSellerHorizontal.setEvent(event);

            return view;
        }
    }








    public static class HotOffersItem extends RecyclerView.ViewHolder {
        public TwoWayView twoWayView;

        public HotOffersItem(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_module_slide, parent, false));

            twoWayView = itemView.findViewById(R.id.two_way_view);
            twoWayView.setOrientation(TwoWayView.Orientation.HORIZONTAL);
        }
    }


    static class HotOffersAdapter extends BaseAdapter {
        private static final int VIEW_TYPE_ITEM = 0;
        private static final int[] VIEW_TYPES = new int[]{VIEW_TYPE_ITEM};
        private Context context;
        private NYHotOffersModule hotOffers;

        public HotOffersAdapter(Context context, NYHotOffersModule hotOffers) {
            this.context = context;
            this.hotOffers = hotOffers;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (hotOffers.getDiveServices() != null && !hotOffers.getDiveServices().isEmpty()) {
                count += hotOffers.getDiveServices().size();
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_ITEM) {
                return hotOffers.getDiveServices().get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            if (hotOffers.getModuleName() != null && position < hotOffers.getDiveServices().size()) {
                return VIEW_TYPE_ITEM;
            }
            return IGNORE_ITEM_VIEW_TYPE;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_ITEM){
                return onCreateHotOfferItem(view, (DiveService) getItem(position));
            }

            return view;
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPES.length;
        }

        private View onCreateHotOfferItem(View view, DiveService diveService){
            if (view == null){
                view = new HotOffersHorizontalGridItemView(context);
            }

            HotOffersHorizontalGridItemView hotOffersHorizontalGridItemView = (HotOffersHorizontalGridItemView) view;
            hotOffersHorizontalGridItemView.setDiveService(diveService);

            return view;
        }
    }











    /*public static class PopularItem extends RecyclerView.ViewHolder {
        public TwoWayView twoWayView;

        public HotOffersItem(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_module_slide, parent, false));

            twoWayView = itemView.findViewById(R.id.two_way_view);
            twoWayView.setOrientation(TwoWayView.Orientation.HORIZONTAL);
        }
    }


    static class HotOffersAdapter extends BaseAdapter {
        private static final int VIEW_TYPE_ITEM = 0;
        private static final int[] VIEW_TYPES = new int[]{VIEW_TYPE_ITEM};
        private Context context;
        private NYHotOffersModule hotOffers;

        public HotOffersAdapter(Context context, NYHotOffersModule hotOffers) {
            this.context = context;
            this.hotOffers = hotOffers;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (hotOffers.getDiveServices() != null && !hotOffers.getDiveServices().isEmpty()) {
                count += hotOffers.getDiveServices().size();
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_ITEM) {
                return hotOffers.getDiveServices().get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            if (hotOffers.getModuleName() != null && position < hotOffers.getDiveServices().size()) {
                return VIEW_TYPE_ITEM;
            }
            return IGNORE_ITEM_VIEW_TYPE;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_ITEM){
                return onCreateHotOfferItem(view, (DiveService) getItem(position));
            }

            return view;
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPES.length;
        }

        private View onCreateHotOfferItem(View view, DiveService diveService){
            if (view == null){
                view = new HotOffersHorizontalGridItemView(context);
            }

            HotOffersHorizontalGridItemView hotOffersHorizontalGridItemView = (HotOffersHorizontalGridItemView) view;
            hotOffersHorizontalGridItemView.setDiveService(diveService);

            return view;
        }
    }*/



}
