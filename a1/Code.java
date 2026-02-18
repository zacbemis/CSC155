package a1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.util.*;

import javagaming.utils.Utils;

public class Code extends JFrame implements GLEventListener {
    private GLCanvas myCanvas;
    private int renderingProgram;
    private int vao[] = new int[1];

    private float xTriangle = 0.0f;
    private float speedTriangle = 0.5f;
    private int directionTriangle = 1;
    private long lastTimeMs = 0;

    private volatile boolean moveInCircle = false;
    private float circleAngle = 0f;
    private static final float CIRCLE_RADIUS = 0.5f;
    private static final float CIRCLE_ANGULAR_SPEED = 1f;

    private volatile int colorMode = 0;
    private volatile float scale = 1.0f;
    private static final float SCALE_STEP = 0.05f;
    private static final float SCALE_MIN = 0.2f;
    private static final float SCALE_MAX = 3f;

    private volatile int pointingDirection = 0;

    public Code() {
        setTitle("A1");
        setSize(600, 600);
        myCanvas = new GLCanvas();
        myCanvas.addGLEventListener(this);
        KeyListener keyHandler = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case '1':
                        moveInCircle = !moveInCircle;
                        break;
                    case '2':
                        colorMode = (colorMode + 1) % 3;
                        break;
                    case '3':
                        scale = Math.min(SCALE_MAX, scale + SCALE_STEP);
                        break;
                    case '4':
                        scale = Math.max(SCALE_MIN, scale - SCALE_STEP);
                        break;
                    case '5':
                        pointingDirection = (pointingDirection + 1) % 4;
                        break;
                }
            }
        };
        myCanvas.addKeyListener(keyHandler);
        this.addKeyListener(keyHandler);
        this.add(myCanvas);
        this.setVisible(true);
        Animator animator = new Animator(myCanvas);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        System.out.println("OpenGL version:  " + gl.glGetString(GL_VERSION));
        System.out.println("OpenGL vendor:   " + gl.glGetString(GL_VENDOR));
        System.out.println("OpenGL renderer: " + gl.glGetString(GL_RENDERER));
        System.out.println("JOGL version:    " + GL4.class.getPackage().getImplementationVersion());
        System.out.println("Java version:    " + System.getProperty("java.version"));
        renderingProgram = Utils.createShaderProgram("base.vert", "base.frag");
        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
    }

    public void display(GLAutoDrawable drawable) {
        GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glClear(GL_COLOR_BUFFER_BIT);
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        gl.glUseProgram(renderingProgram);

        long now = System.currentTimeMillis();
        float dt = (lastTimeMs == 0) ? 0 : (now - lastTimeMs) / 1000f;
        lastTimeMs = now;

        float offsetX, offsetY;
        if (moveInCircle) {
            circleAngle += CIRCLE_ANGULAR_SPEED * dt;
            offsetX = CIRCLE_RADIUS * (float) Math.cos(circleAngle);
            offsetY = CIRCLE_RADIUS * (float) Math.sin(circleAngle);
        } else {
            xTriangle += directionTriangle * speedTriangle * dt;
            if (xTriangle > 1.0f) {
                xTriangle = 1.0f;
                directionTriangle = -1;
            }
            if (xTriangle < -1.0f) {
                xTriangle = -1.0f;
                directionTriangle = 1;
            }
            offsetX = xTriangle;
            offsetY = 0f;
        }

        gl.glProgramUniform1f(renderingProgram, gl.glGetUniformLocation(renderingProgram, "offsetX"), offsetX);
        gl.glProgramUniform1f(renderingProgram, gl.glGetUniformLocation(renderingProgram, "offsetY"), offsetY);
        gl.glProgramUniform1f(renderingProgram, gl.glGetUniformLocation(renderingProgram, "scale"), scale);
        float rotRad = (float) (pointingDirection * Math.PI / 2);
        gl.glProgramUniform1f(renderingProgram, gl.glGetUniformLocation(renderingProgram, "rotationAngle"), rotRad);
        gl.glProgramUniform1i(renderingProgram, gl.glGetUniformLocation(renderingProgram, "colorMode"), colorMode);

        gl.glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    public static void main(String[] args) {
        new Code();
    }
}
