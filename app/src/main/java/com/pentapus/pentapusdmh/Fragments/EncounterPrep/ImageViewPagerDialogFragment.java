package com.pentapus.pentapusdmh.Fragments.EncounterPrep;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.Utils;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Koni on 4/4/16.
 */
public class ImageViewPagerDialogFragment extends Fragment {

    private static final String ARG_PAGE = "ARG_PAGE";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageFragmentPagerAdapter pagerAdapter;
    private FloatingActionButton fabImageVP;
    private Button bDone;
    private int id;
    private Uri imageUri;
    private Uri myFile;
    private int px;

    private static int selectedType = -1;
    private static int selectedPos =-1;
    private static int highlightedPos = -1;
    private static Uri selectedUri;



    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_CHOOSE_IMG = 2;

    public interface UpdateableFragment {
        public void update(File[] updateData);
    }



    public ImageViewPagerDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static ImageViewPagerDialogFragment newInstance() {
        ImageViewPagerDialogFragment fragment = new ImageViewPagerDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        px = (int) Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_viewpager_tab_layout, parent, false);
        ((MainActivity)getActivity()).setFabVisibility(false);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, Utils.getStatusBarHeight(getActivity()), 0, 0);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        fabImageVP = (FloatingActionButton) view.findViewById(R.id.fabImageVP);

        bDone = (Button) view.findViewById(R.id.bDone);
        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = ImageViewPagerDialogFragment.getSelectedUri();
                if(imageUri != null){
                    Intent intent = new Intent();
                    intent.putExtra("imageUri", String.valueOf(imageUri));
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                }else{
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        fabImageVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Glide.clear(bChooseImage);
                Crop.pickImage(getContext(), getActivity().getSupportFragmentManager().findFragmentByTag("F_IMAGE_PAGER"));
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0 :
                        fabImageVP.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fabImageVP.setVisibility(View.GONE);
                        break;
                    default:
                        fabImageVP.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        return view;
    }



    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pagerAdapter = new ImageFragmentPagerAdapter(getChildFragmentManager(), getContext(), id);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        } else if(requestCode == RESULT_CHOOSE_IMG && resultCode == Activity.RESULT_OK){
            if(data != null) {
                String value = data.getStringExtra("imageUri");
                if(value != null) {
                    Uri uri = Uri.parse(value);
                    myFile = uri;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getContext().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getContext(), getActivity().getSupportFragmentManager().findFragmentByTag("F_IMAGE_PAGER"));
    }


    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Glide.with(getContext())
                    .load(Crop.getOutput(result))
                    .asBitmap()
                    .override(px, px)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ContextWrapper cw = new ContextWrapper(getContext().getApplicationContext());
                            // path to /data/data/yourapp/app_data/iconDir
                            File directory = cw.getDir("myIconDir", Context.MODE_PRIVATE);
                            // Create iconDir
                            UUID uuid = UUID.randomUUID();
                            String randomUUIDString = uuid.toString();
                            File mypath = new File(directory, randomUUIDString);
                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(mypath);
                                // Use the compress method on the BitMap object to write image to the OutputStream
                                resource.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            directory = cw.getDir("myIconDir", Context.MODE_PRIVATE);
                            File[] newFile = directory.listFiles();
                            pagerAdapter.setImageUri(newFile);

                            //((ViewPagerMyImageGridFragment)pagerAdapter.getItem(0)).updateView();
                            //File directory = new getContext().getFileStreamPath("app_iconDir");
                            //((ViewPagerMyImageGridFragment)pagerAdapter.getItem()).refresh();
                            Uri uri = Uri.parse(mypath.getPath());
                            imageUri = uri;
                            myFile = uri;
                            //setimageuri
                        }
                    });

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static Uri getSelectedUri() {
        return selectedUri;
    }

    public static void setSelectedUri(Uri selectedUri) {
        ImageViewPagerDialogFragment.selectedUri = selectedUri;
    }

    public static int getSelectedPos() {
        return selectedPos;
    }

    public static void setSelectedPos(int selectedPos) {
        ImageViewPagerDialogFragment.selectedPos = selectedPos;
    }

    public static int getHighlightedPos() {
        return highlightedPos;
    }

    public static void setHighlightedPos(int highlightedPos) {
        ImageViewPagerDialogFragment.highlightedPos = highlightedPos;
    }

    public static int getSelectedType() {
        return selectedType;
    }

    public static void setSelectedType(int selectedType) {
        ImageViewPagerDialogFragment.selectedType = selectedType;
    }

    @Override
    public void onDestroy(){
        Glide.get(getContext()).clearMemory(); //  call this method manual
        super.onDestroy();
    }
}
