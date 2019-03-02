package com.example.abc.myownmatrixapp;

public class RecieptItems {


    private String date;
    private  String time;
    private  String comment;
    private byte[] Im;

    public RecieptItems( String date,String time,String comment,byte[]im )

    {

        this.Im=im ;
        this.date=date;
        this.time=time;
        this.comment=comment;
    }

    public  String getDate()

    {
        return date;
    }

    public  String getTime()
    {
        return time;
    }

    public String getComment()
    {
        return comment;
    }

    public byte[] getImage() {
        return Im;
    }


}
