SUMMARY = "Vulkan Validation Layers"
DESCRIPTION = "Vulkan is an Explicit API, enabling direct control over \
               how GPUs actually work. By design, minimal error checking \
               is done inside a Vulkan driver. Applications have full \
               control and responsibility for correct operation. Any \
               errors in how Vulkan is used can result in a crash. \
               This project provides Vulkan validation layers that can \
               be enabled to assist development by enabling developers to \
               verify their applications correct use of the Vulkan API."
AUTHOR = "Khronos"
HOMEPAGE = "https://github.com/KhronosGroup/Vulkan-ValidationLayers"
BUGTRACKER = "https://github.com/KhronosGroup/Vulkan-ValidationLayers/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=8df9e8826734226d08cb412babfa599c"

DEPENDS += "\
    compiler-rt \
    glslang \
    libcxx \
    spirv-headers \
    spirv-tools \
    vulkan-headers \
   "

SRC_URI = "git://github.com/KhronosGroup/Vulkan-ValidationLayers.git;protocol=https;name=layers \
           git://github.com/martinus/robin-hood-hashing.git;protocol=https;destsuffix=git/robin-hood;name=robin_hood"

SRCREV_layers = "v${PV}"
SRCREV_robin_hood = "3.11.3"

S = "${WORKDIR}/git"

inherit cmake

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[wayland] = "-DBUILD_WSI_WAYLAND_SUPPORT=ON,-DBUILD_WSI_WAYLAND_SUPPORT=OFF,wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-DBUILD_WSI_XCB_SUPPORT=ON -DBUILD_WSI_XLIB_SUPPORT=ON,-DBUILD_WSI_XCB_SUPPORT=OFF -DBUILD_WSI_XLIB_SUPPORT=OFF,libxcb libx11 libxrandr"


EXTRA_OECMAKE += " \
    -D VulkanHeaders_INCLUDE_DIR=${STAGING_INCDIR} \
    -D VulkanRegistry_DIR=${RECIPE_SYSROOT}/${datadir} \
    -D GLSLANG_INSTALL_DIR=${STAGING_DIR_TARGET} \
    -D SPIRV_HEADERS_INSTALL_DIR=${STAGING_DIR_TARGET} \
    -D SPIRV_TOOLS_INSTALL_DIR=${STAGING_DIR_TARGET} \
    -D ROBIN_HOOD_HASHING_INSTALL_DIR=${S}/robin-hood \
    -D BUILD_WERROR=OFF \
    "

FILES:${PN} = " \
    ${libdir} \
    ${datadir} \
    "

FILES:${PN}-dev = ""

BBCLASSEXTEND = ""
