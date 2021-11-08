SUMMARY = "Vulkan Quake port based on QuakeSpasm."
DESCRIPTION = "vkQuake is a port of id Software's Quake using Vulkan instead \
               of OpenGL for rendering. It is based on the popular QuakeSpasm \
               and QuakeSpasm-Spiked ports and runs all mods compatible with \
               QuakeSpasm like Arcane Dimensions."
AUTHOR = "Axel Gneiting"
HOMEPAGE = "https://github.com/Novum/vkQuake"
BUGTRACKER = "https://github.com/Novum/vkQuake/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS += "\
    compiler-rt \
    libcxx \
    libmad \
    libsdl2 \
    libvorbis \
    vulkan-headers \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan x11"

SRC_URI = "git://github.com/jwinarske/vkQuake.git;protocol=https;branch=yocto"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

inherit features_check

do_compile() {
    cd ${S}/Quake
    oe_runmake VERBOSE=1
}

do_install() {
    cd ${S}/Quake
    install -d ${D}${bindir}
    oe_runmake install DESTDIR=${D}
}


FILES:${PN} = "${bindir}"

BBCLASSEXTEND = ""
