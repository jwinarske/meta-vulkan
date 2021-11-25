SUMMARY = "Vulkan Validation Layers"
DESCRIPTION = "Vulkan is an Explicit API, enabling direct control over \
               how GPUs actually work. By design, minimal error checking \
               is done inside a Vulkan driver. Applications have full \
               control and responsibility for correct operation. Any \
               errors in how Vulkan is used can result in a crash. \
               This project provides Vulkan validation layers that can \
               be enabled to assist development by enabling developers to \
               verify their applications correct use of the Vulkan API."
AUTHOR = "ARM"
HOMEPAGE = "https://github.com/KhronosGroup/Vulkan-ValidationLayers"
BUGTRACKER = "https://github.com/KhronosGroup/Vulkan-ValidationLayers/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=8df9e8826734226d08cb412babfa599c"

DEPENDS += "\
    compiler-rt \
    glslang-native \
    libcxx \
    python3-native \
    robin-hood-hashing \
    spirv-headers \
    spirv-tools-native \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "git://github.com/KhronosGroup/Vulkan-ValidationLayers.git;protocol=https"

SRCREV = "v${PV}"

S = "${WORKDIR}/git"

inherit cmake features_check

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[wayland] = "-DBUILD_WSI_WAYLAND_SUPPORT=ON,-DBUILD_WSI_WAYLAND_SUPPORT=OFF,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-DBUILD_WSI_XCB_SUPPORT=ON -DBUILD_WSI_XLIB_SUPPORT=ON,-DBUILD_WSI_XCB_SUPPORT=OFF -DBUILD_WSI_XLIB_SUPPORT=OFF,libxcb libx11 libxrandr"

EXTRA_OECMAKE += " \
    -D GLSLANG_INSTALL_DIR=${STAGING_BINDIR_NATIVE} \
    -D SPIRV_HEADERS_INSTALL_DIR=${STAGING_INCDIR} \
    -D SPIRV_TOOLS_INSTALL_DIR=${STAGING_BINDIR_NATIVE} \
    "

do_install:append () {
    error
}

BBCLASSEXTEND = ""
