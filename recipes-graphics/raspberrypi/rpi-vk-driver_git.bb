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
    expat \
    glibc \
    libdrm \
    vulkan-headers \
    zlib \
    libcxx \
    compiler-rt \
   "

SRC_URI = "git://github.com/Yours3lf/rpi-vk-driver.git;protocol=https;branch=master"

SRCREV = "6bfd11b1ccb947e4eb8d1665083d56d66707de01"

S = "${WORKDIR}/git"

inherit cmake features_check

TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

REQUIRED_DISTRO_FEATURES = "vulkan"
ANY_OF_DISTRO_FEATURES = "x11 wayland"

# choose x11, wayland or both
PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[x11] = "-DBUILD_WSI_XLIB_SUPPORT=ON -DBUILD_WSI_XCB_SUPPORT=ON, -DBUILD_WSI_XLIB_SUPPORT=OFF -DBUILD_WSI_XCB_SUPPORT=OFF, libxcb libx11 libxrandr"
PACKAGECONFIG[wayland] = "-DBUILD_WSI_WAYLAND_SUPPORT=ON, -DBUILD_WSI_WAYLAND_SUPPORT=OFF, wayland wayland-native wayland-protocols"

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE += "-DCMAKE_SKIP_INSTALL_RPATH=ON \
                  -DBUILD_TESTING=OFF \
                  ${PACKAGECONFIG_CONFARGS} \
                 "

FILES_${PN} = "${libdir}/librpi-vk-driver.so \
               ${datadir}/vulkan/icd.d/rpi-vk-driver.json \
              "
FILES_${PN}-dev = "${bindir}/QPUassemblerExe"

BBCLASSEXTEND = ""
