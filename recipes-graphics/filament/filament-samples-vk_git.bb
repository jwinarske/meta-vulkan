SUMMARY = "Lightweight 3D Render Engine Samples"
DESCRIPTION = "Filament is a real-time physically based rendering engine for Android, iOS, Windows, Linux, macOS, and WebGL2"
AUTHOR = "Filament Authors"
HOMEPAGE = "https://github.com/jwinarske/filament-samples"
BUGTRACKER = "https://github.com/jwinarske/filament-samples/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS += "\
    assimp \
    compiler-rt \
    filament-vk \
    filament-vk-native \
    libcxx \
    libpng \
    libsdl2 \
    libxkbcommon \
    virtual/egl \
    vulkan-loader \
    wayland \
    wayland-native \
    "

PV .= "+${SRCPV}"

SRC_URI = "git://github.com/jwinarske/filament-samples;protocol=https;branch=main"

S = "${WORKDIR}/git"

SRCREV = "${AUTOREV}"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[wayland] = "-DFILAMENT_SUPPORTS_WAYLAND=ON,-DFILAMENT_SUPPORTS_WAYLAND=OFF,wayland"
PACKAGECONFIG[x11] = "-DFILAMENT_SUPPORTS_X11=ON,-DFILAMENT_SUPPORTS_X11=OFF,libxcb libx11 libxrandr"

inherit cmake pkgconfig

EXTRA_OECMAKE += " \
    -D BUILD_SHARED_LIBS=OFF \
    -D CMAKE_BUILD_TYPE=Release \
    -D FILAMENT_SUPPORTS_VULKAN=ON \
    -D FILAMENT_SUPPORTS_OPENGL=OFF \
    -D FILAMENT_LINUX_IS_MOBILE=ON \
    -D FILAMENT_USE_SYSTEM_SDL2=ON \
    -D FILAMENT_SKIP_TESTS=ON \
    -D FILAMENT_HOST_TOOLS_ROOT=${STAGING_BINDIR_NATIVE} \
    -D IMPORT_EXECUTABLES=${S}/cmake/ImportExecutables-Release.cmake \
    -D DIST_ARCH=${BUILD_ARCH} \
    "

do_install:append() {
    rm -rf ${D}${libdir}
    rm -rf ${D}${includedir}
}

BBCLASSEXTEND = ""
