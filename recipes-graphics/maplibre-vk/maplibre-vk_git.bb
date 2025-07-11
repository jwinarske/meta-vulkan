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
    curl \
    icu \
    jpeg \
    libpng \
    libwebp \
    libuv \
    virtual/egl \
    virtual/libgles3 \
"

RDEPENDS:${PN} += "\
    vulkan-loader \
"

REQUIRED_DISTRO_FEATURES = "opengl vulkan"

SRC_URI = "\
    gitsm://github.com/maplibre/maplibre-native.git;protocol=https;branch=main \
    file://0001-Remove-parts-for-wayland-vulkan.patch \
"

SRCREV = "b7bf48683cee22a35363e9a310893eb6b47c1a57"

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
    -D MLN_DRAWABLE_RENDERER=ON \
    -D MLN_LEGACY_RENDERER=OFF \
    -D MLN_WITH_EGL=OFF \
    -D MLN_WITH_OPENGL=OFF \
    "

do_install:append() {
    install -d ${D}${libdir}

    cp ${B}/libmbgl-*.a ${D}${libdir}

    install -d ${D}${libdir}/vendor
    install -d ${D}${libdir}/vendor/glslang
    install -d ${D}${libdir}/vendor/glslang/SPIRV

    cp ${B}/vendor/glslang/SPIRV/libSPIRV.a                             ${D}${libdir}/vendor/glslang/SPIRV
    cp ${B}/vendor/glslang/SPIRV/libSPVRemapper.a                       ${D}${libdir}/vendor/glslang/SPIRV

    install -d ${D}${libdir}/vendor/glslang/glslang
    cp ${B}/vendor/glslang/glslang/libglslang.a                         ${D}${libdir}/vendor/glslang/glslang
    cp ${B}/vendor/glslang/glslang/libMachineIndependent.a              ${D}${libdir}/vendor/glslang/glslang
    cp ${B}/vendor/glslang/glslang/libGenericCodeGen.a                  ${D}${libdir}/vendor/glslang/glslang
    cp ${B}/vendor/glslang/glslang/libglslang-default-resource-limits.a ${D}${libdir}/vendor/glslang/glslang
    cp ${B}/vendor/glslang/glslang/OSDependent/Unix/libOSDependent.a    ${D}${libdir}/vendor/glslang/glslang
}

FILES:${PN}-staticdev += "${libdir}"

BBCLASSEXTEND = ""
