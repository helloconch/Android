package com.android.testing.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.cache.ImageCacheStatsTracker;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * FrescoUtils工具类
 */
public class FrescoUtils {
    private static final String PHOTO_FRESCO = "frescocache";

    /**
     * @param context
     * @param cacheSizeInM 磁盘缓存的大小，以M为单位
     */
    public static void init(final Context context, int cacheSizeInM) {


        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context).setMaxCacheSize(cacheSizeInM * 1024 * 1024)//最大缓存
                .setBaseDirectoryName(PHOTO_FRESCO)//子目录
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        //缓存到应用本身的缓存文件夹
                        return context.getCacheDir();
                    }
                }).build();
        //缓存的监听接口，其方法空实现即可
        MyImageCacheStatsTracker myImageCacheStatsTracher = new MyImageCacheStatsTracker();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setImageCacheStatsTracker(myImageCacheStatsTracher)
                .setDownsampleEnabled(true)//Downsampling要不要向下采样，它处理图片速度比常规裁剪快
                //并同时支持png/jpg/wep格式图片，与ResizeOptions配合使用
                .setBitmapsConfig(Bitmap.Config.RGB_565)//如果不是重量级图片应用，就用这个省点内存。默认RGB_888
                .build();


        Fresco.initialize(context, config);

    }

    public static void loadUrl(String url, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height,
                               BaseControllerListener listener) {

        load(Uri.parse(url), draweeView, processor, width, height, listener);

    }

    public static void loadFile(String file, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height,
                                BaseControllerListener listener) {

        load(getFileUri(file), draweeView, processor, width, height, listener);

    }

    public static void loadFile(File file, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height,
                                BaseControllerListener listener) {

        load(getFileUri(file), draweeView, processor, width, height, listener);

    }

    public static void loadRes(int resId, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height,
                               BaseControllerListener listener) {

        load(getResUri(resId), draweeView, processor, width, height, listener);

    }

    public static Uri getFileUri(File file) {
        return Uri.fromFile(file);
    }

    public static Uri getFileUri(String filePath) {
        return Uri.fromFile(new File(filePath));
    }

    public static Uri getResUri(int resId) {
        return Uri.parse("res://xxyy/" + resId);
    }

    public static void load(Uri uri, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height,
                            BaseControllerListener listener) {
        ResizeOptions resizeOptions = null;
        if (width > 0 && height > 0) {
            resizeOptions = new ResizeOptions(width, height);
        }
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setPostprocessor(processor)
                        .setResizeOptions(resizeOptions)
                        //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                        // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setControllerListener(listener)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();


        draweeView.setController(controller);
    }

    /**
     * 读取缓存文件的方法
     *
     * @param url
     * @return
     */
    public static File getFileFromDiskCache(String url) {
        File localFile = null;
        if (!TextUtils.isEmpty(url)) {

            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(url), null);
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

    /**
     * 拿到指定宽高，并经过Processor处理的bitmap
     *
     * @param url
     * @param context
     * @param width
     * @param height
     * @param processor 后处理器,可为null
     * @param listener
     */
    public static void getBitmapWithProcessor(String url, Context context, int width, int height,
                                              BasePostprocessor processor, final BitmapListener listener) {

        ResizeOptions resizeOptions = null;
        if (width != 0 && height != 0) {
            resizeOptions = new ResizeOptions(width, height);
        }

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(false)
                //我们是拿bitmap对象,不是显示,所以这里不需要渐进渲染
                .setPostprocessor(processor)
                .setResizeOptions(resizeOptions)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                listener.onSuccess(bitmap);
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                listener.onFail();
            }
        }, CallerThreadExecutor.getInstance());

    }


    /**
     * 暂停网络请求
     * 在listview快速滑动时使用
     */
    public static void pause() {
        Fresco.getImagePipeline().pause();
    }


    /**
     * 恢复网络请求
     * 当滑动停止时使用
     */
    public static void resume() {
        Fresco.getImagePipeline().resume();
    }

    /**
     * 当设置roundAsCircle为true无效时,采用这个方法,常用在gif的圆形效果上
     * <p/>
     * 或者在xml中设置:fresco:roundWithOverlayColor="@color/you_color_id"
     * "you_color_id"是指你的背景色，这样也可以实现圆角、圆圈效果
     * <p/>
     * roundAsCircle的局限性:
     * 当使用BITMAP_ONLY（默认）模式时的限制：
     * 并非所有的图片分支部分都可以实现圆角，目前只有占位图片和实际图片可以实现圆角，我们正在努力为背景图片实现圆角功能。
     * 只有BitmapDrawable 和 ColorDrawable类的图片可以实现圆角。我们目前不支持包括NinePatchDrawable和 ShapeDrawable在内的其他类型图片。（无论他们是在XML或是程序中声明的）
     * 动画不能被圆角。
     * 由于Android的BitmapShader的限制，当一个图片不能覆盖全部的View的时候，边缘部分会被重复显示，而非留白。对这种情况可以使用不同的缩放类型
     * （比如centerCrop）来保证图片覆盖了全部的View。 OVERLAY_COLOR模式没有上述限制，但由于这个模式使用在图片上覆盖一个纯色图层的方式来模拟圆角效果，
     * 因此只有在图标背景是静止的并且与图层同色的情况下才能获得较好的效果。
     *
     * @param draweeView
     * @param bgColor    圆形遮罩的颜色,应该与背景色一致
     */
    public static void setCircle(SimpleDraweeView draweeView, int bgColor) {
        RoundingParams roundingParams = RoundingParams.asCircle();//这个方法在某些情况下无法成圆,比如gif
        roundingParams.setOverlayColor(bgColor);//加一层遮罩,这个是关键方法
        draweeView.getHierarchy().setRoundingParams(roundingParams);
    }


    /**
     * 清除磁盘缓存
     */
    public static void clearDiskCache() {
        Fresco.getImagePipeline().clearDiskCaches();
    }


    /**
     * 清除单张图片的磁盘缓存
     *
     * @param url
     */
    public static void clearCacheByUrl(String url) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(url);
        // imagePipeline.evictFromMemoryCache(uri);
        imagePipeline.evictFromDiskCache(uri);
        //imagePipeline.evictFromCache(uri);//这个包含了从内存移除和从硬盘移除
    }


    /**
     * Created by hss01248 on 11/26/2015.
     */
    public static class MyImageCacheStatsTracker implements ImageCacheStatsTracker {
        @Override
        public void onBitmapCachePut() {

        }

        @Override
        public void onBitmapCacheHit() {

        }

        @Override
        public void onBitmapCacheMiss() {

        }

        @Override
        public void onMemoryCachePut() {

        }

        @Override
        public void onMemoryCacheHit() {

        }

        @Override
        public void onMemoryCacheMiss() {

        }

        @Override
        public void onStagingAreaHit() {

        }

        @Override
        public void onStagingAreaMiss() {

        }

        @Override
        public void onDiskCacheHit() {
            //Logger.e("ImageCacheStatsTracker---onDiskCacheHit");
        }

        @Override
        public void onDiskCacheMiss() {
            //Logger.e("ImageCacheStatsTracker---onDiskCacheMiss");
        }

        @Override
        public void onDiskCacheGetFail() {
            //Logger.e("ImageCacheStatsTracker---onDiskCacheGetFail");
        }

        @Override
        public void registerBitmapMemoryCache(CountingMemoryCache<?, ?> countingMemoryCache) {

        }

        @Override
        public void registerEncodedMemoryCache(CountingMemoryCache<?, ?> countingMemoryCache) {

        }
    }

    public interface BitmapListener {
        void onSuccess(Bitmap bitmap);

        void onFail();
    }

}
