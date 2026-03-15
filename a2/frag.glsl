#version 430

in vec2 tc;
out vec4 color;

layout (binding=0) uniform sampler2D s;
uniform int useTexture;
uniform vec4 solidColor;

void main(void) {
    if (useTexture == 1) {
        color = texture(s, tc);
    } else {
        color = solidColor;
    }
}
