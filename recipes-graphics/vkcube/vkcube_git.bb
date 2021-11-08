SUMMARY = "Spinning Vulkan Cube"
DESCRIPTION = "Vulkan demo that show minimum viable product for multiple platform support."
AUTHOR = "Kristian Hogsberg"
HOMEPAGE = "https://github.com/krh/vkcube"
BUGTRACKER = "https://github.com/krh/vkcube"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "MIT"

DEPENDS += "\
    drm \
    virutal/libgbm \
    vulkan-headers \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "git://github.com/jwinarske/vkcube.git;protocol=https;branch=yocto"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit meson pkgconfig features_check

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland xcb', d)}"

PACKAGECONFIG[wayland] = "-Dwayland=true,-Dwayland=false,wayland wayland-native wayland-protocols"
PACKAGECONFIG[xcb] = "-Dxcb=true,-Dxcv=false,virtual/libx11 libxcb"

FILES:${PN} = "\
    ${bindir} \
    "

BBCLASSEXTEND = ""
