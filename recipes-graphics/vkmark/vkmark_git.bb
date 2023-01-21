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

SRC_URI = "git://github.com/jwinarske/vkmark.git;protocol=https;branch=jw/vulkan_dep"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit meson pkgconfig features_check

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland xcb', d)}"

PACKAGECONFIG[kms] = "-Dkms=true,-Dkms=false,drm virtual/libgbm"
PACKAGECONFIG[wayland] = "-Dwayland=true,-Dwayland=false,wayland wayland-native wayland-protocols"
PACKAGECONFIG[xcb] = "-Dxcb=true,-Dxcb=false,virtual/libx11 libxcb"

EXTRA_OEMESON += "--prefix ${STAGING_DIR_TARGET}/usr"

do_install() {
    install -d ${D}${bindir}
    cp ${WORKDIR}/build/src/vkmark ${D}${bindir}

    install -d ${D}${libdir}
    cp ${WORKDIR}/build/src/wayland.so ${D}${libdir}

    install -d ${D}${datadir}/vkmark/models
    cp -r ${S}/data/models/* ${D}${datadir}/vkmark/models

    install -d ${D}${datadir}/vkmark/shaders
    cp -r ${S}/data/shaders/* ${D}${datadir}/vkmark/shaders

    install -d ${D}${datadir}/vkmark/textures
    cp -r ${S}/data/textures/* ${D}${datadir}/vkmark/textures

    rm -rf ${D}${datadir}/man
}

FILES:${PN} += "\
    ${libdir} \
    ${datadir} \
"

BBCLASSEXTEND = ""
