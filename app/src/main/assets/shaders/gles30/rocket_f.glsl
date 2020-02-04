#version 300 es
// во фрагментном шейдере точность по умолчанию должна быть указана явно
precision lowp float;

const vec4 colorRocket = vec4(0.7, 0.1, 0.3, 0.7); // цвет и прозрачность ракеты
smooth in vec4 v_commonLight; // общее освещение
out vec4 outColor; // вместо mediump vec4 gl_FragColor в OpenGL 2.0/GLSL 1.00

void main() {
    outColor = colorRocket * v_commonLight;
}