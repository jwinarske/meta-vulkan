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
    libdrm \
"

RDEPENDS:${PN} += "\
    vulkan-loader \
"

SRC_URI = "\
    gitsm://swiftshader.googlesource.com/SwiftShader;protocol=https;branch=master \
    file://0001-CMake-riscv-support.patch \
"

SRCREV = "f72761e8676601271ae282f9581cde771db57a5b"

S = "${WORKDIR}/git"

inherit cmake

PACKAGECONFIG ??= "\
    d2d \
    gles2 \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11 vulkan', d)} \
    benchmarks \
    tests \
"

PACKAGECONFIG[wayland] = "-DSWIFTSHADER_BUILD_WSI_WAYLAND=ON,-DSWIFTSHADER_BUILD_WSI_WAYLAND=OFF,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-DSWIFTSHADER_BUILD_WSI_XCB=ON,-DSWIFTSHADER_BUILD_WSI_XCB=OFF,libxcb libx11 libxrandr"
PACKAGECONFIG[vulkan] = "-DSWIFTSHADER_BUILD_VULKAN=ON,-DSWIFTSHADER_BUILD_VULKAN=OFF,vulkan-headers"
PACKAGECONFIG[gles2] = "-DSWIFTSHADER_BUILD_EGL=ON -DSWIFTSHADER_BUILD_GLESv2=ON,-DSWIFTSHADER_BUILD_EGL=OFF -DSWIFTSHADER_BUILD_GLESv2=OFF,virtual/libgles2"
PACKAGECONFIG[direcfb] = "-DSWIFTSHADER_BUILD_WSI_DIRECTFB=ON,-DSWIFTSHADER_BUILD_WSI_DIRECTFB=OFF"
PACKAGECONFIG[d2d] = "-DSWIFTSHADER_BUILD_WSI_D2D=ON,-DSWIFTSHADER_BUILD_WSI_D2D=OFF"
PACKAGECONFIG[pvr_examples] = "-DSWIFTSHADER_BUILD_PVR=ON, -DSWIFTSHADER_BUILD_PVR=OFF"
PACKAGECONFIG[benchmarks] = "-DSWIFTSHADER_BUILD_BENCHMARKS=ON -DRUN_HAVE_STD_REGEX=0 -DRUN_HAVE_POSIX_REGEX=0, -DSWIFTSHADER_BUILD_BENCHMARKS=OFF"
PACKAGECONFIG[tests] = "-DSWIFTSHADER_BUILD_TESTS=ON, -DSWIFTSHADER_BUILD_TESTS=OFF"

# SWIFTSHADER_WARNINGS_AS_ERRORS=OFF for clang 13+
EXTRA_OECMAKE += " \
    -D SWIFTSHADER_GET_PVR=FALSE \
    -D SWIFTSHADER_BUILD_ANGLE=FALSE \
    -D SWIFTSHADER_USE_GROUP_SOURCES=OFF \
    -D SWIFTSHADER_WARNINGS_AS_ERRORS=OFF \
    "

do_install () {

    install -Dm 644 ${B}/Linux/libvk_swiftshader.so \
        ${D}${libdir}/libvk_swiftshader.so

    sed -i "s|./libvk_swiftshader.so|/usr/lib/libvk_swiftshader.so|g" \
        ${B}/Linux/vk_swiftshader_icd.json

    install -Dm 644 ${B}/Linux/vk_swiftshader_icd.json \
        ${D}${datadir}/vulkan/icd.d/vk_swiftshader_icd.json
}

FILES:${PN} += "\
    ${libdir} \
    ${datadir} \
"

FILES:${PN}-dev = ""

BBCLASSEXTEND = ""
