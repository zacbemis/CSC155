package a2;

import java.nio.FloatBuffer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import com.jogamp.common.nio.Buffers;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import utils.Utils;
import utils.ImportedModel;

public class Code extends JFrame implements GLEventListener, KeyListener {
    private GLCanvas myCanvas;
    private int renderingProgram;
    private int vao[] = new int[1];
    private int vbo[] = new int[2];
    private int catVao[] = new int[1];
    private int catVbo[] = new int[2];
    private int axesVao[] = new int[1];
    private int axesVbo[] = new int[1];
    private Star star;
    private ImportedModel catModel;

    private FloatBuffer vals = Buffers.newDirectFloatBuffer(16);
    private Matrix4f pMat = new Matrix4f();
    private Matrix4f vMat = new Matrix4f();
    private Matrix4f mMat = new Matrix4f();
    private Matrix4f mvMat = new Matrix4f();

    private int mvLoc;
    private int pLoc;
    private int useTexLoc;
    private int solidColorLoc;

    private Camera camera = new Camera();
    private int starTexture;
    private int catTexture;
    private boolean showAxes = true;

    public Code() {
        setTitle("A2");
        setSize(600, 600);
        myCanvas = new GLCanvas();
        myCanvas.addGLEventListener(this);
        myCanvas.addKeyListener(this);
        this.addKeyListener(this);
        this.add(myCanvas);
        this.setVisible(true);
        Animator animator = new Animator(myCanvas);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        renderingProgram = Utils.createShaderProgram("a2/vert.glsl", "a2/frag.glsl");

        mvLoc = gl.glGetUniformLocation(renderingProgram, "mv_matrix");
        pLoc = gl.glGetUniformLocation(renderingProgram, "p_matrix");
        useTexLoc = gl.glGetUniformLocation(renderingProgram, "useTexture");
        solidColorLoc = gl.glGetUniformLocation(renderingProgram, "solidColor");

        star = new Star(gl, vao, vbo);
        catModel = new ImportedModel("a2/assets/Cat.obj");
        setupCat(gl);
        setupAxes(gl);

        starTexture = Utils.loadTexture("a2/star.jpg");
        catTexture = Utils.loadTexture("a2/assets/Cat_diffuse.jpg");

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void display(GLAutoDrawable drawable) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glUseProgram(renderingProgram);

        camera.getViewMatrix(vMat);
        mMat.identity();
        mvMat.identity().mul(vMat).mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

        drawStar(gl);
        drawCat(gl);
        if (showAxes) {
            drawAxes(gl);
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glViewport(0, 0, width, height);

        float aspect = (float) width / (float) height;
        pMat.identity().setPerspective((float) Math.toRadians(60.0f), aspect, 0.1f, 1000.0f);
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    private void setupCat(GL4 gl) {
        int n = catModel.getNumVertices();
        Vector3f[] vertices = catModel.getVertices();
        Vector2f[] texCoords = catModel.getTexCoords();

        float[] pvalues = new float[n * 3];
        float[] tvalues = new float[n * 2];
        for (int i = 0; i < n; i++) {
            pvalues[i * 3]     = vertices[i].x();
            pvalues[i * 3 + 1] = vertices[i].y();
            pvalues[i * 3 + 2] = vertices[i].z();
            tvalues[i * 2]     = texCoords[i].x();
            tvalues[i * 2 + 1] = texCoords[i].y();
        }

        gl.glGenVertexArrays(catVao.length, catVao, 0);
        gl.glBindVertexArray(catVao[0]);
        gl.glGenBuffers(catVbo.length, catVbo, 0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, catVbo[0]);
        FloatBuffer vertBuf = Buffers.newDirectFloatBuffer(pvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, vertBuf.limit() * 4, vertBuf, GL_STATIC_DRAW);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, catVbo[1]);
        FloatBuffer texBuf = Buffers.newDirectFloatBuffer(tvalues);
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit() * 4, texBuf, GL_STATIC_DRAW);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);

        gl.glBindVertexArray(0);
    }

    private void drawCat(GL4 gl) {
        mMat.identity();
        mMat.translate(3.0f, 0.0f, 0.0f);
        mMat.scale(0.015f);
        mvMat.identity().mul(vMat).mul(mMat);
        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));

        gl.glBindVertexArray(catVao[0]);

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, catTexture);
        gl.glUniform1i(useTexLoc, 1);

        gl.glDrawArrays(GL_TRIANGLES, 0, catModel.getNumVertices());
    }

    private void drawStar(GL4 gl) {
        mMat.identity();
        mvMat.identity().mul(vMat).mul(mMat);
        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));

        gl.glBindVertexArray(vao[0]);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, starTexture);
        gl.glUniform1i(useTexLoc, 1);

        gl.glDrawArrays(GL_TRIANGLES, 0, star.getNumVertices());
    }

    private void drawAxes(GL4 gl) {
        mMat.identity();
        mvMat.identity().mul(vMat).mul(mMat);
        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));

        gl.glBindVertexArray(axesVao[0]);
        gl.glUniform1i(useTexLoc, 0);

        gl.glUniform4f(solidColorLoc, 1.0f, 0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL_LINES, 0, 2);

        gl.glUniform4f(solidColorLoc, 0.0f, 1.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL_LINES, 2, 2);

        gl.glUniform4f(solidColorLoc, 0.0f, 0.0f, 1.0f, 1.0f);
        gl.glDrawArrays(GL_LINES, 4, 2);
    }

    private void setupAxes(GL4 gl) {
        float axisLength = 5.0f;
        float[] axisPositions = {
                0.0f, 0.0f, 0.0f,
                axisLength, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, axisLength, 0.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, axisLength
        };

        gl.glGenVertexArrays(axesVao.length, axesVao, 0);
        gl.glBindVertexArray(axesVao[0]);
        gl.glGenBuffers(axesVbo.length, axesVbo, 0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, axesVbo[0]);
        FloatBuffer axisBuf = Buffers.newDirectFloatBuffer(axisPositions);
        gl.glBufferData(GL_ARRAY_BUFFER, axisBuf.limit() * 4, axisBuf, GL_STATIC_DRAW);

        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindVertexArray(0);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                camera.moveForward();
                break;
            case KeyEvent.VK_S:
                camera.moveBackward();
                break;
            case KeyEvent.VK_A:
                camera.moveLeft();
                break;
            case KeyEvent.VK_D:
                camera.moveRight();
                break;
            case KeyEvent.VK_Q:
                camera.moveUp();
                break;
            case KeyEvent.VK_E:
                camera.moveDown();
                break;
            case KeyEvent.VK_LEFT:
                camera.panLeft();
                break;
            case KeyEvent.VK_RIGHT:
                camera.panRight();
                break;
            case KeyEvent.VK_UP:
                camera.pitchUp();
                break;
            case KeyEvent.VK_DOWN:
                camera.pitchDown();
                break;
            case KeyEvent.VK_SPACE:
                showAxes = !showAxes;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // not used
    }

    public static void main(String[] args) {
        new Code();
    }
}
