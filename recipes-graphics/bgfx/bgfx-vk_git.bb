SUMMARY = "rendering library"
DESCRIPTION = "Cross-platform, graphics API agnostic, \
              "Bring Your Own Engine/Framework" style \
              rendering library."
AUTHOR = "bgfx authors"
HOMEPAGE = "https://github.com/bkaradzic/bgfx"
BUGTRACKER = "https://github.com/bkaradzic/bgfx/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "BSD-2"
LIC_FILES_CHKSUM = "file://bgfx/LICENSE;md5=0e9db807e4f1ed14373059c8499d5f82"

DEPENDS += "\
    compiler-rt \
    libcxx \
    vulkan-loader \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "gitsm://github.com/bkaradzic/bgfx.cmake.git;protocol=https;branch=master"

SRCREV = "${AUTOREV}"


S = "${WORKDIR}/git"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

inherit cmake features_check

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE:class-native += "\
    -D BGFX_BUILD_TOOLS=ON
    -D BGFX_BUILD_EXAMPLES=OFF
    -D BGFX_INSTALL=ON
    -D BGFX_INSTALL_EXAMPLES=OFF
    -D BGFX_CUSTOM_TARGETS=ON
    -D BGFX_CONFIG_RENDERER_WEBGPU=OFF
    "

EXTRA_OECMAKE:class-target += "\
    -D BGFX_BUILD_TOOLS=OFF
    -D BGFX_BUILD_EXAMPLES=ON
    -D BGFX_INSTALL=ON
    -D BGFX_INSTALL_EXAMPLES=OFF
    -D BGFX_CUSTOM_TARGETS=ON
    -D BGFX_CONFIG_RENDERER_WEBGPU=OFF
    -D BGFX_LIBRARY_TYPE="STATIC"
    "

BBCLASSEXTEND = "native nativesdk"
