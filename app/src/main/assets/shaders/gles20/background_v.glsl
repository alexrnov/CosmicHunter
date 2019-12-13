#version 100
precision lowp float; // низкая точность для всех переменных, основанных на типе float
uniform mat4 u_mvpMatrix; // модельно-видо-проекционная матрица

attribute vec4 a_position; // сюда загружаются данные вершин
attribute vec2 a_textureCoordinates; // сюда загружаются двухкомпонентные текстурные координаты

varying vec2 v_textureCoordinates; //out - вместо varying в OpenGL 2.0/GLSL 1.00

void main() {
    v_textureCoordinates = a_textureCoordinates;
    gl_Position = u_mvpMatrix * a_position;
}