package by.hmarka.alexey.incognito.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import by.hmarka.alexey.incognito.BuildConfig;
import by.hmarka.alexey.incognito.IncognitoApplication;
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
    }
    private AddActivityInterface parent;

    EditText editText;
    ImageButton sendButton;
    ViewGroup imageContainer;
    GridView gridView;
    ImageView preview;
    int mMaxLenght = 500;

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
        //setHasOptionsMenu(true);
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
        preview = (ImageView)view.findViewById(R.id.img);
        imageContainer = (ViewGroup)view.findViewById(R.id.image_container);
        //gridView = (GridView)view.findViewById(R.id.grid_image_container);
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
                  parent.sendPost(editText.getText().toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        int newMaxLenght;// = SharedPreferenceHelper.getMaxPostLenght();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        newMaxLenght = prefs.getInt(KEY_MAX_POST_LENGHT, 500);

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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_fragment_menu_item:
                addMedia();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMedia(){

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
                                break;
                        }
                    }
        });
        dialogBuilder.show();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_SELECT = 2;
    private void dispatchTSelectPictureIntent() {
        Intent selectPictureIntent =new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectPictureIntent , REQUEST_IMAGE_SELECT);
    }

    private void dispatchTakePictureIntent() {
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
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
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
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE :
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    preview.setImageBitmap(imageBitmap);
                    break;
                case REQUEST_IMAGE_SELECT:
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContext().getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    preview.setImageBitmap(thumbnail );
                    break;
            }
        }
    }
}
