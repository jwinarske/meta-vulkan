SUMMARY = "Vulkan Benchmark"
DESCRIPTION = "vkmark is an extensible Vulkan benchmarking suite with targeted, configurable scenes."
AUTHOR = "Collabora"
HOMEPAGE = "https://github.com/vkmark/vkmark"
BUGTRACKER = "https://github.com/vkmark/vkmark/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "LGPL2.1"
LIC_FILES_CHKSUM = "file://COPYING-LGPL2.1;md5=4fbd65380cdd255951079008b364516c"

DEPENDS += "\
    assimp \
    compiler-rt \
    libcxx \
    vulkan-headers \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "git://github.com/vkmark/vkmark.git;protocol=https;branch=master"

SRCREV = "cf45f2faee236fd1118be2fcd27e4f2a91fc2e40"

S = "${WORKDIR}/git"

inherit meson pkgconfig features_check

TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland xcb', d)}"

PACKAGECONFIG[kms] = "-Dkms=true,,drm virtual/libgbm"
PACKAGECONFIG[wayland] = "-wayland=true,,wayland wayland-native wayland-protocols"
PACKAGECONFIG[xcb] = "-Dxcb=true,,virtual/libx11 libxcb"

# Default to kms if nothing set
EXTRA_OEMESON += " \
    ${@bb.utils.contains_any('PACKAGECONFIG', 'kms wayland xcb', '', ' -Dkms=true', d)} \
    "

FILES:${PN} = "\
    ${bindir}/vkmark* \
    ${datadir}/vulkan-samples/assets \
    "

BBCLASSEXTEND = ""
