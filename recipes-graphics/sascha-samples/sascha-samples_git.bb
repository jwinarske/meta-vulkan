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

DEPENDS_append = "\
    glm \
    vulkan-headers \
    vulkan-loader \
"

REQUIRED_DISTRO_FEATURES = "vulkan"

SRCREV = "24591c6570904047818aafbb8089827fc1a86354"

SRC_URI = "gitsm://github.com/SaschaWillems/Vulkan.git;protocol=https;name=samples;branch=master"

S = "${WORKDIR}/git"

inherit cmake features_check pkgconfig

PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)} \
"

# resolve the most common collision automatically by preferring wayland. this
# really only removes the unnecessary runtime deps. it doesn't change linking
# options

PACKAGECONFIG_remove ??= " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'x11', '', d)} \
"

# all of the package config options below are mutually exclusive but they are
# automatically resolved by cmake logic in the source repo. this list shows the
# priority of the options from highest to lowest

PACKAGECONFIG[d2d] = "-DUSE_D2D_WSI=ON"
PACKAGECONFIG[dfb] = "-DUSE_DIRECTFB_WSI=ON"
PACKAGECONFIG[wayland] = "-DUSE_WAYLAND_WSI=ON,,wayland wayland-native wayland-protocols"
PACKAGECONFIG[headless] = "-DUSE_HEADLESS=ON"
PACKAGECONFIG[x11] = ",,libxcb libx11 libxrandr"

EXTRA_OECMAKE = " \
    -D RESOURCE_INSTALL_DIR=${datadir}/vulkan-samples/assets \
    -D CMAKE_INSTALL_BINDIR=${bindir}/vulkan-samples \
"

FILES_${PN} += "${datadir}"

BBCLASSEXTEND = ""
