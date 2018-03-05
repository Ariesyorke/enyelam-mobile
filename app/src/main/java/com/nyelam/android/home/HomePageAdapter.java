package com.nyelam.android.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Event;
import com.nyelam.android.data.Module;
import com.nyelam.android.data.ModuleDiveSpot;
import com.nyelam.android.data.ModuleEvent;
import com.nyelam.android.data.ModuleService;
import com.nyelam.android.data.NYEventsModule;
import com.nyelam.android.data.NYHotOffersModule;
import com.nyelam.android.helper.NYHelper;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonic on 10/01/2018.
 */

public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_HEADER = 1;
    public static final int VIEW_TYPE_EVENT = 2;
    public static final int VIEW_TYPE_HOT_OFFER = 3;
    public static final int View_TYPE_DIVE_SPOT = 4;

    private Context context;
    private List<Module> modules;
    private HomeFragment fragment;

    public HomePageAdapter(Context context, HomeFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    public void addModul(Module module) {
        if (modules == null) {
            modules = new ArrayList<>();
        }
        modules.add(module);
    }

    public void addModules(List<Module> modules) {
        if (this.modules == null) {
            this.modules = new ArrayList<>();
        }
        this.modules.addAll(modules);

    }

    public void clear() {
        if (modules != null)
            modules.clear();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new SectionHeader(context, parent);
            case View_TYPE_DIVE_SPOT:
                return new PopularItemViewHolder(context, parent);
            case VIEW_TYPE_HOT_OFFER:
                return new HotOffersItemViewHolder(context, parent);
            case VIEW_TYPE_EVENT:
                return new EventsItemViewHolder(context, parent);
        }
//
//        switch (viewType) {
//            case VIEW_TYPE_HEADER:
//                return new SectionHeader(context, parent);
//            case VIEW_TYPE_SLIDER:
//                return new EventsItemViewHolder(context, parent);
//        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int levelOneIndex = getLevelOneIndex(position);
        Module item = modules.get(levelOneIndex);

        if (holder instanceof SectionHeader) {
            ((SectionHeader) holder).tvTitle.setText(item.getName());
        } else if (holder instanceof EventsItemViewHolder) {
            EventsItemViewHolder eventsItem = ((EventsItemViewHolder) holder);

        } else if (holder instanceof HotOffersItemViewHolder) {
            HotOffersItemViewHolder hotOffersItem = ((HotOffersItemViewHolder) holder);
        } else if (holder instanceof PopularItemViewHolder) {
            PopularItemViewHolder popularViewHolder = (PopularItemViewHolder)holder;
        }
    }

    private int getViewType(int position) {
        int index = 0;
        for (int i = 0; i < modules.size(); i++) {
            if (index == position) {
                return VIEW_TYPE_HEADER;
            }
            index++;

            if (modules.get(i) instanceof ModuleEvent) {
                ModuleEvent eventsModule = (ModuleEvent) modules.get(i);
                List<Event> events = eventsModule.getEvents();
                for (int c = 0; c < events.size(); c++) {
                    if (index == position) {
                        return VIEW_TYPE_EVENT;
                    }
                    index++;
                }
            } else if (modules.get(i) instanceof ModuleService) {
                ModuleService hotOffersModule = (ModuleService) modules.get(i);
                List<DiveService> diveServices = hotOffersModule.getDiveServices();
                for (int c = 0; c < diveServices.size(); c++) {
                    if (index == position) {
                        return VIEW_TYPE_HOT_OFFER;
                    }
                    index++;
                }
            } else if (modules.get(i) instanceof ModuleDiveSpot) {
                ModuleDiveSpot popularDiveSpotModule = (ModuleDiveSpot) modules.get(i);
                List<DiveSpot> diveSpots = popularDiveSpotModule.getDiveSpots();
                for (int c = 0; c < diveSpots.size(); c++) {
                    if (index == position) {
                        return View_TYPE_DIVE_SPOT;
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

            if (modules.get(i) instanceof ModuleEvent) {
                ModuleEvent eventsModule = (ModuleEvent) modules.get(i);
                List<Event> events = eventsModule.getEvents();
                for (int c = 0; c < events.size(); c++) {
                    if (index == position) {
                        return i;
                    }
                    index++;
                }
            } else if (modules.get(i) instanceof ModuleService) {
                ModuleService moduleService = (ModuleService)modules.get(i);
                List<DiveService> services = moduleService.getDiveServices();
                for(int c = 0; c < services.size(); c++) {
                    if(index == position) {
                        return i;
                    }
                    index++;
                }
            } else if (modules.get(i) instanceof ModuleDiveSpot) {
                ModuleDiveSpot moduleDiveSpot = (ModuleDiveSpot)modules.get(i);
                List<DiveSpot> diveSpots = moduleDiveSpot.getDiveSpots();
                for(int c = 0; c < diveSpots.size(); c++) {
                    if(index == position) {
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
            for(Module m : modules) {
                if (m instanceof ModuleService) {
                    ModuleService s = (ModuleService)m;
                    if(s.getDiveServices() != null) {
                        count += s.getDiveServices().size();
                    }
                } else if (m instanceof ModuleDiveSpot) {
                    ModuleDiveSpot d = (ModuleDiveSpot)m;
                    if(d.getDiveSpots() != null) {
                        count += d.getDiveSpots().size();
                    }
                } else if (m instanceof  ModuleEvent) {
                    ModuleEvent e = (ModuleEvent)m;
                    if(e.getEvents() != null) {
                        count += e.getEvents().size();
                    }
                }
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

    public static class EventsItemViewHolder extends RecyclerView.ViewHolder {
        public TwoWayView twoWayView;

        public EventsItemViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_module_slide, parent, false));

            twoWayView = itemView.findViewById(R.id.two_way_view);
            twoWayView.setOrientation(TwoWayView.Orientation.HORIZONTAL);
        }

    }

    public static class HotOffersItemViewHolder extends RecyclerView.ViewHolder {
        public TwoWayView twoWayView;

        public HotOffersItemViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_module_slide, parent, false));

            twoWayView = itemView.findViewById(R.id.two_way_view);
            twoWayView.setOrientation(TwoWayView.Orientation.HORIZONTAL);
        }
    }


    public static class PopularItemViewHolder extends RecyclerView.ViewHolder {
        public TwoWayView twoWayView;

        public PopularItemViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_module_slide, parent, false));

            twoWayView = itemView.findViewById(R.id.two_way_view);
            twoWayView.setOrientation(TwoWayView.Orientation.HORIZONTAL);
        }
    }




}
