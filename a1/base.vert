#version 430

uniform float offset;

void main(void) {
    const vec4 vertices[3] = vec4[3](
        vec4( 0.0+offset,  0.5, 0.0, 1.0),
        vec4(-0.1+offset, -0.5, 0.0, 1.0),
        vec4( 0.1+offset, -0.5, 0.0, 1.0)
    );
    gl_Position = vertices[gl_VertexID];
}
