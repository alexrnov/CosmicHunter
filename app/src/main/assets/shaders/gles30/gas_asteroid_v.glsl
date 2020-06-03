#version 300 es
// точность вычислений по умолчанию. Если в вершинном шейдере не задана
// точность по умолчанию, то точность по умолчанию для float и int будет highp
precision lowp float; // низкая точность для всех переменных, основанных на типе float
uniform mat4 u_mvpMatrix; // модельно-видо-проекционная матрица
uniform mat4 u_mvMatrix; // модельно-видовая матрица
uniform mat4 u_pointViewMatrix; // матрица вида для точки обзора
// атрибуты(переменные вершин) принимают значения, задаваемые для выводимых
// вершин. Обычно атрибуты хранят такие данные, как положение, нормаль,
// текстурные координаты и цвета. Описатель layout в начале используется
// для задания индекса соответствующего атрибута. in вместо attribute в OpenGL 2.0/GLSL 1.00
// layout(location = 0) in vec4 a_position; // сюда загружаются данные вершин
// layout(location = 1) in vec2 a_textureCoordinates; // сюда загружаются двухкомпонентные текстурные координаты
// layout(location = 2) in vec3 a_normal; // сюда загружаются нормали
in vec4 a_position; // сюда загружаются данные вершин
in vec2 a_textureCoordinates; // сюда загружаются двухкомпонентные текстурные координаты
in vec3 a_normal; // сюда загружаются нормали

// выходные переменные вершинного шейдера описываются ключевым словом out
// эти переменные будут также описаны во фрагментном шейдере с помощью
// ключевого слова in(и теми же типами) и будут линейно проинтерполированы
// вдоль примитива во время растеризации. Для выходных переменных вершинного
// шейдера/входных переменных фрагментного шейдера не могут иметь описателей
// размещения(layout)
out vec2 v_textureCoordinates; //out - вместо varying в OpenGL 2.0/GLSL 1.00

out lowp float  v_DiffuseIntensity;
out lowp float  v_SpecularIntensity;

struct AmbientLight { // структура для внешнего освещения
    vec3 color; // цвет внешнего освещения
    float intensity; // интенсивность внешнего освещения
};

struct DiffuseLight { // структура для диффузного освещения
    vec3 color; // цвет внешнего освещения
    float intensity; // интенсивность диффузного освещения
};

uniform AmbientLight u_ambientLight; // переменная для внешнего освещения
uniform DiffuseLight u_diffuseLight; // переменная для диффузного освещения

const vec3 lightPosition = vec3(-5.0, 0.0, 1.0); // позиция источника света


const mediump float PI = 3.141592654;
const mediump vec3 rgbK = 2.0 * PI * vec3(1.0/475.0, 1.0/510.0, 1.0/650.0);

const mediump float iridescence = 7.4;
const mediump float minThickness = 50.0;
const mediump float maxVariation = 50.0;

const lowp vec3  cGrainDirection = vec3(10.0, 0.9, -1.0);
void main() {
    // расчитать итоговый цвет для внешнего освещение
    lowp vec3 ambientColor = u_ambientLight.color * u_ambientLight.intensity;
    // преобразовать ориентацию нормали в пространство глаза
    vec3 modelViewNormal = vec3(u_mvMatrix * vec4(a_normal, 0.0));
    vec3 modelViewVertex = vec3(u_mvMatrix * a_position); // eye coord

    mediump vec3 eyeDirection = normalize(-modelViewVertex);

    vec3 lightVector = normalize(lightPosition - modelViewVertex);
    lightVector = mat3(u_pointViewMatrix) * lightVector;

    mediump vec3 normalXgrain = cross(modelViewNormal, cGrainDirection);
    mediump vec3 tangent = normalize(cross(normalXgrain, modelViewNormal));
    mediump float LdotT = dot(tangent, normalize(lightVector));
    mediump float VdotT = dot(tangent, normalize(vec3(0.0, 0.0, 0.0)));
    mediump float NdotL = sqrt(1.0 - pow(LdotT, 2.0));
    mediump float VdotR = NdotL * sqrt(1.0 - pow(VdotT, 2.0)) - VdotT * LdotT;

    v_DiffuseIntensity = max(NdotL * 0.4 + 0.6, 0.0);
    v_SpecularIntensity = max(pow(VdotR, 2.0) * 0.9, 0.0);

    v_textureCoordinates = a_textureCoordinates;
    gl_Position = u_mvpMatrix * a_position;
}
//Фактически любой параметр в шейдере, который остается неизменным
//для всех вершин или фрагментов, должен быть передан как uniform-переменная.
//Пространство имен для uniform-переменных является общим для вершинного
//и фрагментного шейдеров. То есть если вершинный и фрагментный шейдеры
//собираются вместе в программу, то они обладают общим набором
//uniform-переменных. Переменные, чье значение известно во время компиляции
//должны быть константами, а не uniform-переменными

//gl_Position - встроенная специальная переменная, которая
//используется для вывода координат вершины в пространстве
//отсечения. Ее значения используются на шагах отсечения и области
//видимости для выполнения надлежащего отсечения примитивов и
//преобразования координат из пространства отсечения в пространство
//экрана. Значение gl_Position не определено до тех пор, пока вершинный
//шейдер не произведет запись в эту переменную. Эта переменная использует
//значение с плавающей точкой с описателем точности highp