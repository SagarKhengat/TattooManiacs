package in.idk.tattoomaniacs.activity.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.idk.tattoomaniacs.R;
import in.idk.tattoomaniacs.activity.adapter.GalleryAdapter;
import in.idk.tattoomaniacs.activity.app.AppController;
import in.idk.tattoomaniacs.activity.model.Image;

/**
 * Created by Sagar K on 15-01-2017.
 */

public class ChildGallery extends Fragment {
    private static final String TAG_FEED = "feed", TAG_ENTRY = "entry",
            TAG_MEDIA_GROUP = "media$group",
            TAG_MEDIA_CONTENT = "media$content", TAG_IMG_URL = "url",
            TAG_IMG_WIDTH = "width", TAG_IMG_HEIGHT = "height", TAG_ID = "id",
            TAG_T = "$t";

    private ArrayList<Image> images;
//    private ProgressDialog pDialog = new ProgressDialog(getActivity()); ;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

MainActivity mn=new MainActivity();
        View rootView = inflater.inflate(R.layout.chid_gallery, container, false);



        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);


        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getActivity(), images);

      RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
      //GridLayoutManager   mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);

      recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        Bundle bundle = getArguments();
        String url = bundle.getString("url");

//        String endpoint = "https://picasaweb.google.com/data/feed/api/user/116197712105533019354/albumid/6375369972424830321?alt=json";
       fetchImages(url);






return rootView;
     }
    public void fetchImages(String endpoint) {
final ProgressDialog pDialog = ProgressDialog.show(getActivity(), "", "Loading Images Please Wait...!!!", true);




        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, endpoint,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG,
//                        "List of photos json reponse: "
//                                + response.toString());
                pDialog.hide();
                try {
                    // Parsing the json response
                    JSONArray entry = response.getJSONObject(TAG_FEED)
                            .getJSONArray(TAG_ENTRY);

                    // looping through each photo and adding it to list
                    // data set
                    for (int i = entry.length()-1; i > 0 ; i--) {
                        JSONObject photoObj = (JSONObject) entry.get(i);
                        Image image = new Image();
                        JSONArray mediacontentArry = photoObj
                                .getJSONObject(TAG_MEDIA_GROUP)
                                .getJSONArray(TAG_MEDIA_CONTENT);


                        if (mediacontentArry.length() > 0) {
                            JSONObject mediaObj = (JSONObject) mediacontentArry
                                    .get(0);

                            String url = mediaObj
                                    .getString(TAG_IMG_URL);
                            image.setMedium(url);
                            image.setLarge(url);
//                            String photoJson = photoObj.getJSONObject(
//                                    TAG_ID).getString(TAG_T)
//                                    + "&imgmax=d";
//
//                            int width = mediaObj.getInt(TAG_IMG_WIDTH);
//                            int height = mediaObj
//                                    .getInt(TAG_IMG_HEIGHT);



                            // Adding the photo to list data set
                            images.add(image);

//                            Log.d(TAG, "Photo: " + url + ", w: "
//                                    + width + ", h: " + height);
                        }
                    }

                    // Notify list adapter about dataset changes. So
                    // that it renders grid again
                    mAdapter.notifyDataSetChanged();

                    // Hide the loader, make grid visible
//                    pbLoader.setVisibility(View.GONE);
//                    gridView.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            getString(R.string.msg_unknown_error),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                 unable to fetch wallpapers
//                 either google username is wrong or
//                 devices doesn't have internet connection
                Toast.makeText(getActivity(),
                        getString(R.string.msg_wall_fetch_error),
                        Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        });
        // Remove the url from cache
//        AppController.getInstance().getRequestQueue().getCache().remove(endpoint);
//
//        // Disable the cache for this url, so that it always fetches updated
//        // json
//        jsonObjReq.setShouldCache(false);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }






}
