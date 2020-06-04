#version 300 es
// во фрагментном шейдере точность по умолчанию должна быть указана явно
precision lowp float;
// текстурные координаты(входной параметр из вершинного шейдера)
// фрагментный шейдер использует их для чтения из текстуры
in vec2 v_textureCoordinates; //in - вместо varying в OpenGL 2.0/GLSL 1.00

in float CosViewAngle;
in float LightIntensity;
in vec4 v_ResultColor;

in float v_Intensity;

smooth in vec4 v_commonLight;


in lowp float  v_DiffuseIntensity;
in lowp float  v_SpecularIntensity;


out vec4 outColor; // вместо mediump vec4 gl_FragColor в OpenGL 2.0/GLSL 1.00
// сэмплер - специальный тип uniform-переменных, используемых для
// чтения из текстуры. В эту переменную записывается номер текстурного
// блока, к которому привязана данная текстура. Текстуры привязываются
// к текстурным блокам при помощи функции glActiveTexture
uniform sampler2D s_texture;




// wave numbers for the iridescence effect: k = 2.0 * pi / wavelength (nm).
const mediump float PI = 3.141592654;
const mediump vec3 rgbK = 2.0 * PI * vec3(1.0/475.0, 1.0/510.0, 1.0/650.0);

//const mediump float iridescence = 4.4;
//const mediump float minThickness = 80.0;
//const mediump float maxVariation = 50.0;
const mediump float iridescence = 7.0;
const mediump float minThickness = 50.0;
const mediump float maxVariation = 34.0;

const lowp vec3 defaultColor = vec3(0.1, 0.7, 0.9);
const lowp vec3  cBaseColor = vec3(0.9, 0.5, 0.1);
void main() {
    // встроенная функция texture для чтения значений из текстуры.
    // первый параметр - семплер, задающий к какому текстурному блоку
    // привязана текстура, из которой читать значения
    // второй параметр - двухмерные текстурные координаты, используемыя
    // для чтения из текстуры возвращает значение типа vec4,
    // представляющее цвет, прочитанный из текстуры.
    //outColor = texture(s_texture, v_textureCoordinates) * v_commonLight;
    mediump float thickness = texture(s_texture, v_textureCoordinates).r
    * maxVariation + minThickness;
    mediump float delta = (thickness / LightIntensity) + (thickness / CosViewAngle);
    lowp vec3 color = cos(delta * rgbK) * iridescence * LightIntensity;

    vec4 texturColor = texture(s_texture, v_textureCoordinates);

    vec4 resultColor = vec4(color, 1.0);

    if (delta > 750.0 || delta < 370.0) {
        resultColor = texture(s_texture, v_textureCoordinates) * v_commonLight;
    }

    outColor = resultColor;
    outColor.a = 0.3;
}