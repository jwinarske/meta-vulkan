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
    llvm \
    "

DEPENDS:class-target += "\
    filament-vk-native \
    vulkan-headers \
    vulkan-loader \
    "

PV .= "+${SRCPV}"

SRC_URI = "git://github.com/meta-flutter/filament;protocol=https;branch=yocto \
           file://ImportExecutables-Release.cmake.in"

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
    ${PACKAGECONFIG_CONFARGS} \
    "

do_configure:prepend:class-target () {
    cp ${WORKDIR}/ImportExecutables-Release.cmake.in ${S}/ImportExecutables-Release.cmake
    sed -i "s|@MATC_PATH@|${STAGING_BINDIR_NATIVE}/matc|g" ${S}/ImportExecutables-Release.cmake
    sed -i "s|@CMGEN_PATH@|${STAGING_BINDIR_NATIVE}/cmgen|g" ${S}/ImportExecutables-Release.cmake
    sed -i "s|@FILAMESH_PATH@|${STAGING_BINDIR_NATIVE}/filamesh|g" ${S}/ImportExecutables-Release.cmake
    sed -i "s|@MIPGEN_PATH@|${STAGING_BINDIR_NATIVE}/mipgen|g" ${S}/ImportExecutables-Release.cmake
    sed -i "s|@RESGEN_PATH@|${STAGING_BINDIR_NATIVE}/resgen|g" ${S}/ImportExecutables-Release.cmake
    sed -i "s|@GLSLMINIFIER_PATH@|${STAGING_BINDIR_NATIVE}/glslminifier|g" ${S}/ImportExecutables-Release.cmake
}

do_install:append:class-native () {
    rm -rf ${D}${libdir}
    rm -rf ${D}${includedir}
}

do_install:append:class-target () {
    install -d ${D}${libdir}/filament
    mv ${D}${libdir}/*/*.a ${D}${libdir}
    rm -rf ${D}${libdir}/x86_64
    rm ${D}/usr/LICENSE
    rm ${D}/usr/README.md
}

FILES:${PN}-staticdev += " \
    ${libdir}/filament \
    "

BBCLASSEXTEND += "native nativesdk"