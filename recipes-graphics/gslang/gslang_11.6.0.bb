SUMMARY = "Khronos-reference front end for GLSL/ESSL, partial front end for HLSL, and a SPIR-V generator."
DESCRIPTION = "A comprehensive collection of open source C++ examples for Vulkan®, \
               the new generation graphics and compute API from Khronos."
AUTHOR = "Khronos Group"
HOMEPAGE = "https://github.com/KhronosGroup/glslang"
BUGTRACKER = "https://github.com/KhronosGroup/glslang"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "GPL2"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=c5ce49c0456e9b413b98a4368c378229"

DEPENDS += "\
    bison \
    compiler-rt \
    git-native \
    libcxx \
    python3-native \
   "

SRC_URI = "git://github.com/KhronosGroup/glslang.git;protocol=https;branch=master"

SRCREV = "2fb89a0072ae7316af1c856f22663fde4928128a"

S = "${WORKDIR}/git"

inherit cmake features_check

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

do_configure:prepend() {
    cd ${S}
    python3 ./update_glslang_sources.py
    git clone https://github.com/google/googletest.git External/googletest
    cd External/googletest
    git checkout 0c400f67fcf305869c5fb113dd296eca266c9725
    cd ${WORKDIR}/build
}

FILES:${PN} = "\
    ${libdir} \
    ${bindir} \
    ${includedir} \
    "

BBCLASSEXTEND = ""
