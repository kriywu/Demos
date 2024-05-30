package com.wurengao.surfaceviewtestdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class OpenGLFilterActivity : AppCompatActivity() {
    private lateinit var glSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glSurfaceView = GLSurfaceView(this)
        setContentView(glSurfaceView)

        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(MyGLRenderer(this))
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

}

class MyGLRenderer(private val mContext: Context) : GLSurfaceView.Renderer {
    private val vertexShaderCode = """
    attribute vec4 aPosition;
    attribute vec2 aTexCoord;
    uniform mat4 uMVPMatrix;
    varying vec2 vTexCoord;
    
    void main() {
        gl_Position = uMVPMatrix * aPosition;
        vTexCoord = aTexCoord;
    }
""".trimIndent()

    private val fragmentShaderCode = """
    precision mediump float;
    varying vec2 vTexCoord;
    uniform sampler2D sTexture;
    
    void main() {
        vec4 color = texture2D(sTexture, vTexCoord);
        float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
        gl_FragColor = vec4(gray, gray, gray, 1.0);
    }
""".trimIndent()

    private var mProgram: Int = 0
    private var mTextureId = 0
    private var mVertices: FloatBuffer? = null
    private var mTexCoords: FloatBuffer? = null

    private fun setupGraphics() {
        val vertices = floatArrayOf(
            -1.0f, -1.0f, 0.0f,   // Bottom left
            1.0f, -1.0f, 0.0f,   // Bottom right
            -1.0f,  1.0f, 0.0f,   // Top left
            1.0f,  1.0f, 0.0f    // Top right
        )
        mVertices = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
            .put(vertices)

        val texCoords = floatArrayOf(
            0.0f, 0.0f,          // Bottom left
            1.0f, 0.0f,          // Bottom right
            0.0f, 1.0f,          // Top left
            1.0f, 1.0f           // Top right
        )
        mTexCoords = ByteBuffer.allocateDirect(texCoords.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
            .put(texCoords)
    }

    override fun onSurfaceCreated(
        glUnused: GL10?,
        config: javax.microedition.khronos.egl.EGLConfig?
    ) {
        setupGraphics()
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        mProgram = createProgram(vertexShaderCode, fragmentShaderCode)
        loadTexture(R.raw.img) // 加载你的PNG图片资源
    }

    override fun onSurfaceChanged(glUnused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(mProgram)
        // 设置顶点属性
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, mVertices)
        GLES20.glEnableVertexAttribArray(0)
        // 设置纹理坐标属性
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 0, mTexCoords)
        GLES20.glEnableVertexAttribArray(1)
        // 绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun createProgram(vertexCode: String, fragmentCode: String): Int {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentCode)
        val program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)
        return program
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

    private fun loadTexture(resourceId: Int) {
        val bitmap = BitmapFactory.decodeResource(mContext.resources, resourceId)
        mTextureId = loadTexture(bitmap)
        bitmap.recycle()
    }

    private fun loadTexture(bitmap: Bitmap): Int {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        if (textures[0] != 0) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE.toFloat())
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        }
        return textures[0]
    }
}