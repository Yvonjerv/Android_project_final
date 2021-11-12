package com.example.finalexam.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

//自定义圆形网络ImageView组件
public class HttpCircleImageView extends AppCompatImageView {
    private static final int GET_SUCCESS =10;
    private Paint paint;
    private float width;
    private float height;
    private float radius;
    private Matrix matrix;
    private String url;

    public HttpCircleImageView(Context context) {
        super(context);
    }

    public HttpCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setAntiAlias(true);
        matrix=new Matrix();
    }

    public HttpCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
        paint.setAntiAlias(true);
        matrix=new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width=getMeasuredWidth();
        height=getMeasuredHeight();
        radius=Math.min(width,height)/2;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable=this.getDrawable();
        if(drawable==null){
            super.onDraw(canvas);
            return;
        }
        if(drawable instanceof BitmapDrawable){
            Bitmap bitmap=((BitmapDrawable) drawable).getBitmap();
            BitmapShader shader=new BitmapShader(bitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);

            float scale=Math.max(width/bitmap.getWidth(),height/bitmap.getHeight());
            matrix.setScale(scale,scale);
            shader.setLocalMatrix(matrix);

            paint.setShader(shader);
            //canvas.drawCircle(width/2,height/2,radius,paint);
            canvas.drawRoundRect(0,0,width,height,20,20,paint);
            return;
        }
        super.onDraw(canvas);
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_SUCCESS:
                    Bitmap bitmap=(Bitmap)msg.obj;
                    setImageBitmap(bitmap);
                    break;
            }
        }
    };

    private void startThread(){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap=HttpClientUtils.HttpClientGetBitmap(url, null);
                    Message message=new Message();
                    if(bitmap!=null){
                        message.what=GET_SUCCESS;
                        message.obj=bitmap;
                    }
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }

    public void setHttpImage(String url){
        this.url=url;
        startThread();
    }

}
