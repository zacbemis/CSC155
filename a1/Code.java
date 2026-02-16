package a1;

import javax.swing.JFrame;

import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.util.*;

public class Code extends JFrame implements GLEventListener {
    private GLCanvas myCanvas;
    private int renderingProgram;
    private int vao[] = new int[1];

    private float xTriangle = 0.0f;
    private float speedTriangle = 0.5f;
    private int directionTriangle = 1;
    private long lastTimeMs = 0;

    public Code() {
        setTitle("A1");
        setSize(600, 400);
        myCanvas = new GLCanvas();
        myCanvas.addGLEventListener(this);
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

		xTriangle += directionTriangle * speedTriangle * dt;
		if (xTriangle > 1.0f)  { xTriangle = 1.0f;  directionTriangle = -1; }
		if (xTriangle < -1.0f) { xTriangle = -1.0f; directionTriangle = 1; }
		int offsetLoc = gl.glGetUniformLocation(renderingProgram, "offset");
		gl.glProgramUniform1f(renderingProgram, offsetLoc, xTriangle);
		gl.glDrawArrays(GL_TRIANGLES,0,3);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    public static void main(String[] args) {
        new Code();
    }
}
