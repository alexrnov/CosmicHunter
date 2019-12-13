#version 100
precision lowp float;
varying vec2 v_textureCoordinates;
//varying vec4 outColor;
uniform sampler2D s_texture;

void main() {
    gl_FragColor = texture2D(s_texture, v_textureCoordinates);
}