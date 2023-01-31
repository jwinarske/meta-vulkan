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
    vulkan-loader \
    wayland \
    wayland-native \
    "

REQUIRED_DISTRO_FEATURES = "vulkan"

S = "${WORKDIR}/git"

SRCREV = "2eb0dd7153175c8d82957b37946b6181d037f110"

PV .= "+${SRCPV}"

SRC_URI = "\
    gitsm://github.com/google/filament.git;protocol=https;branch=main \
    file://0001-Yocto-Patches.patch \
"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[wayland] = "-DFILAMENT_SUPPORTS_WAYLAND=ON,-DFILAMENT_SUPPORTS_WAYLAND=OFF,wayland"
PACKAGECONFIG[x11] = "-DFILAMENT_SUPPORTS_X11=ON,-DFILAMENT_SUPPORTS_X11=OFF,libxcb libx11 libxrandr"

inherit cmake pkgconfig features_check

EXTRA_OECMAKE += " \
    -D BUILD_SHARED_LIBS=OFF \
    -D CMAKE_BUILD_TYPE=Release \
    -D FILAMENT_SUPPORTS_VULKAN=ON \
    -D FILAMENT_SUPPORTS_OPENGL=OFF \
    -D FILAMENT_LINUX_IS_MOBILE=ON \
    -D FILAMENT_USE_SYSTEM_SDL2=ON \
    -D FILAMENT_SKIP_SDL2=OFF \
    -D FILAMENT_SKIP_TESTS=ON \
    -D DIST_ARCH=${BUILD_ARCH} \
    -D FILAMENT_HOST_TOOLS_ROOT=${STAGING_BINDIR_NATIVE} \
    -D IMPORT_EXECUTABLES_DIR=../recipe-sysroot-native/usr/include/cmake/filament \
    "

do_install:append() {
    rm -rf ${D}${libdir}
    rm -rf ${D}${includedir}

    install -d "${D}${datadir}/filament"

    mv "${D}/usr/LICENSE" "${D}${datadir}/filament/LICENSE"
    mv "${D}/usr/README.md" "${D}${datadir}/filament/README.md"

    install -d ${D}${bindir}

    install -m 755 ${B}/filament/backend/compute_test ${D}${bindir}/
    install -m 755 ${B}/filament/benchmark/benchmark_filament ${D}${bindir}/
    install -m 755 ${B}/filament/test/test_material_parser ${D}${bindir}/
    install -m 755 ${B}/libs/bluevk/test_bluevk ${D}${bindir}/
    install -m 755 ${B}/libs/camutils/test_camutils ${D}${bindir}/
    install -m 755 ${B}/libs/filamat/test_filamat ${D}${bindir}/
    install -m 755 ${B}/libs/filamat/test_filamat_lite ${D}${bindir}/
    install -m 755 ${B}/libs/geometry/test_transcoder ${D}${bindir}/
    install -m 755 ${B}/libs/ktxreader/test_ktxreader ${D}${bindir}/
    install -m 755 ${B}/libs/math/test_math ${D}${bindir}/
    install -m 755 ${B}/libs/utils/test_utils ${D}${bindir}/
    install -m 755 ${B}/libs/filameshio/test_filameshio ${D}${bindir}/
    install -m 755 ${B}/libs/viewer/test_settings ${D}${bindir}/
}

FILES:${PN} += "${datadir}"

BBCLASSEXTEND = ""
