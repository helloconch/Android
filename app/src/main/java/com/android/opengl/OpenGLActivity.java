package com.android.opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.testing.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OpenGL
 * http://blog.piasy.com/2016/06/07/Open-gl-es-android-2-part-1/
 * <p/>
 * OpenGL 绘制的都是图形，包括形状和填充，基本形状是三角形。
 * 每个形状都有顶点，Vertix，顶点的序列就是一个图形。
 * 图形有所谓的正反面，如果我们看向一个图形，它的顶点序列是逆时针方向，那我们看到的就是正面。
 * Shader，着色器，用来描述如何绘制（渲染），GLSL 是 OpenGL 的编程语言，全称就叫 OpenGL Shader Language。OpenGL 渲染需要两种 shader，vertex 和 fragment。
 * Vertex shader，控制顶点的绘制，指定坐标、变换等。
 * Fragment shader，控制形状内区域渲染，纹理填充内容
 */
public class OpenGLActivity extends AppCompatActivity {

    //    @BindView(R.id.openGL)
    GLSurfaceView openGL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl);
        ButterKnife.bind(this);

    }

    private void init() {
        //GL ES 版本
        openGL.setEGLContextClientVersion(2);
        //渲染逻辑
        openGL.setRenderer(new MyRender());
        //渲染模式 RENDERMODE_WHEN_DIRTY 和 RENDERMODE_CONTINUOUSLY，前者是懒惰渲染，
        // 需要手动调用 glSurfaceView.requestRender() 才会进行更新，而后者则是不停渲染。
        openGL.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    static class MyRender implements GLSurfaceView.Renderer {
        /**
         * 在 surface 创建时被回调，通常用于进行初始化工作，只会被回调一次
         *
         * @param gl
         * @param config
         */
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        }

        /**
         * 在每次 surface 尺寸变化时被回调，注意，第一次得知 surface 的尺寸时也会回调
         *
         * @param gl
         * @param width
         * @param height
         */
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        /**
         * 在绘制每一帧的时候回调
         *
         * @param gl
         */
        @Override
        public void onDrawFrame(GL10 gl) {

        }
    }
}
