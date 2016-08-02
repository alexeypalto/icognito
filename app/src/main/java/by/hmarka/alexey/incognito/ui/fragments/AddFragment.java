package by.hmarka.alexey.incognito.ui.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.utils.SharedPreferenceHelper;

/**
 * Created by lashket on 28.6.16.
 */
public class AddFragment extends Fragment {

    private static final String KEY_MAX_POST_LENGHT = "POSTLENGHT";
    public interface AddActivityInterface{
        void setTitle(String title);
        void sendPost(String title);
        void sendPost(String title,List<Bitmap> imagesPath);
    }
    private AddActivityInterface parent;

    EditText editText;
    ImageButton sendButton;
    ViewGroup imageContainer;
    int mMaxLenght = 500;

    HashMap<String,Bitmap> imagesCollection = new HashMap<>() ;

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
                for(String path : imagesCollection.keySet()){
                    Bitmap bitmap = BitmapFactory.decodeFile(path);//, bmOptions);
                    images.add(bitmap);
                }


                  parent.sendPost(editText.getText().toString(), images);
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
//        HashMap<String, String> hm3 = new HashMap<String,String>();
//        hm3.put("txt", getResources().getString(R.string.link_to_video));
//        hm3.put("icon", Integer.toString(R.drawable.link_popup) );
//        aList.add(hm3);

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

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_SELECT = 2;
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

    private void dispatchSelectVideoLink(){

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

    String mCurrentPhotoPath;
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
                    imagesCollection.put(mCurrentPhotoPath,cThumbnail);
                    updateMedia();
                    break;
                case REQUEST_IMAGE_SELECT:
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContext().getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap pThumbnail = getScaledPic(picturePath);
                    imagesCollection.put(picturePath,pThumbnail);
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
        updatePreview();
        getActivity().invalidateOptionsMenu();
    }

    private void updatePreview() {
        imageContainer.removeAllViews();
        List<Map.Entry<String,Bitmap>> imageList =  new ArrayList<>(imagesCollection.entrySet());
        //List<Bitmap> imageList = new ArrayList<>(imagesCollection.values());


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

    private View getImagePreview(Context context,Object key, Bitmap binmap){
        View container;
        LinearLayout.LayoutParams params;
        ImageView imageView;
        LayoutInflater inflater = getLayoutInflater(null);

        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight =1;

        container = inflater.inflate(R.layout.view_image_container,null,false);
        container.setLayoutParams(params);

        imageView = (ImageView)container.findViewById(R.id.content_img);
        imageView.setImageBitmap(binmap);

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
