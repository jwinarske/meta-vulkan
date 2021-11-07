SUMMARY = "VK driver for the Raspberry Pi (Broadcom Videocore IV)"
DESCRIPTION = "RPi-VK-Driver is a low level GPU driver for the \
               Broadcom Videocore IV GPU that implements a subset \
               of the Vulkan (registered trademark of The Khronos \
               Group) standard."
AUTHOR = "Marton Tamas"
HOMEPAGE = "https://github.com/Yours3lf/rpi-vk-driver"
BUGTRACKER = "https://github.com/Yours3lf/rpi-vk-driver/issues"
SECTION = "graphics"
CVE_PRODUCT = "librpi-vk-driver.so"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0e9db807e4f1ed14373059c8499d5f82"

DEPENDS += "\
    compiler-rt \
    drm \
    expat \
    libcxx \
    python3-native \
    vulkan-headers \
    vulkan-loader \
    zlib \
   "

SRC_URI = "git://github.com/meta-flutter/rpi-vk-driver.git;protocol=https;branch=simplify"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

FILES_${PN} = "${libdir}/librpi-vk-driver.so \
               ${datadir}/vulkan/icd.d/rpi-vk-driver.json \
              "
FILES_${PN}-dev = "${bindir}/QPUassemblerExe"

BBCLASSEXTEND = ""
