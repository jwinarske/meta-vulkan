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

REQUIRED_DISTRO_FEATURES = "vulkan"

S = "${WORKDIR}/git"

SRCREV = "6bfd11b1ccb947e4eb8d1665083d56d66707de01"

SRC_URI = "git://github.com/Yours3lf/rpi-vk-driver.git;protocol=https;branch=master"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"

inherit cmake features_check pkgconfig

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE += "\
    -D BUILD_TESTING=OFF \
    -DRPI_ARCH=${BUILD_ARCH} \
"

do_configure:prepend() {
    export PATH="${STAGING_DIR_NATIVE}${bindir}":"${PATH}"
}

FILES:${PN} = "\
    ${libdir}/librpi-vk-driver.so \
    ${datadir}/vulkan/icd.d/rpi-vk-driver.json \
"

FILES:${PN}-dev = "${bindir}/QPUassemblerExe"

BBCLASSEXTEND = ""
