SUMMARY = "Spinning Vulkan Cube"
DESCRIPTION = "Vulkan demo that illustrates minimum viable product for multi platform support."
AUTHOR = "Kristian Hogsberg"
HOMEPAGE = "https://github.com/krh/vkcube"
BUGTRACKER = "https://github.com/krh/vkcube"
SECTION = "graphics"
CVE_PRODUCT = "vkcube"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "\
    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

DEPENDS += "\
    drm \
    libpng \
    virtual/libgbm \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "git://github.com/krh/vkcube.git;protocol=https;branch=master"

SRCREV = "f77395324a3297b2b6ffd7bce0383073e4670190"

S = "${WORKDIR}/git"

inherit meson pkgconfig features_check

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[wayland] = "-Dwayland=true,-Dwayland=false,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-Dxcb=true,-Dxcb=false,virtual/libx11 libxcb"

EXTRA_OEMESON += "--buildtype release"

do_install() {
    install -d ${D}${bindir}
    cp vkcube ${D}${bindir}
}

BBCLASSEXTEND = ""
