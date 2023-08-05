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

DEPENDS += " \
    compiler-rt \
    libcxx \
    openmp \
    vulkan-loader \
    glm \
"

REQUIRED_DISTRO_FEATURES = "vulkan"

SRCREV_FORMAT="sasha-samples"

SRC_URI = " \
    git://github.com/SaschaWillems/Vulkan.git;protocol=https;name=samples;branch=master \
    git://github.com/SaschaWillems/Vulkan-Assets.git;protocol=https;name=assets;destsuffix=assets;branch=master \
"

SRCREV_samples = "bc39dd58faaabfc1a32dc79d5538c8973b28cbce"
SRCREV_assets = "a27c0e584434d59b7c7a714e9180eefca6f0ec4b"

S = "${WORKDIR}/git"

inherit cmake features_check pkgconfig

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"
PREFERRED_PROVIDER_libgomp = "openmp"

PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)} \
"

# resolve the most common collision automatically by preferring wayland. this
# really only removes the unnecessary runtime deps. it doesn't change linking
# options

PACKAGECONFIG:remove ??= " \
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

EXTRA_OECMAKE += " \
    -DRESOURCE_INSTALL_DIR=${datadir}/vulkan-samples/assets \
    -DCMAKE_INSTALL_BINDIR=${bindir}/vulkan-samples \
"

# assets must be copied instead of directly placed in data using the SRC_URI
# options because yocto will clobber the contents of the destination directory

do_configure:prepend () {
    cp -r ${WORKDIR}/assets/* ${S}/data/
}

FILES:${PN} += " \
    ${datadir}/vulkan-samples \
    ${bindir}/vulkan-samples \
"

BBCLASSEXTEND = ""
