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
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRCREV_FORMAT="sasha-samples"

SRC_URI = "git://github.com/SaschaWillems/Vulkan.git;protocol=https;name=samples \
           git://github.com/SaschaWillems/Vulkan-Assets.git;protocol=https;name=assets;destsuffix=assets"

SRCREV_samples = "79d0c5e436623436b6297a8c81fb3ee8ff78d804"
SRCREV_assets = "70847d249cbb3e3996d873592363b934ebacb0e0"

S = "${WORKDIR}/git"

inherit cmake features_check pkgconfig

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"
PREFERRED_PROVIDER_libgomp = "openmp"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[d2d] = "-DUSE_D2D_WSI=ON,-DUSE_D2D_WSI=OFF"
PACKAGECONFIG[headless] = "-DUSE_HEADLESS=ON,-DUSE_HEADLESS=OFF"
PACKAGECONFIG[wayland] = "-DUSE_WAYLAND_WSI=ON,-DUSE_WAYLAND_WSI=OFF,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = ",,libxcb libx11 libxrandr"

EXTRA_OECMAKE += " \
    -DRESOURCE_INSTALL_DIR=${datadir}/vulkan-samples/assets \
    -DCMAKE_INSTALL_BINDIR=${bindir}/vulkan-samples \
    "

do_configure:prepend () {
    cp -r ${WORKDIR}/assets/* ${S}/data/
    rm -rf ${WORKDIR}/assets
}

FILES:${PN} += "${datadir}"

BBCLASSEXTEND = ""
