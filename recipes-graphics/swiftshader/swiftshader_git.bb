SUMMARY = "SwiftShader"
DESCRIPTION = "SwiftShader is a high-performance CPU-based implementation \
               of the Vulkan graphics API12. Its goal is to provide hardware \
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
    vulkan-headers \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "git://swiftshader.googlesource.com/SwiftShader;protocol=https"

SRCREV = "533f38d43254240286b3abf89d99ec5734027dab"

S = "${WORKDIR}/git"

inherit cmake features_check

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[wayland] = "-DSWIFTSHADER_BUILD_WSI_WAYLAND=ON,-DSWIFTSHADER_BUILD_WSI_WAYLAND=OFF,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = ",,libxcb libx11 libxrandr"

# SWIFTSHADER_WARNINGS_AS_ERRORS=OFF for clang 13
EXTRA_OECMAKE += " \
    -D SWIFTSHADER_BUILD_EGL=OFF \
    -D SWIFTSHADER_BUILD_GLESv2=OFF \
    -D SWIFTSHADER_BUILD_VULKAN=TRUE \
    -D SWIFTSHADER_BUILD_WSI_D2D=ON \
    -D SWIFTSHADER_BUILD_PVR=FALSE \
    -D SWIFTSHADER_GET_PVR=FALSE \
    -D SWIFTSHADER_BUILD_ANGLE=FALSE \
    -D SWIFTSHADER_BUILD_TESTS=OFF \
    -D SWIFTSHADER_BUILD_BENCHMARKS=OFF \
    -D SWIFTSHADER_USE_GROUP_SOURCES=OFF \
    -D SWIFTSHADER_WARNINGS_AS_ERRORS=OFF \
    "

do_install () {

    install -d ${D}${libdir}
    cp ${WORKDIR}/build/Linux/libvk_swiftshader.so ${D}${libdir}

    install -d ${D}${datadir}/vulkan/icd.d
    cp ${WORKDIR}/build/Linux/vk_swiftshader_icd.json ${D}${datadir}/vulkan/icd.d

    sed -i "s|./libvk_swiftshader.so|/usr/lib/libvk_swiftshader.so|g" ${D}${datadir}/vulkan/icd.d/vk_swiftshader_icd.json
}

FILES:${PN} = "\
    ${libdir} \
    ${datadir} \
    "

FILES:${PN}-dev = ""

BBCLASSEXTEND = ""
