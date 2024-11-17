SUMMARY = "Vulkan Benchmark"
DESCRIPTION = "vkmark is an extensible Vulkan benchmarking suite with targeted, configurable scenes."
AUTHOR = "Collabora"
HOMEPAGE = "https://github.com/vkmark/vkmark"
BUGTRACKER = "https://github.com/vkmark/vkmark/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING-LGPL2.1;md5=4fbd65380cdd255951079008b364516c"

DEPENDS += " \
    assimp \
    glm \
    vulkan-headers \
    vulkan-loader \
"

REQUIRED_DISTRO_FEATURES = "vulkan"

S = "${WORKDIR}/git"

SRCREV = "ab6e6f34077722d5ae33f6bd40b18ef9c0e99a15"

SRC_URI = "\
    git://github.com/vkmark/vkmark.git;protocol=https;branch=master \
"

inherit meson features_check pkgconfig

PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)} \
    kms \
"

PACKAGECONFIG[kms] = "-Dkms=true,-Dkms=false,drm virtual/libgbm"
PACKAGECONFIG[wayland] = "-Dwayland=true,-Dwayland=false,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-Dxcb=true,-Dxcb=false,virtual/libx11 libxcb xcb-util-wm"

EXTRA_OEMESON += "--buildtype release"

FILES:${PN} += "${datadir}"

BBCLASSEXTEND = ""
