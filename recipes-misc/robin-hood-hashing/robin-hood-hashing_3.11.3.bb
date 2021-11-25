SUMMARY = "robin_hood unordered map & set"
DESCRIPTION = "Fast & memory efficient hashtable based on robin hood hashing for C++11/14/17/20."
AUTHOR = "martinus"
HOMEPAGE = "https://github.com/martinus/robin-hood-hashing"
BUGTRACKER = "https://github.com/martinus/robin-hood-hashing/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=77e01beb871656508b138bb62e1a6fb2"

DEPENDS += "\
    compiler-rt \
    libcxx \
   "

SRC_URI = "git://github.com/martinus/robin-hood-hashing.git;protocol=https"

SRCREV = "v${PV}"

S = "${WORKDIR}/git"

inherit cmake features_check

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"


BBCLASSEXTEND = ""
