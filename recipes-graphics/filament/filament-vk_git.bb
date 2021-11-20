SUMMARY = "Lightweight 3D Render Engine"
DESCRIPTION = "Filament is a real-time physically based rendering engine for Android, iOS, Windows, Linux, macOS, and WebGL2"
AUTHOR = "Filament Authors"
HOMEPAGE = "https://github.com/google/filament"
BUGTRACKER = "https://github.com/google/filament/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS += "\
    compiler-rt \
    libcxx \
    "

DEPENDS:class-target += "\
    filament-vk-native \
    vulkan-headers \
    vulkan-loader \
    "

PV .= "+${SRCPV}"

SRC_URI = "git://github.com/meta-flutter/filament;protocol=https;branch=yocto \
           file://ImportExecutables-Release.cmake"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
TOOLCHAIN:class-native = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[wayland] = "-DFILAMENT_SUPPORTS_WAYLAND=ON,-DFILAMENT_SUPPORTS_WAYLAND=OFF,wayland"
PACKAGECONFIG[x11] = "-DFILAMENT_SUPPORTS_X11=ON,-DFILAMENT_SUPPORTS_X11=OFF,libxcb libx11 libxrandr"

inherit cmake pkgconfig

EXTRA_OECMAKE:class-native += " \
    -D BUILD_SHARED_LIBS=OFF \
    -D FILAMENT_SUPPORTS_VULKAN=ON \
    -D FILAMENT_SUPPORTS_OPENGL=OFF \
    -D FILAMENT_BUILD_FILAMAT=OFF \
    -D CMAKE_BUILD_TYPE=Release \
    -D FILAMENT_SKIP_SAMPLES=ON \
    -D FILAMENT_SKIP_SDL2=ON \
    ${PACKAGECONFIG_CONFARGS} \
    "

EXTRA_OECMAKE:class-target += " \
    -D BUILD_SHARED_LIBS=OFF \
    -D FILAMENT_SUPPORTS_VULKAN=ON \
    -D FILAMENT_SUPPORTS_OPENGL=OFF \
    -D FILAMENT_LINUX_IS_MOBILE=ON \
    -D CMAKE_BUILD_TYPE=Release \
    -D IMPORT_EXECUTABLES_DIR=. \
    -D FILAMENT_SKIP_SDL2=ON \
    -D DIST_ARCH=${BUILD_ARCH} \
    -D FILAMENT_HOST_TOOLS_ROOT=${STAGING_BINDIR_NATIVE} \
    ${PACKAGECONFIG_CONFARGS} \
    "

do_configure:prepend:class-target () {
    cp ${WORKDIR}/ImportExecutables-Release.cmake ${S}/ImportExecutables-Release.cmake
}

do_install:append:class-native () {
    rm -rf ${D}${libdir}
    rm -rf ${D}${includedir}
}

do_install:append:class-target () {
    mv ${D}${libdir}/*/*.a ${D}${libdir}
    rm -rf ${D}${libdir}/${BUILD_ARCH}
    rm ${D}/usr/LICENSE
    rm ${D}/usr/README.md
}

FILES:${PN}-staticdev += " \
    ${libdir} \
    "

BBCLASSEXTEND += "native nativesdk"