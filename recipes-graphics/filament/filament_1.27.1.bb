SUMMARY = "Lightweight 3D Render Engine"
DESCRIPTION = "Filament is a real-time physically based rendering engine for Android, iOS, Windows, Linux, macOS, and WebGL2"
AUTHOR = "Filament Authors"
HOMEPAGE = "https://github.com/google/filament"
BUGTRACKER = "https://github.com/google/filament/issues"
SECTION = "graphics"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"


DEPENDS_class-native += "\
    compiler-rt-native \
    libcxx-native \
    "

DEPENDS_class-target += "\
    compiler-rt \
    filament-native \
    libcxx \
    virtual/egl \
    "

REQUIRED_DISTRO_FEATURES = "opengl"

PV .= "+${SRCPV}"

SRC_URI = "git://github.com/google/filament;protocol=https;branch=release \
           file://0001-Wayland-FilamentApp.patch \
           file://0002-Remove-unused-code.patch \
           file://0003-Size-callback-prototype.patch \
           file://0004-Add-tools-target.patch \
           file://ImportExecutables-Release.cmake"

SRCREV = "867d4d44f5ef2a56f663f5d8a7ba984407adfcbe"

S = "${WORKDIR}/git"

RUNTIME_class-native = "llvm"
TOOLCHAIN_class-native = "clang"
PREFERRED_PROVIDER_class-native_libgcc = "compiler-rt"

RUNTIME_class-target = "llvm"
TOOLCHAIN_class-target = "clang"
PREFERRED_PROVIDER_class-target_libgcc = "compiler-rt"


PACKAGECONFIG_class-native ??= "vulkan"
PACKAGECONFIG_class-target ??= "vulkan wayland samples"

PACKAGECONFIG[opengl] = "\
    -DFILAMENT_SUPPORTS_OPENGL=ON  -DFILAMENT_SUPPORTS_EGL_ON_LINUX=ON, \
    -DFILAMENT_SUPPORTS_OPENGL=OFF -DFILAMENT_SUPPORTS_EGL_ON_LINUX=OFF"
PACKAGECONFIG[vulkan] = "\
    -DFILAMENT_SUPPORTS_VULKAN=ON  -DFILAMENT_USE_SWIFTSHADER=OFF, \
    -DFILAMENT_SUPPORTS_VULKAN=OFF -DFILAMENT_USE_SWIFTSHADER=ON"
PACKAGECONFIG[wayland] = "\
    -DFILAMENT_SUPPORTS_WAYLAND=ON, \
    -DFILAMENT_SUPPORTS_WAYLAND=OFF, \
    wayland-native wayland wayland-protocols libxkbcommon"
PACKAGECONFIG[x11] = "\
    -DFILAMENT_SUPPORTS_XCB=ON  -DFILAMENT_SUPPORTS_XLIB=ON, \
    -DFILAMENT_SUPPORTS_XCB=OFF -DFILAMENT_SUPPORTS_XLIB=OFF, \
    libxrandr libxinerama libxi libxcursor"
PACKAGECONFIG[samples] = "\
    -DFILAMENT_SKIP_SAMPLES=OFF -DFILAMENT_SKIP_SDL2=OFF, \
    -DFILAMENT_SKIP_SAMPLES=ON  -DFILAMENT_SKIP_SDL2=ON"

inherit cmake pkgconfig features_check

EXTRA_OECMAKE_BUILD_class-native = "tools"

# tool build pass
EXTRA_OECMAKE_class-native += " \
    -D CMAKE_BUILD_TYPE=Release \
    ${PACKAGECONFIG_CONFARGS} \
    "

# target build pass
EXTRA_OECMAKE_class-target += " \
    -D CMAKE_BUILD_TYPE=Release \
    -D IMPORT_EXECUTABLES_DIR=. \
    -D DIST_ARCH=${BUILD_ARCH} \
    -D FILAMENT_HOST_TOOLS_ROOT=${STAGING_BINDIR_NATIVE} \
    ${PACKAGECONFIG_CONFARGS} \
    "

do_configure_prepend_class-target () {
    cp ${WORKDIR}/ImportExecutables-Release.cmake ${S}
}

do_install_append_class-native() {
    rm -rf ${D}${includedir}
    rm -rf ${D}${libdir}
}

do_install_append_class-target () {

    # include missing header
    mkdir -p ${D}${includedir}/utils/generic
    cp ${S}/libs/utils/include/utils/generic/Mutex.h ${D}${includedir}/utils/generic/

    # patch MaterialInstance.h to include <cstring>
    sed -i "s/#include <math\/mathfwd.h>/#include <math\/mathfwd.h>\n\n#include <cstring>/g" ${D}${includedir}/filament/MaterialInstance.h

    # static libs
    mv ${D}${libdir}/*/*.a ${D}${libdir}
    rm -rf ${D}${libdir}/${BUILD_ARCH}

    # remove cross compiled tools
    rm ${D}${bindir}/cmgen
    rm ${D}${bindir}/filamesh
    rm ${D}${bindir}/glslminifier
    rm ${D}${bindir}/matc
    rm ${D}${bindir}/matinfo
    rm ${D}${bindir}/mipgen
    rm ${D}${bindir}/normal-blending
    rm ${D}${bindir}/resgen
    rm ${D}${bindir}/roughness-prefilter
    rm ${D}${bindir}/specular-color
    rm ${D}${bindir}/uberz

    # user docs
    rm -rf ${D}/usr/docs

    # misc
    rm ${D}/usr/LICENSE
    rm ${D}/usr/README.md

    # install samples
    if ${@bb.utils.contains('PACKAGECONFIG', 'samples', 'true', 'false', d)}; then
        install -d ${D}${datadir}/filament/samples
        find ${B}/samples -executable -type f -exec cp {} ${D}${datadir}/filament/samples \;
        mv ${D}${bindir}/assets ${D}${datadir}/filament/samples/
    fi
}

FILES_${PN}-dev = "${includedir}"
FILES_${PN}-staticdev = "${libdir}"
FILES_${PN}-samples = "${datadir}"

RPROVIDES_${PN}_append = " ${PN}-samples"

BBCLASSEXTEND += "native nativesdk"
