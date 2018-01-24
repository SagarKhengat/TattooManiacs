package in.idk.tattoomaniacs.activity.activity;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import in.idk.tattoomaniacs.R;
import in.idk.tattoomaniacs.activity.model.Image;

/**
 * Created by Sagar K on 12-01-2017.
 */

public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Image> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
//    private TextView lblCount;
    private AdView mAdView;
    private int selectedPosition = 0;
    private LinearLayout llSetWallpaper, llDownloadWallpaper;
    private static final String FOLDER_NAME="TattooManiacs";


    static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
//        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        mAdView = (AdView) v.findViewById(R.id.adView);

        MobileAds.initialize(getActivity(), "ca-app-pub-6222232916855878~5732205742");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        images = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
//        lblCount.setText((position + 1) + " of " + images.size());

        Image image = images.get(position);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            final TouchImageView imageViewPreview = (TouchImageView) view.findViewById(R.id.image_preview);


            final FloatingActionsMenu fab = ( FloatingActionsMenu ) view.findViewById(R.id.fab);
            fab.setVisibility(View.GONE);

            final FloatingActionButton send=(FloatingActionButton) view.findViewById(R.id.send);

                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            GlideBitmapDrawable bitmapDrawable = (GlideBitmapDrawable)imageViewPreview.getDrawable();
                            Bitmap bitmap = bitmapDrawable.getBitmap();

                            // Save this bitmap to a file.
                            File cache = getContext().getExternalCacheDir();
                            File sharefile = new File(cache, "toshare.png");
                            Log.d("share file type is", sharefile.getAbsolutePath());
                            try {
                                FileOutputStream out = new FileOutputStream(sharefile);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                out.flush();
                                out.close();
                            } catch (IOException e) {
                                Log.e("ERROR", String.valueOf(e.getMessage()));

                            }


                            // Now send it out to share
                            Intent share = new Intent(android.content.Intent.ACTION_SEND);
                            share.setType("image/*");
                            share.putExtra(Intent.EXTRA_STREAM,
                                    Uri.parse("file://" + sharefile));

                            startActivity(Intent.createChooser(share,
                                    "Share This Tattoo Design with"));




                        }
                    });

            final FloatingActionButton wallpaper=(FloatingActionButton) view.findViewById(R.id.wallpaper);
                    wallpaper.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final AlertDialog ad = new AlertDialog.Builder(getActivity())
                                    .create();
                            ad.setCancelable(false);
                            ad.setTitle("Set Wallpaper");
                            ad.setMessage("Do you want to Set this Tattoo Design as Wallpaper?");
                            ad.setButton(AlertDialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    GlideBitmapDrawable bitmapDrawable = (GlideBitmapDrawable)imageViewPreview.getDrawable();
                                    Bitmap bitmap = bitmapDrawable.getBitmap();
                                    setAsWallpaper(bitmap);
                                }
                            });
                            ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Not Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ad.dismiss();
                                }
                            });

                            ad.show();

                        }
                    });


            final FloatingActionButton download =(FloatingActionButton) view.findViewById(R.id.download);
                 download.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                             if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                 ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                             }
                         }
                         final AlertDialog ad = new AlertDialog.Builder(getActivity())
                                 .create();
                         ad.setCancelable(false);
                         ad.setTitle("Download Image");
                         ad.setMessage("Do you want to Download this Tattoo Design?");
                         ad.setButton(AlertDialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {

                                 GlideBitmapDrawable bitmapDrawable = (GlideBitmapDrawable)imageViewPreview.getDrawable();
                                 Bitmap bitmap = bitmapDrawable.getBitmap();
                                 saveImageToSDCard(bitmap);
                             }
                         });
                         ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Not Now",  new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 ad.dismiss();
                             }
                         });

                         ad.show();
                     }
                 });








            Image image = images.get(position);

            Glide.with(getContext()).load(image.getLarge())
                    .thumbnail(0.5f)

                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            fab.setVisibility(View.VISIBLE);
                            imageViewPreview.setImageDrawable(resource);
                        }
                    });

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        public void saveImageToSDCard(Bitmap bitmap) {

            File myDir = new File(
                    Environment.getExternalStorageDirectory().getPath()
                            + File.separator
                            + FOLDER_NAME);

            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Tattoo_Design-" + n + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Toast.makeText(getContext(),"Tattoo design saved to TattooManiacs Folder...!!!",
                        Toast.LENGTH_LONG).show();
                Log.d(TAG, "Tattoo_Design saved to: " + file.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(),"Sorry! Failed to save Tattoo Design...!!!",
                        Toast.LENGTH_LONG).show();
            }
        }

        public void setAsWallpaper(Bitmap bitmap) {
            try {
                WallpaperManager wm = WallpaperManager.getInstance(getContext());

                wm.setBitmap(bitmap);
                Toast.makeText(getContext(),"Wallpaper Set to the Screen...!!!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(),"Sorry! Unable to set wallpapaer",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}
