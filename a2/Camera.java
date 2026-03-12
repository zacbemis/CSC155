package a2;

import org.joml.Matrix4f;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class Camera {
    private final Vector3f location = new Vector3f(0f, 0f, 8f);

    // U: right, V: up, N: forward
    private final Vector3f u = new Vector3f(1f, 0f, 0f);
    private final Vector3f v = new Vector3f(0f, 1f, 0f);
    private final Vector3f n = new Vector3f(0f, 0f, -1f);

    private static final float MOVE_STEP = 0.1f;
    private static final float ROTATE_STEP_RAD = (float) Math.toRadians(3.0);

    public Matrix4f getViewMatrix(Matrix4f dest) {
        dest.identity();
        dest.m00(u.x);
        dest.m10(u.y);
        dest.m20(u.z);

        dest.m01(v.x);
        dest.m11(v.y);
        dest.m21(v.z);

        dest.m02(-n.x);
        dest.m12(-n.y);
        dest.m22(-n.z);

        dest.m30(-u.dot(location));
        dest.m31(-v.dot(location));
        dest.m32(n.dot(location)); 
        return dest;
    }

    public void moveForward() {
        location.fma(MOVE_STEP, n);
    }

    public void moveBackward() {
        location.fma(-MOVE_STEP, n);
    }

    public void moveRight() {
        location.fma(MOVE_STEP, u);
    }

    public void moveLeft() {
        location.fma(-MOVE_STEP, u);
    }

    public void moveUp() {
        location.fma(MOVE_STEP, v);
    }

    public void moveDown() {
        location.fma(-MOVE_STEP, v);
    }

    public void panRight() {
        rotateUAndNAround(v, ROTATE_STEP_RAD);
    }

    public void panLeft() {
        rotateUAndNAround(v, -ROTATE_STEP_RAD);
    }

    public void pitchUp() {
        rotateVAndNAround(u, ROTATE_STEP_RAD);
    }

    public void pitchDown() {
        rotateVAndNAround(u, -ROTATE_STEP_RAD);
    }

    private void rotateUAndNAround(Vector3f axis, float angleRad) {
        Matrix3f rot = new Matrix3f().rotation(angleRad, axis);
        rot.transform(u);
        rot.transform(n);
        orthonormalize();
    }

    private void rotateVAndNAround(Vector3f axis, float angleRad) {
        Matrix3f rot = new Matrix3f().rotation(angleRad, axis);
        rot.transform(v);
        rot.transform(n);
        orthonormalize();
    }

    private void orthonormalize() {
        n.normalize();
        v.normalize();
        u.set(v).cross(n).normalize();
        v.set(n).cross(u).normalize();
    }

    public Vector3f getLocation() {
        return location;
    }

    public Vector3f getU() {
        return u;
    }

    public Vector3f getV() {
        return v;
    }

    public Vector3f getN() {
        return n;
    }
}
