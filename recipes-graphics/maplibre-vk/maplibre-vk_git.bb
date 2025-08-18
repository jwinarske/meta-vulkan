SUMMARY = "MapLibre Native Vulkan"
DESCRIPTION = "MapLibre Native - Interactive vector tile maps for iOS, \
               Android and other platforms."
AUTHOR = "Maplibre"
HOMEPAGE = "https://maplibre.org"
BUGTRACKER = "https://github.com/maplibre/maplibre-native/issues"
SECTION = "graphics"
CVE_PRODUCT = ""

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "\
    file://LICENSE.md;md5=9557c4180a72a6137edd96affcb140e4 \
"

DEPENDS += "\
    compiler-rt \
    curl \
    icu \
    jpeg \
    libcxx \
    libpng \
    libwebp \
    libuv \
    virtual/egl \
    virtual/libgles3 \
"

RDEPENDS:${PN} += "\
    vulkan-loader \
"

REQUIRED_DISTRO_FEATURES = "vulkan"

SRC_URI = "\
    gitsm://github.com/maplibre/maplibre-native.git;protocol=https;branch=main \
        file://0001-SPIRV-include-cstdint.patch \
        file://0002-Prevent-setting-MLN_WITH_EGL-and-OPENGL_USE_GLES3.patch \
"

SRCREV = "core-fe158c7e9b0b3f748f88d34ad384a7bcbc2cf903"

TOOLCHAIN = "clang"
TOOLCHAIN_NATIVE = "clang"
TC_CXX_RUNTIME = "llvm"
PREFERRED_PROVIDER_llvm = "clang"
PREFERRED_PROVIDER_llvm-native = "clang-native"
PREFERRED_PROVIDER_libgcc = "compiler-rt"
PREFERRED_PROVIDER_libgomp = "openmp"
LIBCPLUSPLUS = "-stdlib=libc++"

inherit pkgconfig cmake features_check

PACKAGECONFIG ??= "\
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland', d)} \
"

PACKAGECONFIG[wayland] = "-DMLN_WITH_WAYLAND=ON, -DMLN_WITH_WAYLAND=OFF, wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-DMLN_WITH_X11=ON, -DMLN_WITH_X11=OFF, libxcb libx11 libxrandr"

EXTRA_OECMAKE += " \
    -D BUILD_SHARED_LIBS=OFF \
    -D MLN_WITH_WERROR=OFF \
    -D MLN_WITH_VULKAN=ON \
    -D MLN_LEGACY_RENDERER=OFF \
    -D MLN_WITH_EGL=OFF \
    -D MLN_WITH_OPENGL=OFF \
    -D MLN_WITH_GLFW=OFF \
    "

do_install:append() {
    install -d ${D}${libdir}/maplibre

    cp ${B}/libmbgl-*.a ${D}${libdir}/maplibre

    install -d ${D}${libdir}/maplibre/vendor
    install -d ${D}${libdir}/maplibre/vendor/glslang
    install -d ${D}${libdir}/maplibre/vendor/glslang/SPIRV

    cp ${B}/vendor/glslang/SPIRV/libSPIRV.a                             ${D}${libdir}/maplibre/vendor/glslang/SPIRV
    cp ${B}/vendor/glslang/SPIRV/libSPVRemapper.a                       ${D}${libdir}/maplibre/vendor/glslang/SPIRV

    install -d ${D}${libdir}/vendor/glslang/glslang
    cp ${B}/vendor/glslang/glslang/libglslang.a                         ${D}${libdir}/maplibre/vendor/glslang/glslang
    cp ${B}/vendor/glslang/glslang/libMachineIndependent.a              ${D}${libdir}/maplibre/vendor/glslang/glslang
    cp ${B}/vendor/glslang/glslang/libGenericCodeGen.a                  ${D}${libdir}/maplibre/vendor/glslang/glslang
    cp ${B}/vendor/glslang/glslang/libglslang-default-resource-limits.a ${D}${libdir}/maplibre/vendor/glslang/glslang
    cp ${B}/vendor/glslang/glslang/OSDependent/Unix/libOSDependent.a    ${D}${libdir}/maplibre/vendor/glslang/glslang
}

FILES:${PN}-staticdev += "${libdir}"

INSANE_SKIP:${PN}-staticdev = "buildpaths"

BBCLASSEXTEND = ""
