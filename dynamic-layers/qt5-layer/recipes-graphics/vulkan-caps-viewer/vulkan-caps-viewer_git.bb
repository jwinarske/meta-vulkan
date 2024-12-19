SUMMARY = "Vulkan hardware capability viewer"
DESCRIPTION = "Client application to display hardware implementation " \
   "details for GPUs supporting the Vulkan API by Khronos."
AUTHOR = "Sascha Willems"
HOMEPAGE = "https://github.com/SaschaWillems/VulkanCapsViewer"
BUGTRACKER = "https://github.com/SaschaWillems/VulkanCapsViewer/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0389d9d616b56125bd46fcb2cb900a8b"

DEPENDS += " \
    qtbase \
    vulkan-loader \
"

REQUIRED_DISTRO_FEATURES = "vulkan"

SRCREV = "ae13fe001605bb21183bf67c4f50645c1c35d76b"

SRC_URI = "\
    gitsm://github.com/SaschaWillems/VulkanCapsViewer.git;protocol=https;branch=master \
"

S = "${UNPACKDIR}/git"

inherit qmake5 features_check pkgconfig

PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)} \
"

# resolve the most common collision automatically by preferring wayland. this
# really only removes the unnecessary runtime deps. it doesn't change linking
# options

PACKAGECONFIG:remove ??= " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'x11', '', d)} \
"

# all of the package config options below are mutually exclusive.
# this list shows the priority of the options from highest to lowest

PACKAGECONFIG[wayland] = "-DWAYLAND=ON,-DWAYLAND=OFF,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-DX11=ON,-DX11=OFF,libxcb libx11 libxrandr"


FILES:${PN} += " \
    ${datadir} \
"

RDEPENDS:${PN} += " \
    qtdeclarative \
    qtwayland \
"

BBCLASSEXTEND = ""
