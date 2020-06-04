#version 100
// во фрагментном шейдере точность по умолчанию должна быть указана явно
precision lowp float;
// текстурные координаты(входной параметр из вершинного шейдера)
// фрагментный шейдер использует их для чтения из текстуры
varying vec2 v_textureCoordinates; //in - вместо varying в OpenGL 2.0/GLSL 1.00

varying float v_CosViewAngle;
varying float v_LightIntensity;

varying vec4 v_commonLight;

//out vec4 outColor; // вместо mediump vec4 gl_FragColor в OpenGL 2.0/GLSL 1.00
uniform sampler2D s_texture;

// wave numbers for the iridescence effect: k = 2.0 * pi / wavelength (nm).
const mediump float PI = 3.141592654;
const mediump vec3 rgbK = 2.0 * PI * vec3(1.0/475.0, 1.0/510.0, 1.0/650.0);

const mediump float iridescence = 7.0;
const mediump float minThickness = 50.0;
const mediump float maxVariation = 34.0;
const lowp float beginWave = 750.0;
const lowp float endWave = 370.0;

const lowp float c_alpha = 0.3;

void main() {
    mediump float thickness = texture2D(s_texture, v_textureCoordinates).r * maxVariation + minThickness;
    mediump float delta = (thickness / v_LightIntensity) + (thickness / v_CosViewAngle);
    lowp vec3 color = cos(delta * rgbK) * iridescence * v_LightIntensity;

    vec4 resultColor;
    if (delta > beginWave || delta < endWave) {
        resultColor = texture2D(s_texture, v_textureCoordinates) * v_commonLight;
    } else {
        resultColor = vec4(color, 1.0);
    }
    gl_FragColor = resultColor;
    gl_FragColor.a = c_alpha;
}