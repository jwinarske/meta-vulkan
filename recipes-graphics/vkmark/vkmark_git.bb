SUMMARY = "Vulkan Benchmark"
DESCRIPTION = "vkmark is an extensible Vulkan benchmarking suite with targeted, configurable scenes."
AUTHOR = "Collabora"
HOMEPAGE = "https://github.com/vkmark/vkmark"
BUGTRACKER = "https://github.com/vkmark/vkmark/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "LGPL2.1"
LIC_FILES_CHKSUM = "file://COPYING-LGPL2.1;md5=dcf473723faabf17baa9b5f2207599d0"

DEPENDS += "\
    compiler-rt \
    libassimp \
    libcxx \
    openmp \
    python3-native \
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

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[kms] = "-Dkms=true,,drm virtual/libgbm"
PACKAGECONFIG[wayland] = "-wayland=true,,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-Dxcb=true,,virtual/libx11 libxcb"

# Default to kms if nothing set
EXTRA_OEMESON += " \
    ${@bb.utils.contains_any('PACKAGECONFIG', 'kms wayland x11', '', ' -Dkms=true', d)} \
    "

FILES:${PN} = "\
    ${bindir}/vkmark* \
    ${datadir}/vulkan-samples/assets \
    "

BBCLASSEXTEND = ""
