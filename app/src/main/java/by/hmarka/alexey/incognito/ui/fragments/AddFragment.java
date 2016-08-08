package by.hmarka.alexey.incognito.ui.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.WindowCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.utils.Constants;
import by.hmarka.alexey.incognito.utils.SharedPreferenceHelper;

/**
 * Created by lashket on 28.6.16.
 */
public class AddFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_SELECT = 2;
    //private static final String KEY_MAX_POST_LENGHT = "POSTLENGHT";
    public interface AddActivityInterface{
        void setTitle(String title);
        void sendPost(String title);
        void sendPost(String title,List<String> videoIds, List<Bitmap> imagesPath);

    }
    private AddActivityInterface parent;

    EditText editText;
    ImageButton sendButton;
    ViewGroup imageContainer;
    int mMaxLenght = 500;

    HashMap<String,Bitmap> imagesCollection = new HashMap<>() ;
    HashMap<String,String> videoCollection = new HashMap<>() ;
    String mCurrentPhotoPath;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            parent = (AddActivityInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement AddActivityInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent=null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        findViews(view);
        setupListners();
        setMaxLenghtFilter();
        return view;
    }

    private void findViews(View view){
        editText = (EditText)view.findViewById(R.id.editText);
        sendButton = (ImageButton)view.findViewById(R.id.send_button);
        imageContainer = (ViewGroup)view.findViewById(R.id.image_container);
    }

    private void setupListners(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setTitle(editable.length(),mMaxLenght);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Bitmap> images = new ArrayList<Bitmap>();
                for(String key: imagesCollection.keySet()){

                    BitmapFactory.Options currentOptions = new  BitmapFactory.Options();
                    currentOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(key, currentOptions);
                    BitmapFactory.Options newOptions = new  BitmapFactory.Options();
                    newOptions.inPurgeable = true;
                    newOptions.inSampleSize = Math.max(currentOptions.outHeight/Constants.PICTURE_SIZE, currentOptions.outWidth/Constants.PICTURE_SIZE);
                    Bitmap bitmap =  BitmapFactory.decodeFile(key,newOptions);

                    int newWidth = bitmap.getWidth();
                    int newHeight =bitmap.getHeight();
                    if(newWidth > Constants.PICTURE_SIZE || newHeight > Constants.PICTURE_SIZE){
                        if (newWidth > newHeight){
                            newWidth  = Constants.PICTURE_SIZE;
                            newHeight  = (newWidth * bitmap.getHeight())/ bitmap.getWidth();
                        }else{
                            newHeight  = Constants.PICTURE_SIZE;
                            newWidth  = (bitmap.getWidth() * newHeight ) / bitmap.getHeight();
                        }
                    }
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                    //bitmap.recycle();
                    bitmap = scaledBitmap;

                    images.add(bitmap);
                }
                System.gc();

                List<String> videos = new ArrayList<String>();
                for(String key: videoCollection.keySet()){
                    //String videoUrl = "http://youtu.be/"+ key + "";
                    videos.add(key);
                }


                parent.sendPost(editText.getText().toString(), videos, images);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        int newMaxLenght = SharedPreferenceHelper.getMaxPostLenght();
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        //newMaxLenght = prefs.getInt(KEY_MAX_POST_LENGHT, 500);

        if( newMaxLenght!= mMaxLenght){
            mMaxLenght = newMaxLenght;
            setMaxLenghtFilter();

            if(editText.getText().length()>mMaxLenght) {
                Editable oldText = editText.getText();
                CharSequence newText = oldText.subSequence(0,mMaxLenght);
                editText.setText(newText);
            }
        }
        setTitle(editText.getText().length(),mMaxLenght);
    }

    private void setTitle(int actual,int max){
        StringBuilder sb = new StringBuilder();
        sb.append(actual);
        sb.append("/");
        sb.append(max);
        parent.setTitle( sb.toString());
    }

    private void setMaxLenghtFilter(){
        InputFilter[] fa= new InputFilter[1];
        fa[0] = new InputFilter.LengthFilter(mMaxLenght);
        editText.setFilters(fa);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_post_menu, menu);
