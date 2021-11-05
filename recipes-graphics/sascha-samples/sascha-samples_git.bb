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
    libgomp \
    vulkan-headers \
    python3-native \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"
ANY_OF_DISTRO_FEATURES = "x11 wayland"

SRC_URI = "git://github.com/SaschaWillems/Vulkan.git;protocol=https;branch=master"

SRCREV = "e79634e4da0a0bdcefa92b93d70350f784d9e40d"

S = "${WORKDIR}/git"

inherit cmake features_check

TOOLCHAIN = "clang"

# must choose x11 or wayland or both
PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[x11] = "-DUSE_WAYLAND_WSI=OFF, , libxcb libx11 libxrandr"
PACKAGECONFIG[wayland] = "-DUSE_WAYLAND_WSI=ON, -DUSE_WAYLAND_WSI=OFF, wayland"

do_patch:append() {
    cd ${S}
    python3 download_assets.py
}

do_install() {
    install -d ${D}${bindir}/sascha-samples
    cp -r ${WORKDIR}/bin/* ${D}${bindir}/sascha-samples
    cp ${S}/bin/* ${D}${bindir}/sascha-samples
}

FILES_${PN} = "${bindir}/sascha-samples"

BBCLASSEXTEND = ""
