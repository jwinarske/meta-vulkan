SUMMARY = "Examples and demos for the new Vulkan API."
DESCRIPTION = "A comprehensive collection of open source C++ examples for Vulkan®, \
               the new generation graphics and compute API from Khronos."
AUTHOR = "Sascha Willems"
HOMEPAGE = "https://github.com/SaschaWillems/Vulkan"
BUGTRACKER = "https://github.com/SaschaWillems/Vulkan/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=dcf473723faabf17baa9b5f2207599d0"

DEPENDS += "\
    compiler-rt \
    libcxx \
    openmp \
    python3-native \
    vulkan-headers \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "gitsm://github.com/SaschaWillems/Vulkan.git;protocol=https;branch=master"

SRCREV = "e79634e4da0a0bdcefa92b93d70350f784d9e40d"

S = "${WORKDIR}/git"

inherit cmake features_check

TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"
PREFERRED_PROVIDER:libgomp = "openmp"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[d2d] = "-DUSE_D2D_WSI=ON,"
PACKAGECONFIG[headless] = "-DUSE_HEADLESS=ON,"
PACKAGECONFIG[wayland] = "-DUSE_WAYLAND_WSI=ON,,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-DUSE_WAYLAND_WSI=OFF,,libxcb libx11 libxrandr"

# Default to d2d if nothing set
EXTRA_OECMAKE += " \
    -DRESOURCE_INSTALL_DIR=${datadir}/vulkan-samples/assets \
    -DCMAKE_INSTALL_BINDIR=${bindir}/vulkan-samples \
    ${@bb.utils.contains_any('PACKAGECONFIG', 'd2d headless wayland x11', '', ' -DUSE_D2D_WSI=ON', d)} \
    "

do_configure:prepend() {
    cd ${S}
    python3 download_assets.py
    cd ${WORKDIR}/build
}

FILES:${PN} = "\
    ${bindir}/vulkan-samples* \
    ${datadir}/vulkan-samples/assets \
    "

BBCLASSEXTEND = ""
