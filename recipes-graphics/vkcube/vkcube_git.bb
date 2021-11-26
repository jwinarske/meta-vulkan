SUMMARY = "Spinning Vulkan Cube"
DESCRIPTION = "Vulkan demo that illustrates minimum viable product for multi platform support."
AUTHOR = "Kristian Hogsberg"
HOMEPAGE = "https://github.com/krh/vkcube"
BUGTRACKER = "https://github.com/krh/vkcube"
SECTION = "graphics"
CVE_PRODUCT = "vkcube"
LICENSE = "CLOSED"

DEPENDS += "\
    compiler-rt \
    drm \
    libcxx \
    libpng \
    virtual/libgbm \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "git://github.com/jwinarske/vkcube.git;protocol=https;branch=yocto"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

inherit meson pkgconfig features_check

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[wayland] = "-Dwayland=true,-Dwayland=false,wayland wayland-native wayland-protocols"
PACKAGECONFIG[xcb] = "-Dxcb=true,-Dxcb=false,virtual/libx11 libxcb"

do_install() {
    install -d ${D}${bindir}
    cp vkcube ${D}${bindir}
}

FILES:${PN} = "\
    ${bindir} \
    "

BBCLASSEXTEND = ""
