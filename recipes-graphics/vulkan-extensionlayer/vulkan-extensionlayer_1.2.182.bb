SUMMARY = "Vulkan Extension Layer"
DESCRIPTION = "Layer providing Vulkan features when native \
               support is unavailable."
AUTHOR = "Khronos"
HOMEPAGE = "https://github.com/KhronosGroup/Vulkan-ExtensionLayer"
BUGTRACKER = "https://github.com/KhronosGroup/Vulkan-ExtensionLayer/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

DEPENDS += "\
    compiler-rt \
    glslang \
    libcxx \
    spirv-headers \
    vulkan-headers \
   "

SRC_URI = "git://github.com/KhronosGroup/Vulkan-ExtensionLayer.git;protocol=https"

SRCREV = "v${PV}"

S = "${WORKDIR}/git"

inherit cmake

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"
PREFERRED_PROVIDER:libgomp = "openmp"

FILES:${PN} = " \
    ${libdir} \
    ${datadir} \
    "

FILES:${PN}-dev = ""

BBCLASSEXTEND = ""
