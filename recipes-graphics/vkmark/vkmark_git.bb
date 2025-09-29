SUMMARY = "Vulkan Benchmark"
DESCRIPTION = "vkmark is an extensible Vulkan benchmarking suite with targeted, configurable scenes."
AUTHOR = "Collabora"
HOMEPAGE = "https://github.com/vkmark/vkmark"
BUGTRACKER = "https://github.com/vkmark/vkmark/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING-LGPL2.1;md5=4fbd65380cdd255951079008b364516c"

DEPENDS += "\
    assimp \
    compiler-rt \
    glm \
    libcxx \
    vulkan-headers \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "git://github.com/vkmark/vkmark.git;protocol=https;branch=master"

SRCREV = "83a128d65b1adddfacb4fa2e555a7e90817a7266"

inherit meson pkgconfig features_check

TOOLCHAIN = "clang"
TOOLCHAIN_NATIVE = "clang"
TC_CXX_RUNTIME = "llvm"
PREFERRED_PROVIDER_llvm = "clang"
PREFERRED_PROVIDER_llvm-native = "clang-native"
PREFERRED_PROVIDER_libgcc = "compiler-rt"
PREFERRED_PROVIDER_libgomp = "openmp"
LIBCPLUSPLUS = "-stdlib=libc++"

PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)} \
"

# resolve the most common collision automatically by preferring wayland. this
# really only removes the unnecessary runtime deps. it doesn't change linking
# options

PACKAGECONFIG:remove ??= " \
    kms \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'x11', '', d)} \
"

PACKAGECONFIG[kms] = "-Dkms=true,-Dkms=false,drm virtual/libgbm"
PACKAGECONFIG[wayland] = "-Dwayland=true,-Dwayland=false,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-Dxcb=true,-Dxcb=false,virtual/libx11 libxcb xcb-util-wm"

WAYLAND_IS_PRESENT = "${@bb.utils.filter('DISTRO_FEATURES', 'wayland', d)}"
X11_IS_PRESENT = "${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)}"

do_install() {
    install -d ${D}${datadir}
    install -d ${D}${datadir}/vkmark

    cp ${B}/src/vkmark      ${D}${datadir}/vkmark
    cp ${B}/src/headless.so ${D}${datadir}/vkmark
    cp ${B}/src/display.so  ${D}${datadir}/vkmark

    test -n "${WAYLAND_IS_PRESENT}" && cp ${B}/src/wayland.so ${D}${datadir}/vkmark
    test -n "${X11_IS_PRESENT}"     && cp ${B}/src/xcb.so     ${D}${datadir}/vkmark

    install -d ${D}${datadir}/vkmark/models
    cp -r ${S}/data/models/* ${D}${datadir}/vkmark/models

    install -d ${D}${datadir}/vkmark/shaders
    cp -r ${S}/data/shaders/* ${D}${datadir}/vkmark/shaders

    install -d ${D}${datadir}/vkmark/textures
    cp -r ${S}/data/textures/* ${D}${datadir}/vkmark/textures

    rm -rf ${D}${datadir}/man
}

FILES:${PN} += "${datadir}"

FILES:${PN}-dev = ""

BBCLASSEXTEND = ""