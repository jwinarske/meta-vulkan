SUMMARY = "Quake III Vulkan implementation."
DESCRIPTION = "Vulkan backend implementation for Quake III."
AUTHOR = "Sui Jingfeng"
HOMEPAGE = "https://github.com/suijingfeng/vkQuake3"
BUGTRACKER = "https://github.com/suijingfeng/vkQuake3/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=87113aa2b484c59a17085b5c3f900ebf"

DEPENDS += "\
    compiler-rt \
    curl \
    jpeg \
    libcxx \
    libevdev \
    libinput \
    libogg \
    libopus \
    opusfile \
    libsdl2 \
    libvorbis \
    openal-soft \
    openssl \
    udev \
    virtual/egl \
    virtual/libgles2 \
    vulkan-loader \
    zlib \
   "

RDEPENDS:${PN} += "\
   curl \
   jpeg \
   libegl-mesa \
   libgles1-mesa \
   libgles2-mesa \
   libogg \
   libopus \
   libvorbis \
   openal-soft \
   opusfile \
   zlib \
   "

REQUIRED_DISTRO_FEATURES = "vulkan opengl"

SRC_URI = "git://github.com/suijingfeng/vkQuake3.git;protocol=https;branch=master"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

inherit autotools-brokensep features_check

EXTRA_OEMAKE += "\
    release \
    ARCH=${HOST_ARCH} \
    COMPILE_ARCH=${TARGET_ARCH} \
    USE_INTERNAL_ZLIB=0 \
    USE_INTERNAL_JPEG=0 \
    HAVE_VM_COMPILED=0 \
    USE_LOCAL_HEADERS=0 \
    USE_CURL_DLOPEN=1 \
    USE_OPENAL_DLOPEN=1 \
    BUILD_SERVER=1 \
    BUILD_RENDERER_OPENGL2=1 \
    USE_RENDERER_DLOPEN=1 \
    BUILD_MISSIONPACK=1 \
    "

do_install() {

    install -d ${D}/usr/games/vkquake3/baseq3
    install -d ${D}/usr/games/vkquake3/missionpack

    cd ${S}/build/release-linux-${TARGET_ARCH}

    cp ioquake3.aarch64 ${D}/usr/games/vkquake3/
    cp ioq3ded.aarch64 ${D}/usr/games/vkquake3/

    cp baseq3/cgameaarch64.so ${D}/usr/games/vkquake3/baseq3/
    cp baseq3/qagameaarch64.so ${D}/usr/games/vkquake3/baseq3/
    cp baseq3/uiaarch64.so ${D}/usr/games/vkquake3/baseq3/

    cp missionpack/cgameaarch64.so ${D}/usr/games/vkquake3/missionpack/
    cp missionpack/qagameaarch64.so ${D}/usr/games/vkquake3/missionpack/
    cp missionpack/uiaarch64.so ${D}/usr/games/vkquake3/missionpack/

    cp renderer_opengl1_aarch64.so ${D}/usr/games/vkquake3/
    cp renderer_opengl2_aarch64.so ${D}/usr/games/vkquake3/
    cp renderer_vulkan_aarch64.so ${D}/usr/games/vkquake3/
}

FILES:${PN} = "/usr/games"

BBCLASSEXTEND = ""
