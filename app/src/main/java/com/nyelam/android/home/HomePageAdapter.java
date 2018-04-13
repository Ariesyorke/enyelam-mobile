package com.nyelam.android.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Event;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.Module;
import com.nyelam.android.data.ModuleDiveSpot;
import com.nyelam.android.data.ModuleEvent;
import com.nyelam.android.data.ModuleService;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.dotrip.DoTripActivity;
import com.nyelam.android.dotrip.DoTripDetailActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.NYStrikethroughTextView;

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

    private Activity context;
    private List<Module> modules;
    private HomeFragment fragment;

    public HomePageAdapter(Activity context, HomeFragment fragment) {
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
            ModuleEvent moduleEvent = (ModuleEvent)item;
            ((EventsItemViewHolder) holder).setModel(moduleEvent.getEvents());
        } else if (holder instanceof HotOffersItemViewHolder) {
            HotOffersItemViewHolder hotOffersItem = ((HotOffersItemViewHolder) holder);
            ModuleService moduleService = (ModuleService) item;
            if (moduleService != null)hotOffersItem.setModel(moduleService.getDiveServices());
        } else if (holder instanceof PopularItemViewHolder) {
            PopularItemViewHolder popularViewHolder = (PopularItemViewHolder) holder;
            ModuleDiveSpot moduleDiveSpot = (ModuleDiveSpot)item;
            popularViewHolder.setModel(moduleDiveSpot.getDiveSpots());
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
                if (index == position) {
                    return VIEW_TYPE_EVENT;
                }
                index++;
            } else if (modules.get(i) instanceof ModuleService) {
                if (index == position) {
                    return VIEW_TYPE_HOT_OFFER;
                }
                index++;

            } else if (modules.get(i) instanceof ModuleDiveSpot) {
                if (index == position) {
                    return View_TYPE_DIVE_SPOT;
                }
                index++;
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
                if (index == position) {
                    return i;
                }
                index++;
            } else if (modules.get(i) instanceof ModuleService) {
                if (index == position) {
                    return i;
                }
                index++;
            } else if (modules.get(i) instanceof ModuleDiveSpot) {
                if (index == position) {
                    return i;
                }
                index++;
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
            count += (modules.size() * 2);
        }
        return count;
    }

    public static class SectionHeader extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvSeeAll;

        public SectionHeader(final Context context, final ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.section_header, parent, false));

            tvTitle = itemView.findViewById(R.id.header_textView);
            tvSeeAll = itemView.findViewById(R.id.see_all_textView);

            tvSeeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //NYHelper.handlePopupMessage(context, context.getString(R.string.coming_soon)+" "+String.valueOf(parent.getClass().getName()), null);
                    Intent intent = new Intent(context, DoTripActivity.class);
                    context.startActivity(intent);
                }
            });

        }
    }

    public static class EventsItemViewHolder extends RecyclerView.ViewHolder {
        public TwoWayView twoWayView;
        Context context;
        EventAdapter adapter;
        public EventsItemViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_module_slide, parent, false));

            twoWayView = itemView.findViewById(R.id.two_way_view);

            twoWayView.setOrientation(TwoWayView.Orientation.HORIZONTAL);
            this.context = context;
            adapter = new EventAdapter();
            twoWayView.setAdapter(adapter);
        }

        public void setModel(List<Event> events) {
            adapter.clear();
            adapter.addEvents(events);
            adapter.notifyDataSetChanged();
            twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    NYHelper.handlePopupMessage(context, context.getString(R.string.coming_soon), null);
                }
            });
        }

        public class EventAdapter extends BaseAdapter {
            private List<Event> events;
            public void addEvent(Event event) {
                if(this.events == null) {
                    this.events = new ArrayList<>();
                }
                this.events.add(event);
            }
            public void addEvents(List<Event> events) {
                if(this.events == null) {
                    this.events = new ArrayList<>();
                }
                this.events.addAll(events);
            }

            public void clear() {
                this.events = new ArrayList<>();
            }
            @Override
            public int getCount() {
                int count = 0;
                if(events != null && !events.isEmpty()) {
                    count += events.size();
                }
                return count;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if(view == null) {
                    view = View.inflate(context, R.layout.view_item_modul_event, null);
                }
                final ImageView imageView = (ImageView)view.findViewById(R.id.event_imageView);
                TextView locationTextView = (TextView)view.findViewById(R.id.location_textView);
                TextView nameTextView = (TextView)view.findViewById(R.id.name_textView);
                TextView priceTextView = (TextView)view.findViewById(R.id.price_textView);
                Event event = events.get(i);
                if (event != null) {

                    if (NYHelper.isStringNotEmpty(event.getName()))
                        nameTextView.setText(event.getName());

                    if (event.getLocation() != null) {

                        Location location = event.getLocation();
                        String locString = "";

                        if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName()))
                            locString += location.getCity().getName();
                        if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName()))
                            locString += ", " + location.getProvince().getName();

                        locationTextView.setText(locString);
                    }


                    double normalPrice = Double.valueOf(event.getNormalPrice());
                    double specialPrice = Double.valueOf(event.getSpecialPrice());

                    if (specialPrice < normalPrice && specialPrice > 0) {
                        priceTextView.setText(NYHelper.priceFormatter(specialPrice));
                        //priceStrikethroughTextView.setText(NYHelper.priceFormatter(normalPrice));
                        //priceStrikethroughTextView.setVisibility(View.VISIBLE);
                    } else {
                        priceTextView.setText(NYHelper.priceFormatter(normalPrice));
                        //priceStrikethroughTextView.setVisibility(View.GONE);
                    }

                    //SET IMAGE
                    if (NYHelper.isStringNotEmpty(event.getFeaturedImage())) {
                        ImageLoader.getInstance().loadImage(event.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                imageView.setImageResource(R.drawable.example_pic);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                imageView.setImageBitmap(loadedImage);
                                //activity.getCache().put(imageUri, loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                imageView.setImageResource(R.drawable.example_pic);
                            }
                        });

                        ImageLoader.getInstance().displayImage(event.getFeaturedImage(), imageView, NYHelper.getOption());

                    } else {
                        imageView.setImageResource(R.drawable.example_pic);
                    }
                }
                return view;
            }
        }


    }

    public static class HotOffersItemViewHolder extends RecyclerView.ViewHolder {
        public TwoWayView twoWayView;
        Activity context;
        HotOffersAdapter adapter;

        public HotOffersItemViewHolder(Activity context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_module_slide, parent, false));

            twoWayView = itemView.findViewById(R.id.two_way_view);
            twoWayView.setItemMargin(20);
            twoWayView.setOrientation(TwoWayView.Orientation.HORIZONTAL);
            this.context = context;
            adapter = new HotOffersAdapter();
            twoWayView.setAdapter(adapter);
        }

        public void setModel(final List<DiveService> diveServices) {
            adapter.clear();
            //adapter.
            adapter.addDiveServices(context, diveServices);
            adapter.notifyDataSetChanged();
            twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DiveService diveService = diveServices.get(i);
                    /*SearchService searchService = new SearchService();
                    searchService.setName(diveService.getName());
                    searchService.setId(diveService.getId());
                    searchService.setLicense(diveService.isLicense());
                    searchService.setType(4);*/

                    Intent intent = new Intent(context, DoTripDetailActivity.class);
                    intent.putExtra(NYHelper.SERVICE, diveService.toString());
                    context.startActivity(intent);
                }
            });
        }

        public class HotOffersAdapter extends BaseAdapter {
            private Activity activity;
            private List<DiveService> diveServices;

            public void addDiveService(DiveService diveService) {
                if (this.diveServices == null) {
                    this.diveServices = new ArrayList<>();
                }
                this.diveServices.add(diveService);
            }

            public void addDiveServices(Activity activity, List<DiveService> diveServices) {
                this.activity = activity;
                if (this.diveServices == null) {
                    this.diveServices = new ArrayList<>();
                }
                this.diveServices.addAll(diveServices);
            }

            public void clear() {
                if (diveServices != null) {
                    diveServices.clear();
                }
            }

            @Override
            public int getCount() {
                int count = 0;
                if (diveServices != null && !diveServices.isEmpty()) {
                    count += diveServices.size();
                }
                return count;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = View.inflate(context, R.layout.view_item_modul_do_trip, null);
                }

                DiveService diveService = diveServices.get(i);
                CardView cardView = (CardView) view.findViewById(R.id.cardView);
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
                final ImageView featuredImageView = (ImageView) view.findViewById(R.id.featured_imageView);
                TextView serviceNameTextView = (TextView) view.findViewById(R.id.service_name_textView);
                NYStrikethroughTextView priceStrikethroughTextView = (NYStrikethroughTextView) itemView.findViewById(R.id.price_strikethrough_textView);
                TextView priceTextView = (TextView) view.findViewById(R.id.price_textView);
                TextView scheduleTextView = (TextView) view.findViewById(R.id.shedule_textView);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;

                /*CardView.LayoutParams layoutParams = (CardView.LayoutParams)
                        cardView.getLayoutParams();
                layoutParams.width = width*3/4;*/

                TwoWayView.LayoutParams param = new TwoWayView.LayoutParams(
                   width*3/4,
                   ViewGroup.LayoutParams.WRAP_CONTENT
                );
                cardView.setLayoutParams(param);


                /*FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(
                        width*3/4,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                param.setMargins(0,0,NYHelper.integerToDP(context, 10),0);
                linearLayout.setLayoutParams(param);*/


                /*FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(
                   *//*width*//* width*3/4,
                   *//*height*//* FrameLayout.LayoutParams.WRAP_CONTENT
                );
                param.setMargins(0,0,NYHelper.integerToDP(context, 10),0);
                cardView.setLayoutParams(param);*/


                if (diveService != null) {

                    if (NYHelper.isStringNotEmpty(diveService.getName()))
                        serviceNameTextView.setText(diveService.getName());

                    if (diveService.getDiveSpots().get(0) != null && diveService.getDiveSpots().get(0).getLocation() != null) {

                        Location location = diveService.getDiveSpots().get(0).getLocation();
                        String locString = "";
                        if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName()))
                            locString += location.getCity().getName();
                        if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName()))
                            locString += ", " + location.getProvince().getName();
                        //if (NYHelper.isStringNotEmpty(location.getCountry())) locString += ", "+location.getCountry();
                    }

                    if (diveService.getSchedule() != null) {
                        scheduleTextView.setText(NYHelper.setMillisToDate(diveService.getSchedule().getStartDate()) + " - " + NYHelper.setMillisToDate(diveService.getSchedule().getStartDate()));
                    }

                    double normalPrice = Double.valueOf(diveService.getNormalPrice());
                    double specialPrice = Double.valueOf(diveService.getSpecialPrice());

                    if (specialPrice < normalPrice && specialPrice > 0) {
                        priceTextView.setText(NYHelper.priceFormatter(specialPrice));
                        //priceStrikethroughTextView.setText(NYHelper.priceFormatter(normalPrice));
                        //priceStrikethroughTextView.setVisibility(View.VISIBLE);
                    } else {
                        priceTextView.setText(NYHelper.priceFormatter(normalPrice));
                        //priceStrikethroughTextView.setVisibility(View.GONE);
                    }

                    //SET IMAGE
                    if (NYHelper.isStringNotEmpty(diveService.getFeaturedImage())) {
                        ImageLoader.getInstance().loadImage(diveService.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                featuredImageView.setImageResource(R.drawable.example_pic);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                featuredImageView.setImageBitmap(loadedImage);
                                //activity.getCache().put(imageUri, loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                featuredImageView.setImageResource(R.drawable.example_pic);
                            }
                        });

                        ImageLoader.getInstance().displayImage(diveService.getFeaturedImage(), featuredImageView, NYHelper.getOption());

                    } else {
                        featuredImageView.setImageResource(R.drawable.example_pic);
                    }

                }
                return view;
            }
        }
    }


    public static class PopularItemViewHolder extends RecyclerView.ViewHolder {
        public TwoWayView twoWayView;
        Context context;
        DiveSpotAdapter adapter;

        public PopularItemViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_module_slide, parent, false));

            twoWayView = itemView.findViewById(R.id.two_way_view);
            twoWayView.setOrientation(TwoWayView.Orientation.HORIZONTAL);
            this.context = context;
            adapter = new DiveSpotAdapter();
            twoWayView.setAdapter(adapter);
        }

        public void setModel(final List<DiveSpot> diveSpots) {
            adapter.clear();
            adapter.addDiveSpots(diveSpots);
            adapter.notifyDataSetChanged();
            twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DiveSpot diveSpot = diveSpots.get(i);
                    SearchService searchService = new SearchService();
                    searchService.setName(diveSpot.getName());
                    searchService.setId(diveSpot.getId());
                    searchService.setLicense(false);
                    searchService.setType(1);

                    Intent intent = new Intent(context, DoDiveActivity.class);
                    intent.putExtra(NYHelper.SEARCH_RESULT, searchService.toString());
                    context.startActivity(intent);
                }
            });
        }

        public class DiveSpotAdapter extends BaseAdapter {
            private List<DiveSpot> diveSpots;

            public void addDiveSpot(DiveSpot diveSpot) {
                if(this.diveSpots == null) {
                    this.diveSpots = new ArrayList<>();
                }
                this.diveSpots.add(diveSpot);
            }
            public void addDiveSpots(List<DiveSpot> diveSpots) {
                if(this.diveSpots == null) {
                    this.diveSpots = new ArrayList<>();
                }
                this.diveSpots.addAll(diveSpots);
            }

            public void clear() {
                this.diveSpots = new ArrayList<>();
            }

            @Override
            public int getCount() {
                int count = 0;
                if(diveSpots != null && !diveSpots.isEmpty()) {
                    count += diveSpots.size();
                }
                return count;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view ==  null) {
                    view = View.inflate(context, R.layout.view_item_modul_popular_dive_spot, null);
                }
                final ImageView diveSpotImageView = (ImageView) view.findViewById(R.id.dive_spot_imageView);
                TextView diveSpotNameTextView = (TextView) view.findViewById(R.id.dive_spot_name_textView);
                LinearLayout containerLinaerLayout = (LinearLayout) view.findViewById(R.id.container_linaerLayout);
                DiveSpot diveSpot = diveSpots.get(i);
                if (diveSpot != null){

                    List<int[]> drColor = new ArrayList<>();
                    drColor.add(new int[] {0xFFAFD201,0xFF1E4001});
                    drColor.add(new int[] {0xFF0000E7,0xFF000050});
                    drColor.add(new int[] {0xFFB10000,0xFF1E0000});
                    drColor.add(new int[] {0xFF00CB00,0xFF013A01});
                    drColor.add(new int[] {0xFFA900E0,0xFF1C0052});
                    drColor.add(new int[] {0xFFFE0388,0xFFF05825});

                    GradientDrawable gd = new GradientDrawable(
                            GradientDrawable.Orientation.TOP_BOTTOM,
                            drColor.get(i%5));

                    gd.setCornerRadius(0f);
                    gd.setAlpha(180);

                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        containerLinaerLayout.setBackgroundDrawable(gd);
                    } else {
                        containerLinaerLayout.setBackground(gd);
                    }


                    if (NYHelper.isStringNotEmpty(diveSpot.getName())) diveSpotNameTextView.setText(diveSpot.getName());

                    //SET IMAGE
                    if (NYHelper.isStringNotEmpty(diveSpot.getFeaturedImage())) {
                        ImageLoader.getInstance().loadImage(diveSpot.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                diveSpotImageView.setImageResource(R.drawable.example_pic);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                diveSpotImageView.setImageBitmap(loadedImage);
                                //activity.getCache().put(imageUri, loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                diveSpotImageView.setImageResource(R.drawable.example_pic);
                            }
                        });

                        ImageLoader.getInstance().displayImage(diveSpot.getFeaturedImage(), diveSpotImageView, NYHelper.getOption());

                    } else {
                        diveSpotImageView.setImageResource(R.drawable.example_pic);
                    }


                }
                return view;

            }
        }
    }


}
