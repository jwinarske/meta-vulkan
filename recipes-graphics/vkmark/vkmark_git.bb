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
    glm \
    libcxx \
    vulkan-headers \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "git://github.com/jwinarske/vkmark.git;protocol=https;branch=stacksize_fix"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit meson pkgconfig features_check

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland xcb', d)}"

PACKAGECONFIG[kms] = "-Dkms=true,-Dkms=false,drm virtual/libgbm"
PACKAGECONFIG[wayland] = "-Dwayland=true,-Dwayland=false,wayland wayland-native wayland-protocols"
PACKAGECONFIG[xcb] = "-Dxcb=true,-Dxcb=false,virtual/libx11 libxcb"

# Default to kms if nothing set
EXTRA_OEMESON += "${@bb.utils.contains_any('PACKAGECONFIG', 'kms wayland xcb', '', ' -Dkms=true', d)}"

do_configure:prepend() {
    export VULKAN_SDK="${STAGING_DIR_TARGET}/usr"
}

# strange build break looking for headers
do_compile[depends] += "vulkan-headers:do_populate_sysroot"

BBCLASSEXTEND = ""
