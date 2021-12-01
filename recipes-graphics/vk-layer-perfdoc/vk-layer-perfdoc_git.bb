SUMMARY = "Vulkan layer"
DESCRIPTION = "A cross-platform Vulkan layer which checks \
               Vulkan applications for best practices on Arm Mali devices."
AUTHOR = "ARM"
HOMEPAGE = "https://github.com/ARM-software/perfdoc"
BUGTRACKER = "https://github.com/ARM-software/perfdoc/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=dcf6622c76d01ba8c068628ba51a4d27"

DEPENDS += "\
    compiler-rt \
    libcxx \
    vulkan-headers \
   "

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "gitsm://github.com/ARM-software/perfdoc.git;protocol=https"

SRCREV = "718ab65de48b964c3355df33f8d7a5f4253cf7c7"

S = "${WORKDIR}/git"

inherit cmake features_check

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

EXTRA_OECMAKE += "-D PERFDOC_TESTS=OFF"

do_install() {
    install -d ${D}${datadir}/vulkan/explicit_layer.d
    install -m 644 layer/VkLayer_mali_perf_doc.json ${D}${datadir}/vulkan/explicit_layer.d
    
    install -d ${D}${libdir}
    install -m 644 layer/libVkLayer_mali_perf_doc.so ${D}${libdir}
    
    sed -i "s|./libVkLayer_mali_perf_doc.so|/usr/lib/libVkLayer_mali_perf_doc.so|g" ${D}${datadir}/vulkan/explicit_layer.d/VkLayer_mali_perf_doc.json
}

FILES:${PN} = " \
    ${libdir} \
    ${datadir} \
    "

FILES:${PN}-dev = ""

BBCLASSEXTEND = ""
