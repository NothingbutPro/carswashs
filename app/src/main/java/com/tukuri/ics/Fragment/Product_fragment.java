package com.tukuri.ics.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tukuri.ics.Adapter.ImageAdapter;
import com.tukuri.ics.Adapter.Product_adapter;
import com.tukuri.ics.Config.BaseURL;
import com.tukuri.ics.Model.Category_model;
import com.tukuri.ics.Model.Product_model;
import com.tukuri.ics.R;
import com.tukuri.ics.ConnectivityReceiver;
import com.tukuri.ics.CustomVolleyJsonRequest;
import com.tukuri.ics.AppController;
import com.tukuri.ics.MainActivity;


public class Product_fragment extends Fragment  implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private static String TAG = Product_fragment.class.getSimpleName();

    private RecyclerView rv_cat;
    private TabLayout tab_cat;
    ImageView img1;
    HashMap<String, String> map = new HashMap<>();
    private List<Category_model> modelList;
    public HashMap<String, Integer> sliderImages;
    private ViewPager imageSlider;



    private List<Category_model> category_modelList = new ArrayList<>();
    private List<String> cat_menu_id = new ArrayList<>();

    private List<Product_model> product_modelList = new ArrayList<>();
    private Product_adapter adapter_product;

   //private SliderLayout imgSlider;


    public Product_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        setHasOptionsMenu(true);


//        imageSlider = (SliderLayout) view.findViewById(R.id.slider);
          imageSlider = (ViewPager) view.findViewById(R.id.view_pager);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(getContext());
        viewPager.setAdapter(adapter);
//
//        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        imageSlider.setCustomAnimation(new DescriptionAnimation());
//        imageSlider.setDuration(10000);

        sliderImages = new HashMap<>();
      //  SliaderImage();
        
        tab_cat = (TabLayout) view.findViewById(R.id.tab_cat);
        rv_cat = (RecyclerView) view.findViewById(R.id.rv_subcategory);
        img1 = (ImageView) view.findViewById(R.id.img1);
        rv_cat.setLayoutManager(new GridLayoutManager(getActivity(),3));

     String getcat_id = getArguments().getString("cat_id");
       String getcat_title = getArguments().getString("cat_title");
       String getcat_image = getArguments().getString("cat_image");

        ((MainActivity) getActivity()).setTitle(getcat_title);
        ((MainActivity) getActivity()).BackIC();

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetCategoryRequest(getcat_id);
           // makeGetSliderRequest();

        }


        tab_cat.setVisibility(View.GONE);
        tab_cat.setSelectedTabIndicatorColor(getActivity().getResources().getColor(R.color.white));

        tab_cat.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String getcat_id = cat_menu_id.get(tab.getPosition());

                if (ConnectivityReceiver.isConnected()) {
                    makeGetProductRequest(getcat_id);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                /*String getcat_id = cat_menu_id.get(tab.getPosition());
                tab_cat.setSelectedTabIndicatorColor(getActivity().getResources().getColor(R.color.white));

                if (ConnectivityReceiver.isConnected()) {
                    makeGetProductRequest(getcat_id);
                }*/
            }
        });



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

//    private void SliaderImage() {
//        HashMap<String,String> url_maps = new HashMap<String, String>();
//        url_maps.put("image1", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSBercadwbd9WfbG4XR3N7axtS6qr5Ne0hmDb_56ux-c5ZXlUXf");
//        url_maps.put("image2", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcR-GPHBtluDUTqrzamjFhJx90fEkl8JPESqa7869HmKESFYVqyh");
//        url_maps.put("image3", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSbMZNBmHIKBO0UIni1eMv6jy44QvrWGU9gVQpa7sY2ohJDIKWp");
//
//
//        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
//        file_maps.put("image1",R.drawable.img1);
//        file_maps.put("image2",R.drawable.img2);
//        file_maps.put("image3",R.drawable.img3);
//
//        for(String name : url_maps.keySet()){
//            TextSliderView textSliderView = new TextSliderView(getActivity());
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(url_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);
//
//            imageSlider.addSlider(textSliderView);
//        }
//    }

//    private void makeGetSliderRequest()
//    {
//        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_SLIDER_URL2,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//
//                        try {
//                            // Parsing json array response
//                            // loop through each json object
//
//                            // arraylist list variable for store data;
//                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
//
//                            for (int i = 0; i < response.length(); i++) {
//
//                                JSONObject jsonObject = (JSONObject) response
//                                        .get(i);
//
//                                HashMap<String, String> url_maps = new HashMap<String, String>();
//                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
//                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL2 + jsonObject.getString("slider_image"));
//
//                                listarray.add(url_maps);
//                            }
//
//                            for (HashMap<String, String> name : listarray) {
//                                TextSliderView textSliderView = new TextSliderView(getActivity());
//                                // initialize a SliderLayout
//                                textSliderView
//                                        .description(name.get("slider_title"))
//                                        .image(name.get("slider_image"))
//                                        .setScaleType(BaseSliderView.ScaleType.Fit);
//
//                                //add your extra information
//                                textSliderView.bundle(new Bundle());
//                                textSliderView.getBundle()
//                                        .putString("extra", name.get("slider_title"));
//
//                                imgSlider.addSlider(textSliderView);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getActivity(),
//                                    "Error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
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
//        AppController.getInstance().addToRequestQueue(req);
//
//    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetProductRequest(String cat_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_product_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();

                        product_modelList = gson.fromJson(response.getString("data"), listType);

                        adapter_product = new Product_adapter(product_modelList, getActivity());
                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();

                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
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

    /**
     * Method to make json object request where json response starts wtih
     */
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

                        if (!category_modelList.isEmpty()) {
                            tab_cat.setVisibility(View.VISIBLE);

                            cat_menu_id.clear();
                            for (int i = 0; i < category_modelList.size(); i++) {
                                cat_menu_id.add(category_modelList.get(i).getId());
                                tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getTitle()));
                            }
                        } else {
                            makeGetProductRequest(parent_id);
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
                    Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
        // TODO Add your menu entries here
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





  /*     @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(false);
        MenuItem check = menu.findItem(R.id.action_change_password);
        check.setVisible(false);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setBackgroundColor(getResources().getColor(R.color.white));
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(Color.GRAY);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter_product.getFilter().filter(newText);
                return false;
            }
        });
    }*/


}
