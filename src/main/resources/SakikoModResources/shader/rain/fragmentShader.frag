uniform vec3 u_resolution;
uniform float u_time;
uniform sampler2D u_texture;
uniform sampler2D iChannel0;

float noise(in vec3 p) {
    vec3 f = fract(p);
    p = floor(p);
    f = f * f * (3.0 - 2.0 * f);
    f.xy += p.xy + p.z * vec2(37.0, 17.0);
    f.xy = texture(iChannel0, (f.xy + 0.5) / 256.0, -256.0).yx;
    return mix(f.x, f.y, f.z);
}

float fbm(in vec3 p) {
    return noise(p) + noise(p * 2.0) / 2.0 + noise(p * 4.0) / 4.0;
}

vec3 rotateZ(vec3 p, float t)
{
    float s = sin(t);
    float c = cos(t);
    mat3 m = mat3(
    c, -s, 0.0,
    s, c, 0.0,
    0.0, 0.0, 1.0
    );
    return p * m;
}

float rainLayer(in vec2 uv, in float offset, in float rotate, in vec2 grid, in float density)
{
    uv = rotateZ(uv.xyx, rotate).xy;
    uv.x += offset;
    vec2 id = vec2(ivec2(uv * grid)) + .5;
    float n1 = fbm(id.xxx);
    uv.y += u_time * (n1 + .5) * density;
    id = vec2(ivec2(uv * grid)) + .5;
    vec2 mid = abs(fract(uv * grid));
    mid.x *= 2.;
    float n = fbm(id.xyx);
    return mid.x * (1. - mid.y) * smoothstep(.3, .4, n) * smoothstep(0., .5, mid.x) * smoothstep(1., .5, mid.x);
}

void main()
{
    vec2 uv = gl_FragCoord.xy / u_resolution.xy;
    float r1 = rainLayer(uv, 23., .4, vec2(76., 6.), 2.1);
    r1 += rainLayer(uv, 38., .38, vec2(96., 6.), 2.);
    r1 += rainLayer(uv, 38.4, .38, vec2(96., 6.), 2.3) * .5;
    vec3 f = mix(vec3(.0, .0, .0) * .0, vec3(.0, .0, .0) * .0, uv.y);
    f = clamp(f, vec3(0.), vec3(1.));
    f += r1;
    gl_FragColor = vec4(f, 1.0);
}