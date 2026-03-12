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
import utils.Utils;

public class Code extends JFrame implements GLEventListener, KeyListener {
    private GLCanvas myCanvas;
    private int renderingProgram;
    private int vao[] = new int[1];
    private int vbo[] = new int[2];
    private Star star;

    private FloatBuffer vals = Buffers.newDirectFloatBuffer(16);
    private Matrix4f pMat = new Matrix4f();
    private Matrix4f vMat = new Matrix4f();
    private Matrix4f mMat = new Matrix4f();
    private Matrix4f mvMat = new Matrix4f();

    private Camera camera = new Camera();
    private int starTexture;
    private boolean showAxes = true; // placeholder for later axes rendering

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

        star = new Star(gl, vao, vbo);

        float aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
        pMat.identity().setPerspective((float) Math.toRadians(60.0f), aspect, 0.1f, 1000.0f);

        starTexture = Utils.loadTexture("a2/star.jpg");
    }

    public void display(GLAutoDrawable drawable) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glClear(GL_COLOR_BUFFER_BIT);
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        gl.glUseProgram(renderingProgram);

        int mvLoc = gl.glGetUniformLocation(renderingProgram, "mv_matrix");
        int pLoc = gl.glGetUniformLocation(renderingProgram, "p_matrix");

        camera.getViewMatrix(vMat);
        mMat.identity();
        mvMat.identity().mul(vMat).mul(mMat);

        gl.glUniformMatrix4fv(mvLoc, 1, false, mvMat.get(vals));
        gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

        gl.glBindVertexArray(vao[0]);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, starTexture);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glDrawArrays(GL_TRIANGLES, 0, star.getNumVertices());
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        float aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
        pMat.identity().setPerspective((float) Math.toRadians(60.0f), aspect, 0.1f, 1000.0f);
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    // --- KeyListener implementation (per 155 keyboard tips) ---

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
