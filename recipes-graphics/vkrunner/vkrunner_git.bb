SUMMARY = "A shader script tester for Vulkan ."
DESCRIPTION = "VkRunner is a Vulkan shader tester based on shader_runner in Piglit. \
               The goal is to make it be able to test scripts as similar to Piglit’s \
               shader_test format as possible."
AUTHOR = "Igalia"
HOMEPAGE = "https://github.com/Igalia/vkrunner"
BUGTRACKER = "https://github.com/Igalia/vkrunner/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://COPYING;md5=2327139e51b79a3abbfec6568e92ecc5"

DEPENDS += "\
    compiler-rt \
    libcxx \
    vulkan-headers \
    vulkan-loader \
   "

RDEPENDS += "\
    glslang \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "git://github.com/Igalia/vkrunner.git;protocol=https;branch=master"

SRCREV = "1b4cc6b129e857d88ba9487bc8a4983d6a11df02"

S = "${WORKDIR}/git"

inherit cmake features_check

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

do_install:append() {
    install -d ${D}${datadir}/vkrunner/examples
    cp -r ${S}/examples/* ${D}${datadir}/vkrunner/examples/
}

FILES:${PN} += "\
    $[datadir} \
    "

BBCLASSEXTEND = ""
