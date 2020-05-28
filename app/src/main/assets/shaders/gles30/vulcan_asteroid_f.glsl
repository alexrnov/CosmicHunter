#version 300 es
// во фрагментном шейдере точность по умолчанию должна быть указана явно
precision lowp float;

uniform mat4 u_mvpMatrix;

smooth in vec4 v_commonLight;

out vec4 outColor; // вместо mediump vec4 gl_FragColor в OpenGL 2.0/GLSL 1.00
in vec3 v_eyeDirectModel;
in vec3 v_normal;
//in lowp float SpecularIntensity;

uniform samplerCube s_texture;

void main() {
    //lowp vec3 refractColor = texture(s_texture, RefractCoord).rgb;

    // Calculate refraction direction in model space
    vec3 refractDirect = refract(v_eyeDirectModel, normalize(v_normal), 0.65);
    // Project refraction
    refractDirect = (u_mvpMatrix * vec4(refractDirect, 0.0)).xyw;
    // Map refraction direction to 2d coordinates
    vec2 refractCoord = 0.5 * (refractDirect.xy / refractDirect.z) + 0.5;


    vec4 glassColor = texture(s_texture, vec3(refractCoord, 1.0));
    //outColor = glassColor + SpecularIntensity + v_commonLight;
    outColor = glassColor + v_commonLight;
    outColor.a = 0.8;
    //outColor = vec4(refractColor, 1.0) * v_commonLight;

}