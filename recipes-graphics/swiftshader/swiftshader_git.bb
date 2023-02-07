SUMMARY = "SwiftShader"
DESCRIPTION = "SwiftShader is a high-performance CPU-based implementation \
               of the Vulkan 1.3 graphics API. Its goal is to provide hardware \
               independence for advanced 3D graphics."
AUTHOR = "Google"
HOMEPAGE = "https://swiftshader.googlesource.com/SwiftShader"
BUGTRACKER = "https://g.co/swiftshaderbugs"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d273d63619c9aeaf15cdaf76422c4f87"

DEPENDS += "\
    compiler-rt \
    libcxx \
    libdrm \
"

RDEPENDS:${PN} += "\
    vulkan-loader \
"

SRC_URI = "git://swiftshader.googlesource.com/SwiftShader;protocol=https;branch=master"

SRCREV = "938d3a1fac4deda77efb1c22c5e080ee4686eb0a"

S = "${WORKDIR}/git"

inherit cmake

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"

PACKAGECONFIG ??= "\
    d2d gles2 \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11 vulkan', d)} \
"

PACKAGECONFIG[wayland] = "-DSWIFTSHADER_BUILD_WSI_WAYLAND=ON,-DSWIFTSHADER_BUILD_WSI_WAYLAND=OFF,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = ",,libxcb libx11 libxrandr"
PACKAGECONFIG[vulkan] = "-DSWIFTSHADER_BUILD_VULKAN=ON,,vulkan-headers"
PACKAGECONFIG[gles2] = "-DSWIFTSHADER_BUILD_EGL=ON -DSWIFTSHADER_BUILD_GLESv2=ON,,virtual/libgles2"
PACKAGECONFIG[d2d] = "-DSWIFTSHADER_BUILD_WSI_D2D=ON,-DSWIFTSHADER_BUILD_WSI_D2D=OFF"

# SWIFTSHADER_WARNINGS_AS_ERRORS=OFF for clang 13
EXTRA_OECMAKE += " \
    -D SWIFTSHADER_BUILD_PVR=FALSE \
    -D SWIFTSHADER_GET_PVR=FALSE \
    -D SWIFTSHADER_BUILD_ANGLE=FALSE \
    -D SWIFTSHADER_BUILD_TESTS=OFF \
    -D SWIFTSHADER_BUILD_BENCHMARKS=OFF \
    -D SWIFTSHADER_USE_GROUP_SOURCES=OFF \
    -D SWIFTSHADER_WARNINGS_AS_ERRORS=OFF \
    "

do_install () {

    install -Dm 644 ${WORKDIR}/build/Linux/libvk_swiftshader.so \
        ${D}${libdir}/libvk_swiftshader.so

    sed -i "s|./libvk_swiftshader.so|/usr/lib/libvk_swiftshader.so|g" \
        ${WORKDIR}/build/Linux/vk_swiftshader_icd.json

    install -Dm 644 ${WORKDIR}/build/Linux/vk_swiftshader_icd.json \
        ${D}${datadir}/vulkan/icd.d/vk_swiftshader_icd.json
}

FILES:${PN} += "\
    ${libdir} \
    ${datadir} \
"

FILES:${PN}-dev = ""

BBCLASSEXTEND = ""
