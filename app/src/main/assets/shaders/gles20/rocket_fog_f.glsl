#version 100
// во фрагментном шейдере точность по умолчанию должна быть указана явно
precision lowp float;
const vec4 colorRocket = vec4(0.7, 0.1, 0.3, 0.7); // цвет и прозрачность ракеты
varying vec4 v_commonLight;
varying float v_fog_factor;
//out vec4 outColor; // вместо mediump vec4 gl_FragColor в OpenGL 2.0/GLSL 1.00
//mediump vec4 gl_FragColor;
const vec4 fogColor = vec4(1.0, 1.0, 1.0, 1.0);
void main() {

    vec4 baseColor = colorRocket * v_commonLight;
    gl_FragColor = mix(baseColor, fogColor, v_fog_factor);

    // встроенная функция texture для чтения значений из текстуры.
    // первый параметр - семплер, задающий к какому текстурному блоку
    // привязана текстура, из которой читать значения
    // второй параметр - двухмерные текстурные координаты, используемые
    // для чтения из текстуры возвращает значение типа vec4,
    // представляющее цвет, прочитанный из текстуры.
    //gl_FragColor = colorRocket * v_commonLight;
}