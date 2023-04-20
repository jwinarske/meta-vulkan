DESCRIPTION = "Open Asset Import Library is a portable Open Source library to import \
               various well-known 3D model formats in a uniform manner."
HOMEPAGE = "http://www.assimp.org/"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d9d5275cab4fb13ae624d42ce64865de"

DEPENDS += "\
    compiler-rt \
    libcxx \
    zlib \
"

SRC_URI = "git://github.com/assimp/assimp.git;protocol=https;branch=master \
           "
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>(\d+(\.\d+)+))"

SRCREV = "9519a62dd20799c5493c638d1ef5a6f484e5faf1"

S = "${WORKDIR}/git"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"

inherit cmake

EXTRA_OECMAKE = "\
    -D ASSIMP_HUNTER_ENABLED=OFF \
    -D ASSIMP_DOUBLE_PRECISION=OFF \
    -D ASSIMP_OPT_BUILD_PACKAGES=OFF \
    -D ASSIMP_BUILD_ASSIMP_TOOLS=OFF \
    -D ASSIMP_BUILD_SAMPLES=OFF \
    -D ASSIMP_BUILD_ZLIB=OFF \
    -D ASSIMP_BUILD_TESTS=OFF \
    -D ASSIMP_WARNINGS_AS_ERRORS=ON \
    -D ASSIMP_IGNORE_GIT_HASH=OFF \
    -D ASSIMP_LIB_INSTALL_DIR=${baselib} \
"
