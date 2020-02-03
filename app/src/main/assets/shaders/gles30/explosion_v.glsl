#version 300 es
uniform float u_lastTimeExplosion; // время, прошедшее с момента взрыва.
// центр взрыва (координаты частиц заданы относительно этого центра.
uniform vec3 u_centerPosition;
uniform float u_sizeSprite;
// переменные a_lifetime, a_startPosition и a_endPosition получают
// случайные значения в конструкторе класса.
//layout(location = 0) in float a_lifeTime; // время жизни частицы в секундах
//layout(location = 1) in vec3 a_startPosition; // начальное положение частицы в момент взрыва
//layout(location = 2) in vec3 a_endPosition; // конечное положение частицы
in float a_lifeTime; // время жизни частицы в секундах
in vec3 a_startPosition; // начальное положение частицы в момент взрыва
in vec3 a_endPosition; // конечное положение частицы

// оставшееся время жизни частицы (передается во фрагментный шейдер)
// это значение будет использоваться фрагментным шейдером, для того
// чтобы увеличивать прозрачность частицы по мере того, как ее время
// жизни подходит к концу
out float v_lifeTime;
//const float c_sizeSprite = 80.0; // размер спрайта
void main()
{
    // вычислить положение частицы с помощью линейной
    // интерполяции начального и конечного положений (создается анимация)
    gl_Position.xyz = a_startPosition + (u_lastTimeExplosion * a_endPosition);
    gl_Position.xyz += u_centerPosition;
    gl_Position.w = 1.0;
    // вычислить оставшееся время жизни частицы
    v_lifeTime = 1.0 - (u_lastTimeExplosion / a_lifeTime);
    v_lifeTime = clamp(v_lifeTime, 0.0, 1.0);
    // вычислить размер спрайта, исходя из оставшегося времени
    // жизни, и записать его во встроенную переменную gl_PointSize.
    // Это ведет к уменьшению размера частицы по мере того, как
    // время ее жизни заканчивается
    gl_PointSize = pow(v_lifeTime, 5.0) * u_sizeSprite;
}