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
in vec3 a_normal; // сюда загружаются нормали

// smooth - описатель интерполяции. Smooth(линейная интерполяция вдоль примитива)
// - используется по умолчанию. Другие возможные варианты flat(плоское закрашивние)
// и centroid(интерполяция внутри примитива)
smooth out vec4 v_commonLight; //интерполятор для общего освещения(фоновое + диффузное)

out vec3 v_normal;
out vec3 v_eyeDirectModel;
//out lowp float SpecularIntensity;

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
const mediump float eta = 0.1;

/*
float getSpecularIntensity(vec4 position, vec3 a_normal, vec3 eyeDirectModel) {
    float shininess = 100.0;
    vec3 lightPosition = vec3(-20.0, 0.0, 0.0);
    // We ignore that N dot L could be negative (light coming from behind the surface)
    mediump vec3 LightDirModel = normalize(lightPosition - position.xyz);
    mediump vec3 halfVector = normalize(LightDirModel + eyeDirectModel);
    lowp float NdotH = max(dot(a_normal, halfVector), 0.0);
    return pow(NdotH, shininess);
}
*/
void main() {
    v_normal = a_normal;

    vec4 eyePositionModel = u_mvMatrix * a_position;
    // Eye direction in model space (// eye coord)
    vec3 eyeDirectModel = normalize(- eyePositionModel.xyz);
    v_eyeDirectModel = eyeDirectModel;

    // specular lighting
    //SpecularIntensity = getSpecularIntensity(a_position, a_normal, eyeDirectModel);

    // расчитать итоговый цвет для внешнего освещение
    lowp vec3 ambientColor = u_ambientLight.color * u_ambientLight.intensity;
    // преобразовать ориентацию нормали в пространство глаза
    vec3 modelViewNormal = vec3(u_mvMatrix * vec4(a_normal, 0.0));

    vec3 lightVector = normalize(lightPosition - vec3(eyePositionModel));
    lightVector = mat3(u_pointViewMatrix) * lightVector;
    float diffuse = max(dot(modelViewNormal, lightVector), 0.0);

    // расчитать итоговый цвет для диффузного освещения
    lowp vec3 diffuseColor = diffuse * u_diffuseLight.color * u_diffuseLight.intensity;

    v_commonLight = vec4((ambientColor + diffuseColor), 1.0);
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