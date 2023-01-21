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
    glm \
    vulkan-headers \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

S = "${WORKDIR}/git"

SRCREV = "30d2cd37f0566589d90914501fc7c51a4e51f559"

SRC_URI = "\
    git://github.com/vkmark/vkmark.git;protocol=https;branch=master \
    file://0001-vulkan_hpp-crosscompile.patch \
"

inherit meson features_check pkgconfig

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland xcb', d)}"

PACKAGECONFIG[kms] = "-Dkms=true,-Dkms=false,drm virtual/libgbm"
PACKAGECONFIG[wayland] = "-Dwayland=true,-Dwayland=false,wayland wayland-native wayland-protocols"
PACKAGECONFIG[xcb] = "-Dxcb=true,-Dxcb=false,virtual/libx11 libxcb"

EXTRA_OEMESON += "--buildtype release --prefix ${STAGING_DIR_TARGET}/usr"

do_install() {
    install -d ${D}${datadir}/vkmark/models
    install -d ${D}${datadir}/vkmark/shaders
    install -d ${D}${datadir}/vkmark/textures

    cp ${WORKDIR}/build/src/vkmark ${D}${datadir}/vkmark/
    cp ${WORKDIR}/build/src/wayland.so ${D}${datadir}/vkmark/

    cp -r ${S}/data/models/* ${D}${datadir}/vkmark/models
    cp -r ${S}/data/shaders/* ${D}${datadir}/vkmark/shaders
    cp -r ${S}/data/textures/* ${D}${datadir}/vkmark/textures

    rm -rf ${D}${datadir}/man
}

FILES:${PN} += "${datadir}"

BBCLASSEXTEND = ""
