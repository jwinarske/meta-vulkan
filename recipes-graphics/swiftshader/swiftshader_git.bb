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
    file://0002-marl-cmake-3.5.patch \
"

SRC_URI:riscv64 = "\
    file://0001-CMake-riscv-support.patch \
"

SRCREV = "f72761e8676601271ae282f9581cde771db57a5b"

inherit cmake

PACKAGECONFIG ??= "\
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)} \
    atsc \
"

PACKAGECONFIG[wayland] = "-DSWIFTSHADER_BUILD_WSI_WAYLAND=ON,-DSWIFTSHADER_BUILD_WSI_WAYLAND=OFF,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-DSWIFTSHADER_BUILD_WSI_XCB=ON,-DSWIFTSHADER_BUILD_WSI_XCB=OFF,libxcb libx11 libxrandr"
PACKAGECONFIG[direcfb] = "-DSWIFTSHADER_BUILD_WSI_DIRECTFB=ON,-DSWIFTSHADER_BUILD_WSI_DIRECTFB=OFF"
PACKAGECONFIG[d2d] = "-DSWIFTSHADER_BUILD_WSI_D2D=ON,-DSWIFTSHADER_BUILD_WSI_D2D=OFF"

PACKAGECONFIG[pvr_examples] = "-DSWIFTSHADER_BUILD_PVR=ON, -DSWIFTSHADER_BUILD_PVR=OFF"
PACKAGECONFIG[benchmarks] = "-DSWIFTSHADER_BUILD_BENCHMARKS=ON -DRUN_HAVE_STD_REGEX=0 -DRUN_HAVE_POSIX_REGEX=0, -DSWIFTSHADER_BUILD_BENCHMARKS=OFF"

# tests are not supported on aarch64; due to subzero
PACKAGECONFIG[tests] = "-DSWIFTSHADER_BUILD_TESTS=ON, -DSWIFTSHADER_BUILD_TESTS=OFF"

PACKAGECONFIG[msan] = "-DSWIFTSHADER_MSAN=ON, -DSWIFTSHADER_MSAN=OFF"
PACKAGECONFIG[msan] = "-DSWIFTSHADER_ASAN=ON, -DSWIFTSHADER_ASAN=OFF"
PACKAGECONFIG[tsan] = "-DSWIFTSHADER_TSAN=ON, -DSWIFTSHADER_TSAN=OFF"
PACKAGECONFIG[ubsan] = "-DSWIFTSHADER_UBSAN=ON, -DSWIFTSHADER_UBSAN=OFF"
PACKAGECONFIG[emit-coverage] = "-DSWIFTSHADER_EMIT_COVERAGE=ON, -DSWIFTSHADER_EMIT_COVERAGE=OFF"
PACKAGECONFIG[dcheck] = "-DSWIFTSHADER_DCHECK_ALWAYS_ON=ON, -DSWIFTSHADER_DCHECK_ALWAYS_ON=OFF"

PACKAGECONFIG[reactor-emit-debug-info] = "-DREACTOR_EMIT_DEBUG_INFO=ON, -DREACTOR_EMIT_DEBUG_INFO=OFF"
PACKAGECONFIG[reactor-emit-print-location] = "-DREACTOR_EMIT_PRINT_LOCATION=ON, -DREACTOR_EMIT_PRINT_LOCATION=OFF"
PACKAGECONFIG[reactor-emit-asm-file] = "-DREACTOR_EMIT_ASM_FILE=ON, -DREACTOR_EMIT_ASM_FILE=OFF"
PACKAGECONFIG[reactor-enable-print] = "-DREACTOR_ENABLE_PRINT=ON, -DREACTOR_ENABLE_PRINT=OFF"
PACKAGECONFIG[reactor-verify-llvm-ir] = "-DREACTOR_VERIFY_LLVM_IR=ON, -DREACTOR_VERIFY_LLVM_IR=OFF"

PACKAGECONFIG[less-debug-info] = "-DSWIFTSHADER_LESS_DEBUG_INFO=ON, -DSWIFTSHADER_LESS_DEBUG_INFO=OFF"

# option_if_not_defined(SWIFTSHADER_ENABLE_VULKAN_DEBUGGER "Enable Vulkan debugger support" FALSE)  # TODO(b/251802301)
PACKAGECONFIG[atsc] = "-DSWIFTSHADER_ENABLE_ASTC=ON, -DSWIFTSHADER_ENABLE_ASTC=OFF"

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

INSANE_SKIP:${PN} = "already-stripped"

FILES:${PN} += "\
    ${libdir} \
    ${datadir} \
"

FILES:${PN}-dev = ""

BBCLASSEXTEND = ""
