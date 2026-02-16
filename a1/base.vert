#version 430

uniform float offsetX;
uniform float offsetY;
uniform float scale;
uniform float rotationAngle;
uniform int colorMode;

out vec4 vertColor;

void main(void) {
    const vec2 base[3] = vec2[3](
        vec2( 0.0,  0.5),
        vec2(-0.1, -0.5),
        vec2( 0.1, -0.5)
    );
    vec2 p = base[gl_VertexID] * scale;
    float c = cos(rotationAngle);
    float s = sin(rotationAngle);
    p = vec2(p.x * c - p.y * s, p.x * s + p.y * c);
    gl_Position = vec4(p.x + offsetX, p.y + offsetY, 0.0, 1.0);

    if (colorMode == 2) {
        if (gl_VertexID == 0) vertColor = vec4(1.0, 0.0, 0.0, 1.0);
        else if (gl_VertexID == 1) vertColor = vec4(0.0, 1.0, 0.0, 1.0);
        else vertColor = vec4(0.0, 0.0, 1.0, 1.0);
    } else if (colorMode == 1) {
        vertColor = vec4(0.5, 0.0, 0.5, 1.0);
    } else {
        vertColor = vec4(1.0, 1.0, 0.0, 1.0);
    }
}
