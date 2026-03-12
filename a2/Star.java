package a2;

import java.nio.*;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.common.nio.Buffers;

public class Star {
    public Star(GL4 gl, int[] vao, int[] vbo) {
        setupStarVertices(gl, vao, vbo);
    }

    private void setupStarVertices(GL4 gl, int[] vao, int[] vbo) {
        float[] starPositions = {
            0.0f, 0.0f,  0.2f,  0.0f,  1.0f,  0.2f,  0.2f,  0.3f,  0.2f,
            0.0f, 0.0f,  0.2f,  0.2f,  0.3f,  0.2f,  0.9f,  0.3f,  0.2f,
            0.0f, 0.0f,  0.2f,  0.9f,  0.3f,  0.2f,  0.3f, -0.1f,  0.2f,
            0.0f, 0.0f,  0.2f,  0.3f, -0.1f,  0.2f,  0.5f, -0.8f,  0.2f,
            0.0f, 0.0f,  0.2f,  0.5f, -0.8f,  0.2f,  0.0f, -0.3f,  0.2f,
            0.0f, 0.0f,  0.2f,  0.0f, -0.3f,  0.2f, -0.5f, -0.8f,  0.2f,
            0.0f, 0.0f,  0.2f, -0.5f, -0.8f,  0.2f, -0.3f, -0.1f,  0.2f,
            0.0f, 0.0f,  0.2f, -0.3f, -0.1f,  0.2f, -0.9f,  0.3f,  0.2f,
            0.0f, 0.0f,  0.2f, -0.9f,  0.3f,  0.2f, -0.2f,  0.3f,  0.2f,
            0.0f, 0.0f,  0.2f, -0.2f,  0.3f,  0.2f,  0.0f,  1.0f,  0.2f,
            0.0f, 0.0f, -0.2f,  0.2f,  0.3f, -0.2f,  0.0f,  1.0f, -0.2f,
            0.0f, 0.0f, -0.2f,  0.9f,  0.3f, -0.2f,  0.2f,  0.3f, -0.2f,
            0.0f, 0.0f, -0.2f,  0.3f, -0.1f, -0.2f,  0.9f,  0.3f, -0.2f,
            0.0f, 0.0f, -0.2f,  0.5f, -0.8f, -0.2f,  0.3f, -0.1f, -0.2f,
            0.0f, 0.0f, -0.2f,  0.0f, -0.3f, -0.2f,  0.5f, -0.8f, -0.2f,
            0.0f, 0.0f, -0.2f, -0.5f, -0.8f, -0.2f,  0.0f, -0.3f, -0.2f,
            0.0f, 0.0f, -0.2f, -0.3f, -0.1f, -0.2f, -0.5f, -0.8f, -0.2f,
            0.0f, 0.0f, -0.2f, -0.9f,  0.3f, -0.2f, -0.3f, -0.1f, -0.2f,
            0.0f, 0.0f, -0.2f, -0.2f,  0.3f, -0.2f, -0.9f,  0.3f, -0.2f,
            0.0f, 0.0f, -0.2f,  0.0f,  1.0f, -0.2f, -0.2f,  0.3f, -0.2f
        };

        float[] starTextureCoordinates = {
            0.5f, 0.5f, 0.5f, 1.0f, 0.6f, 0.6f,
            0.5f, 0.5f, 0.6f, 0.6f, 1.0f, 0.6f,
            0.5f, 0.5f, 1.0f, 0.6f, 0.7f, 0.5f,
            0.5f, 0.5f, 0.7f, 0.5f, 0.8f, 0.0f,
            0.5f, 0.5f, 0.8f, 0.0f, 0.5f, 0.3f,
            0.5f, 0.5f, 0.5f, 0.3f, 0.2f, 0.0f,
            0.5f, 0.5f, 0.2f, 0.0f, 0.3f, 0.5f,
            0.5f, 0.5f, 0.3f, 0.5f, 0.0f, 0.6f,
            0.5f, 0.5f, 0.0f, 0.6f, 0.4f, 0.6f,
            0.5f, 0.5f, 0.4f, 0.6f, 0.5f, 1.0f,
            0.5f, 0.5f, 0.6f, 0.6f, 0.5f, 1.0f,
            0.5f, 0.5f, 1.0f, 0.6f, 0.6f, 0.6f,
            0.5f, 0.5f, 0.7f, 0.5f, 1.0f, 0.6f,
            0.5f, 0.5f, 0.8f, 0.0f, 0.7f, 0.5f,
            0.5f, 0.5f, 0.5f, 0.3f, 0.8f, 0.0f,
            0.5f, 0.5f, 0.2f, 0.0f, 0.5f, 0.3f,
            0.5f, 0.5f, 0.3f, 0.5f, 0.2f, 0.0f,
            0.5f, 0.5f, 0.0f, 0.6f, 0.3f, 0.5f,
            0.5f, 0.5f, 0.4f, 0.6f, 0.0f, 0.6f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.4f, 0.6f
        };

        gl.glGenVertexArrays(vao.length, vao, 0);
        gl.glBindVertexArray(vao[0]);
        gl.glGenBuffers(vbo.length, vbo, 0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
        FloatBuffer posBuf = Buffers.newDirectFloatBuffer(starPositions);
        gl.glBufferData(GL_ARRAY_BUFFER, posBuf.limit()*4, posBuf, GL_STATIC_DRAW);

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
        FloatBuffer texBuf = Buffers.newDirectFloatBuffer(starTextureCoordinates);
        gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit()*4, texBuf, GL_STATIC_DRAW);
    }

    public int getNumVertices() {
        return 60;
    }
}
