package com.example.abc.myownmatrixapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shockwave.pdfium.PdfiumCore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.Inflater;

public class document_detail_fragment extends Fragment {



   static ImageView imgTakenPic;
    private static final int CAM_REQUEST = 1313;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_FILE = 2;
Activity activity;
    private byte[] image;
    String DateVar;
    String TimeVar;

    EditText comment;
    TextView date, time,file_name;

 View view ;
    Button insert;
     Context context;




    public document_detail_fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_document_detail_fragment, container, false);
        context=getContext();

        FloatingActionButton fab1=view.findViewById(R.id.fab_action1);
        FloatingActionButton fab2=view.findViewById(R.id.fab_action2);
        FloatingActionButton fab3=view.findViewById(R.id.fab_action3);
imgTakenPic=(ImageView)view.findViewById(R.id.imageView);
fab2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
});

fab1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setType("application/pdf|application/vnd.openxmlformats-officedocument.wordprocessingml.document|application/msword|text/plain");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),RESULT_LOAD_FILE);
    }
});
        fab3.setOnClickListener(new document_detail_fragment.btnTakePhotoClicker());



        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat TimeFormat=new SimpleDateFormat("HH:mm:ss");
        TimeVar=TimeFormat.format(calendar.getTime());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        DateVar=simpleDateFormat.format(calendar.getTime());






        final FloatingActionsMenu ffMenu =(FloatingActionsMenu)view. findViewById(R.id.fb_menu);
        RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.rel_layout3);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (ffMenu.isExpanded())
                    ffMenu.collapse();
                return false;
            }
        });
        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialogue_document_detail);

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            // set the custom dialog components - text, image and button
            time = (TextView) dialog.findViewById(R.id.time);
            comment = (EditText) dialog.findViewById(R.id.comment);
            date = (TextView) dialog.findViewById(R.id.date);

            ImageView imgeView = (ImageView) dialog.findViewById(R.id.imageView);
            imgeView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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
        if(requestCode==RESULT_LOAD_FILE)
        {
        if (data != null && data.getData() != null && resultCode == getActivity().RESULT_OK) {
           Uri uri=data.getData();
           String t=getMimeType(uri);
           if(t.equals("application/pdf")) {
               generateImageFromPdf(uri);
           }
           else
               if(t.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")||t.equals("text/plain")||t.equals("application/msword"))
               {
                 openFile(uri,t);
               }
        }
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
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    void openFile(final Uri myuri,final String t)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue_document_detail1);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // set the custom dialog components - text, image and button
        time = (TextView) dialog.findViewById(R.id.time);
        comment = (EditText) dialog.findViewById(R.id.comment);
        date = (TextView) dialog.findViewById(R.id.date);
        file_name=(TextView)dialog.findViewById(R.id.file_name);
        time.setText(TimeVar);
        date.setText(DateVar);
        File file= new File(myuri.getPath());
        file.getName();
        file_name.setText(file.getName());

        file_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setDataAndType(myuri,t)  ;
startActivity(intent);
//                try {
//                    startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    int time = 10;
//                    for (int i = 0; i < time; i++) {
//                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
//                    }
//                }

            }
        });
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


    void generateImageFromPdf(final Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(pdfUri, "r");
            com.shockwave.pdfium.PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
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
            image.setImageBitmap(bmp);
            time.setText(TimeVar);
            date.setText(DateVar);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");

                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        //if user doesn't have pdf reader instructing to download a pdf reader
                    }

                }
            });

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch(Exception e) {
            //todo with exception
        }
    }

}
