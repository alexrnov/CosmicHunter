#version 300 es
// точность вычислений по умолчанию. Если в вершинном шейдере не задана
// точность по умолчанию, то точность по умолчанию для float и int будет highp
precision lowp float; // низкая точность для всех переменных, основанных на типе float
uniform mat4 u_mvpMatrix; // модельно-видо-проекционная матрица
uniform mat4 u_mvMatrix; // модельно-видовая матрица
uniform mat4 u_vMatrix;
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

//smooth out vec4 v_eye_space_position;
smooth out float v_fog_factor;
// smooth - описатель интерполяции. Smooth(линейная интерполяция вдоль примитива)
// - используется по умолчанию. Другие возможные варианты flat(плоское закрашивние)
// и centroid(интерполяция внутри примитива)
smooth out vec4 v_commonLight; //интерполятор для общего освещения(фоновое + диффузное)

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

const vec3 lightDirection = vec3(0.7, 0.0, -1.0); // вектор направленного освещения

const float startFog = 10.0;
const float endFog = 140.0;
const vec4 eye = vec4(0.0, 0.0, 0.0, 0.0);
float getFogFactor(float fogCoord)
{
    //float factor = exp(-0.005*fogCoord); // экспоненциальный туман exp
    //float factor = exp(-pow(0.01*fogCoord, 2.0)); // экспоненциальный туман exp2
    float factor = (endFog - fogCoord) / (endFog - startFog); // линейный туман
    factor = 1.0 - clamp(factor, 0.0, 1.0);
    return factor;
}

void main() {
    // calculate eye space position of every vertex
    vec4 v_eye_space_position = u_mvMatrix * a_position;
    // obtain cartesian coordinate z
    float fogCoord = abs(v_eye_space_position.z / v_eye_space_position.w);

    float v_eyeDistance = distance(v_eye_space_position.xyz, eye.xyz);

    v_fog_factor = getFogFactor(v_eyeDistance);
    // расчитать итоговый цвет для внешнего освещение
    lowp vec3 ambientColor = u_ambientLight.color * u_ambientLight.intensity;
    // преобразовать ориентацию нормали в пространство глаза
    vec3 modelViewNormal = vec3(u_mvMatrix * vec4(a_normal, 0.0));
    float diffuse = max(-dot(modelViewNormal, lightDirection), 0.0);
    // расчитать итоговый цвет для диффузного освещения
    lowp vec3 diffuseColor = diffuse * u_diffuseLight.color * u_diffuseLight.intensity;

    v_commonLight = vec4((ambientColor + diffuseColor), 1.0);
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