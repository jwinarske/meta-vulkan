SUMMARY = "Quake III Vulkan implementation."
DESCRIPTION = "Vulkan backend implementation for Quake III."
AUTHOR = "Marton Tamas"
HOMEPAGE = "https://github.com/Yours3lf/vkQuake3/"
BUGTRACKER = "https://github.com/Yours3lf/vkQuake3/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=87113aa2b484c59a17085b5c3f900ebf"

DEPENDS += "\
    compiler-rt \
    libcxx \
    libevdev \
    libinput \
    libsdl2 \
    mtdev \
    udev \
    vulkan-loader \
    vulkan-headers \
    zlib \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "git://github.com/jwinarske/vkQuake3.git;protocol=https;branch=yocto"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake features_check

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"

EXTRA_OECMAKE += "\
    -D ARCH_STRING=${TARGET_ARCH} \
    "

FILES_${PN} = "\
    ${bindir} \
    ${libdir} \
    "

FILES_${PN}-dev = ""

BBCLASSEXTEND = ""
