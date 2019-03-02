package com.example.abc.myownmatrixapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DocumentActivity extends AppCompatActivity {


    ImageView imgTakenPic;
    private static final int CAM_REQUEST = 1313;


    private byte[] image;
    String DateVar;
    String TimeVar;

    EditText comment;
    TextView date, time;

    Button insert;
    final Context context=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);



        FloatingActionButton fab1=findViewById(R.id.fab_action1);
        FloatingActionButton fab2=findViewById(R.id.fab_action2);
        FloatingActionButton fab3=findViewById(R.id.fab_action3);


        fab3.setOnClickListener(new btnTakePhotoClicker());



        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat TimeFormat=new SimpleDateFormat("HH:mm:ss");
        TimeVar=TimeFormat.format(calendar.getTime());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        DateVar=simpleDateFormat.format(calendar.getTime());






        final FloatingActionsMenu ffMenu =(FloatingActionsMenu) findViewById(R.id.fb_menu);
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.rel_layout3);
relativeLayout.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (ffMenu.isExpanded())
           ffMenu.collapse();
        return false;
    }
});


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_REQUEST) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");


            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialogue_document_detail);

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            // set the custom dialog components - text, image and button
             time = (TextView) dialog.findViewById(R.id.time);
            comment = (EditText) dialog.findViewById(R.id.comment);
            date = (TextView) dialog.findViewById(R.id.date);
            ImageView image = (ImageView) dialog.findViewById(R.id.imageView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setClipToOutline(true);
            }
            image.setImageBitmap(bitmap);
            time.setText(TimeVar);
            date.setText(DateVar);
            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }
    class btnTakePhotoClicker implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_REQUEST);

        }
    }



    private byte[] imageViewToByte(ImageView image) {
        // Toast.makeText(Data.this, "array mai tou aya ", Toast.LENGTH_SHORT).show();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;

    }
    private void ClearText() {
        date.setText("");
        time.setText("");
        comment.setText("");
        comment.requestFocus();

    }

    public void showToast(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
