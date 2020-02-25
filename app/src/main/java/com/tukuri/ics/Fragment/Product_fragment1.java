package com.tukuri.ics.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tukuri.ics.Adapter.Home_adapter;
import com.tukuri.ics.Adapter.Product_adapter;
import com.tukuri.ics.AppController;
import com.tukuri.ics.Config.BaseURL;
import com.tukuri.ics.ConnectivityReceiver;
import com.tukuri.ics.CustomVolleyJsonRequest;
import com.tukuri.ics.MainActivity;
import com.tukuri.ics.Model.Category_model;
import com.tukuri.ics.Model.Product_model;
import com.tukuri.ics.R;
import com.tukuri.ics.RecyclerTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product_fragment1 extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {
    private static String TAG = Product_fragment1.class.getSimpleName();

    private RecyclerView rv_cat;
    private RecyclerView tab_cat;
    ImageView img1;
    private Home_adapter adapter;
    HashMap<String, String> map = new HashMap<>();
    private List<Category_model> modelList;
    public HashMap<String, Integer> sliderImages;
    private ViewPager imageSlider;



    private List<Category_model> category_modelList = new ArrayList<>();
    private List<String> cat_menu_id = new ArrayList<>();

    private List<Product_model> product_modelList = new ArrayList<>();
    private Product_adapter adapter_product;

    //private SliderLayout imgSlider;


    public Product_fragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product1, container, false);
        imageSlider = (ViewPager) view.findViewById(R.id.view_pager);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(getActivity());
        viewPager.setAdapter(adapter);
//
//        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        imageSlider.setCustomAnimation(new DescriptionAnimation());
//        imageSlider.setDuration(10000);

        sliderImages = new HashMap<>();
        //  SliaderImage();

        tab_cat = (RecyclerView) view.findViewById(R.id.tab_cat);
        rv_cat = (RecyclerView) view.findViewById(R.id.rv_subcategory);
        img1 = (ImageView) view.findViewById(R.id.img1);
        rv_cat.setLayoutManager(new GridLayoutManager(getActivity(),3));

        String getcat_id = getArguments().getString("cat_id");
        String getcat_title = getArguments().getString("cat_title");
        String getcat_image = getArguments().getString("cat_image");

        ((MainActivity) getActivity()).setTitle(getcat_title);
        ((MainActivity) getActivity()).BackIC();

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            tab_cat.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        }
        else{
            tab_cat.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetCategoryRequest(getcat_id);
            // makeGetSliderRequest();

        }


        tab_cat.setVisibility(View.GONE);


             tab_cat.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), tab_cat,
                new RecyclerTouchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String getcat_id = cat_menu_id.get(position);

                    if (ConnectivityReceiver.isConnected()) {
                       // makeGetProductRequest(getcat_id);
                        String getid = category_modelList.get(position).getId();
                        String getcat_title = category_modelList.get(position).getTitle();
                        String getcat_image = category_modelList.get(position).getImage();

                        Bundle args = new Bundle();
                        Fragment fm = new Product_fragment();
                        args.putString("cat_id", getid);
                        args.putString("cat_title", getcat_title);
                        args.putString("cat_image", getcat_image);
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                .addToBackStack(null).commit();
                    }

                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));





        Glide.with(getActivity())
                .load(BaseURL.IMG_CATEGORY_URL + getcat_image)
                .override(90,90)
                .placeholder(R.mipmap.ic_applogo)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(img1);

        return view;

    }
//    private void makeGetProductRequest(String cat_id) {
//
//        // Tag used to cancel the request
//        String tag_json_obj = "json_product_req";
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("cat_id", cat_id);
//
//
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//                try {
//                    Boolean status = response.getBoolean("responce");
//                    if (status) {
//
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<Product_model>>() {
//                        }.getType();
//
//                        product_modelList = gson.fromJson(response.getString("data"), listType);
//
//                        adapter_product = new Product_adapter(product_modelList, getActivity());
//                        rv_cat.setAdapter(adapter_product);
//                        adapter_product.notifyDataSetChanged();
//
//                        if (getActivity() != null) {
//                            if (product_modelList.isEmpty()) {
//                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Adding request to request queue
//        com.tukuri.ics.AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }

    private void makeGetCategoryRequest(final String parent_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", parent_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Category_model>>() {
                        }.getType();

                        category_modelList = gson.fromJson(response.getString("data"), listType);
                        adapter = new Home_adapter(category_modelList);
                        tab_cat.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (!category_modelList.isEmpty()) {
                            tab_cat.setVisibility(View.VISIBLE);

                            cat_menu_id.clear();
                            for (int i = 0; i < category_modelList.size(); i++) {
                                cat_menu_id.add(category_modelList.get(i).getId());
                            }
                        } else {
                            //makeGetProductRequest(parent_id);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    public class ImageAdapter extends PagerAdapter {
        Context context;
        private int[] GalImages = new int[] {
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,

        };
        ImageAdapter(Context context)
        {
            this.context=context;
        }
        @Override
        public int getCount() {
            return GalImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(GalImages[position]);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(true);
        MenuItem check = menu.findItem(R.id.action_change_password);
        check.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                Fragment fm = new Search_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
                return false;
        }
        return false;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
