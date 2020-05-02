#version 300 es
// во фрагментном шейдере точность по умолчанию должна быть указана явно
precision lowp float;
// текстурные координаты(входной параметр из вершинного шейдера)
// фрагментный шейдер использует их для чтения из текстуры
in vec2 v_textureCoordinates; //in - вместо varying в OpenGL 2.0/GLSL 1.00
//smooth in vec4 v_eye_space_position;
smooth in float v_fog_factor;
smooth in vec4 v_commonLight;
out vec4 outColor; // вместо mediump vec4 gl_FragColor в OpenGL 2.0/GLSL 1.00
// сэмплер - специальный тип uniform-переменных, используемых для
// чтения из текстуры. В эту переменную записывается номер текстурного
// блока, к которому привязана данная текстура. Текстуры привязываются
// к текстурным блокам при помощи функции glActiveTexture
uniform sampler2D s_texture;

//const vec4 fogColor = vec4(0.7, 0.9, 0.1, 1.0);
//const vec4 fogColor = vec4(0.7, 0.1, 0.3, 1.0);
const vec4 fogColor = vec4(1.0, 1.0, 1.0, 1.0);

void main() {
    // встроенная функция texture для чтения значений из текстуры.
    // первый параметр - семплер, задающий к какому текстурному блоку
    // привязана текстура, из которой читать значения
    // второй параметр - двухмерные текстурные координаты, используемыя
    // для чтения из текстуры возвращает значение типа vec4,
    // представляющее цвет, прочитанный из текстуры.
    vec4 baseColor = texture(s_texture, v_textureCoordinates) * v_commonLight;

    outColor = mix(baseColor, fogColor, v_fog_factor);
}