#version 300 es
// во фрагментном шейдере точность по умолчанию должна быть указана явно
precision lowp float;

smooth in vec4 v_commonLight;
in mediump vec2 RefractCoord;

out vec4 outColor; // вместо mediump vec4 gl_FragColor в OpenGL 2.0/GLSL 1.00
uniform samplerCube s_texture;

void main() {
    //lowp vec3 refractColor = texture(s_texture, RefractCoord).rgb;

    vec4 glassColor = texture(s_texture, vec3(RefractCoord, 1.0));
    outColor = glassColor;

    //outColor = vec4(refractColor, 1.0) * v_commonLight;

}