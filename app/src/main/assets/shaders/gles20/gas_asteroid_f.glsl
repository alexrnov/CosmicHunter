#version 100
// во фрагментном шейдере точность по умолчанию должна быть указана явно
precision lowp float;
// текстурные координаты(входной параметр из вершинного шейдера)
// фрагментный шейдер использует их для чтения из текстуры
varying vec2 v_textureCoordinates; //in - вместо varying в OpenGL 2.0/GLSL 1.00

varying lowp float  v_DiffuseIntensity;
varying lowp float  v_SpecularIntensity;

//out vec4 outColor; // вместо mediump vec4 gl_FragColor в OpenGL 2.0/GLSL 1.00
// сэмплер - специальный тип uniform-переменных, используемых для
// чтения из текстуры. В эту переменную записывается номер текстурного
// блока, к которому привязана данная текстура. Текстуры привязываются
// к текстурным блокам при помощи функции glActiveTexture
uniform sampler2D s_texture;

const lowp vec3 defaultColor = vec3(0.1, 0.7, 0.9);

void main() {
    vec4 textureColor = texture2D(s_texture, v_textureCoordinates);
    lowp vec3 color = (defaultColor * v_DiffuseIntensity) + v_SpecularIntensity;
    gl_FragColor = vec4(color, 1.0) * textureColor;
}