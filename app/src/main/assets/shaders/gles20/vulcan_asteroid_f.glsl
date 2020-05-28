#version 100
// во фрагментном шейдере точность по умолчанию должна быть указана явно
precision lowp float;

uniform mat4 u_mvpMatrix;

varying vec4 v_commonLight;

varying vec3 v_eyeDirectModel;
varying vec3 v_normal;
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

    vec4 glassColor = textureCube(s_texture, vec3(refractCoord, 1.0));
    //outColor = glassColor + SpecularIntensity + v_commonLight;
    gl_FragColor = glassColor + v_commonLight;
    gl_FragColor.a = 0.8;

    //outColor = vec4(refractColor, 1.0) * v_commonLight;
}