//        MenuItem item = menu.findItem(R.id.add_fragment_menu_item);
//        if(item != null){
//            item.setTitle(String.valueOf( imagesCollection.values().size()));
//        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_fragment_menu_item:
                addMediaDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMediaDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        HashMap<String, String> hm = new HashMap<String,String>();
        hm.put("txt", getResources().getString(R.string.take_picture));
        hm.put("icon", Integer.toString(R.drawable.camera_popup) );
        aList.add(hm);
        HashMap<String, String> hm2 = new HashMap<String,String>();
        hm2.put("txt",getResources().getString(R.string.select_photo));
        hm2.put("icon", Integer.toString(R.drawable.choose_photo_popup) );
        aList.add(hm2);
        //video link
        HashMap<String, String> hm3 = new HashMap<String,String>();
        hm3.put("txt", getResources().getString(R.string.link_to_video));
        hm3.put("icon", Integer.toString(R.drawable.link_popup) );
        aList.add(hm3);

        String[] from = { "txt","icon" };
        int[] to = { R.id.txt,R.id.icon};

        ListAdapter adapter= new SimpleAdapter(getContext(),aList,R.layout.item_image_source_dialog,from,to);
        dialogBuilder.setAdapter(adapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                dispatchTakePictureIntent();
                                break;
                            case 1:
                                dispatchTSelectPictureIntent();
                                break;
                            case 2:
                                dispatchSelectVideoLink();
                                break;
                        }
                    }
        });
        dialogBuilder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                startTakePictureIntent();
            }
        }
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startSelectPictureIntent();
            }
        }
    }


    private void dispatchTSelectPictureIntent() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
        else{
            startSelectPictureIntent();
        }
    }
    private void  startSelectPictureIntent(){
        Intent selectPictureIntent =new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectPictureIntent , REQUEST_IMAGE_SELECT);
    }

    private void dispatchTakePictureIntent() {
        boolean cameraPerm = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        boolean sdPerm = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions( new String[]{ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else
        {
            startTakePictureIntent();
        }
    }

    private void startTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity( getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("AddPost", "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri pathUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pathUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    private void dispatchSelectVideoLink(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        final EditText edittext = new EditText(getContext());
        dialogBuilder.setView(edittext);
        dialogBuilder.setTitle(R.string.link_to_video_message);
        //dialogBuilder.setMessage("Enter Your Message");

        dialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String editTextValue = edittext.getText().toString();
                addYoutubeVideoFromVideoUrl(editTextValue);
            }
        });

        dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        dialogBuilder.show();
    }

    public void addYoutubeVideoFromVideoUrl(String videoLink) {
        String videoId = getYoutubeVideoIdFromUrl(videoLink);
        String imgUrl = "http://img.youtube.com/vi/"+ videoId + "/0.jpg";
        //String videoUrl = "http://youtu.be/"+videoId + "";

        videoCollection.put(videoId,imgUrl);
        updateMedia();
    }

    private static String getYoutubeVideoIdFromUrl(String inUrl) {
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1);
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = /*"file:" +*/ image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE :
                    Bitmap cThumbnail = getScaledPic(mCurrentPhotoPath);
                    if (imagesCollection.size() > 9) {
                        Toast.makeText(getContext(), "Максимальное количество фото - 10", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    imagesCollection.put(mCurrentPhotoPath ,cThumbnail);
                    updateMedia();
                    break;
                case REQUEST_IMAGE_SELECT:
                    if (imagesCollection.size() > 9) {
                        Toast.makeText(getContext(), "Максимальное количество фото - 10", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContext().getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap pThumbnail = getScaledPic(picturePath);
                    imagesCollection.put(picturePath, pThumbnail);
                    updateMedia();
                    break;
            }
        }
    }
    private Bitmap getScaledPic(String photoPath) {
        // Get the dimensions of the View
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int targetW = displayMetrics.heightPixels/2;
        int targetH = displayMetrics.widthPixels/2;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        bmOptions.inScreenDensity = displayMetrics.densityDpi;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        return bitmap;
    }

    private void updateMedia() {
        imageContainer.removeAllViews();
        updateVideoPreview();
        updateImagePreview();
        getActivity().invalidateOptionsMenu();
    }
    private void updateVideoPreview() {
        List<Map.Entry<String,String>> imageList =  new ArrayList<>(videoCollection.entrySet());

        Context context = getContext();

        for(int i = 0;i<imageList.size(); i++){
            Map.Entry set1 = imageList.get(i);

            View imageView1 = getVideoPreview(context, set1.getKey(),(String) set1.getValue());

            LinearLayout container = new LinearLayout(context);
            container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            container.setOrientation(LinearLayout.HORIZONTAL);
            container.setGravity(Gravity.CENTER_HORIZONTAL);
            container.addView(imageView1);

            imageContainer.addView(container);
        }
    }
    private View getVideoPreview(Context context,Object key, String imageUrl){
        View container;
        LinearLayout.LayoutParams params;
        ImageView imageView;
        LayoutInflater inflater = getLayoutInflater(null);

        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight =1;

        container = inflater.inflate(R.layout.view_image_container,null,false);
        container.setLayoutParams(params);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;

        imageView = (ImageView)container.findViewById(R.id.content_img);
        Picasso.with(getContext())
                .load(imageUrl)
                .resize(Math.round(dpWidth), Math.round(dpWidth))
                .centerCrop()
                .into(imageView);

        ImageView play_img = (ImageView)container.findViewById(R.id.play_img);
        play_img.setVisibility(View.VISIBLE);

        container.setTag(key);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoCollection.remove(view.getTag());
                updateMedia();

//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + view.getTag()));
//                    startActivity(intent);
//                } catch (ActivityNotFoundException ex) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://www.youtube.com/watch?v=" + view.getTag()));
//                    startActivity(intent);
//                }
            }
        });
        return container;
    }


    private void updateImagePreview() {
        List<Map.Entry<String,Bitmap>> imageList =  new ArrayList<>(imagesCollection.entrySet());

        Context context = getContext();

        int countCouple = imageList.size()/2;
        int ex = imageList.size()%2;

        for(int i = 0;i<countCouple; i++){
            Map.Entry set1 = imageList.get(i*2);
            View imageView1 = getImagePreview(context, set1.getKey(),(Bitmap) set1.getValue());

            Map.Entry  set2 = imageList.get(i*2+1);
            View imageView2 = getImagePreview(context, set2.getKey(),(Bitmap) set2.getValue());

            LinearLayout container = new LinearLayout(context);
            container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            container.setOrientation(LinearLayout.HORIZONTAL);
            container.setGravity(Gravity.CENTER_HORIZONTAL);
            container.addView(imageView1);
            container.addView(imageView2);

            imageContainer.addView(container);
        }

        if(ex>0)
        {
            Map.Entry set1 = imageList.get(countCouple*2);
            View imageView1 = getImagePreview(context, set1.getKey(),(Bitmap) set1.getValue());

            LinearLayout container = new LinearLayout(context);
            container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            container.setOrientation(LinearLayout.HORIZONTAL);
            container.addView(imageView1);

            imageContainer.addView(container);
        }
    }

    private View getImagePreview(Context context,Object key, Bitmap bitmap){
        View container;
        LinearLayout.LayoutParams params;
        ImageView imageView;
        LayoutInflater inflater = getLayoutInflater(null);

        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight =1;

        container = inflater.inflate(R.layout.view_image_container,null,false);
        container.setLayoutParams(params);

        imageView = (ImageView)container.findViewById(R.id.content_img);
        imageView.setImageBitmap(bitmap);

        container.setTag(key);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagesCollection.remove(view.getTag());
                updateMedia();
            }
        });
        return container;
    }

}
