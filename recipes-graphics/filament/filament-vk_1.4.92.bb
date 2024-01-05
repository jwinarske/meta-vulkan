SUMMARY = "Lightweight 3D Render Engine"
DESCRIPTION = "Filament is a real-time physically based rendering engine for Android, iOS, Windows, Linux, macOS, and WebGL2"
AUTHOR = "Filament Authors"
HOMEPAGE = "https://github.com/google/filament"
BUGTRACKER = "https://github.com/google/filament/issues"
SECTION = "graphics"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4a8c8edce973aab6aaecb609fbab16bd"

DEPENDS += "\
    compiler-rt \
    libcxx \
    zip-native \
    "

DEPENDS:class-target += "\
    filament-vk-native \
    vulkan-loader \
    unzip-native \
    "

REQUIRED_DISTRO_FEATURES:class-target = "vulkan"

S = "${WORKDIR}/git"

SRCREV = "acfe9298d9113e64e71e63ad2f50f7e76db019dd"

SRC_URI = "\
    git://github.com/google/filament.git;protocol=https;branch=release \
    file://0001-error-ignoring-return-value-of-function-declared-wit.patch \
    file://0001-disable-backend-tests.patch \
    file://0001-install-required-files.patch \
    file://ImportExecutables-Release.cmake \
"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
TOOLCHAIN:class-native = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11 vulkan', d)}"

PACKAGECONFIG[opengl] = "-DFILAMENT_SUPPORTS_OPENGL=ON -DFILAMENT_SUPPORTS_EGL_ON_LINUX=ON,-DFILAMENT_SUPPORTS_OPENGL=OFF -DFILAMENT_SUPPORTS_EGL_ON_LINUX=OFF,virtual/egl virtual/libgles2"
PACKAGECONFIG[vulkan] = "-DFILAMENT_SUPPORTS_VULKAN=ON,-DFILAMENT_SUPPORTS_VULKAN=OFF,vulkan-loader"
PACKAGECONFIG[wayland] = "-DFILAMENT_SUPPORTS_WAYLAND=ON,-DFILAMENT_SUPPORTS_WAYLAND=OFF,wayland"
PACKAGECONFIG[x11] = "-DFILAMENT_SUPPORTS_X11=ON -DFILAMENT_SUPPORTS_XCB=ON,-DFILAMENT_SUPPORTS_X11=OFF -DFILAMENT_SUPPORTS_XCB=OFF,libxcb libx11 libxrandr"

inherit cmake pkgconfig features_check

EXTRA_OECMAKE:class-native += " \
    -D CMAKE_BUILD_TYPE=Release \
    -D FILAMENT_BUILD_FILAMAT=ON \
    -D FILAMENT_SKIP_SAMPLES=ON \
    -D FILAMENT_SKIP_SDL2=ON \
    ${PACKAGECONFIG_CONFARGS} \
    "

EXTRA_OECMAKE:class-target += " \
    -D BUILD_SHARED_LIBS=OFF \
    -D CMAKE_BUILD_TYPE=Release \
    -D FILAMENT_LINUX_IS_MOBILE=ON \
    -D FILAMENT_BUILD_FILAMAT=ON \
    -D FILAMENT_SKIP_SAMPLES=ON \
    -D FILAMENT_SKIP_SDL2=ON \
    -D DIST_ARCH=${BUILD_ARCH} \
    -D IMPORT_EXECUTABLES_DIR=. \
    -D FILAMENT_HOST_TOOLS_ROOT=${WORKDIR}/host_tools/bin \
    ${PACKAGECONFIG_CONFARGS} \
    "

do_configure:prepend:class-target () {
    # extract host_tools.zip
    rm -rf ${WORKDIR}/host_tools | true
    unzip ${STAGING_DATADIR_NATIVE}/filament/host_tools.zip -d ${WORKDIR}

    # overwrite auto generated version
    cp ${WORKDIR}/ImportExecutables-Release.cmake ${S}/ImportExecutables-Release.cmake
}

do_install:append:class-native () {
    rm -rf ${D}${includedir}
    rm -rf ${D}${libdir}

    #
    # Create host_tools.zip
    #

    install -d ${B}/host_tools
    mv ${D}${STAGING_DIR_NATIVE}/usr/LICENSE ${B}/host_tools/
    mv ${D}${STAGING_DIR_NATIVE}/usr/README.md ${B}/host_tools/

    install -d ${B}/host_tools/bin
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/basisu ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/cmgen ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/filamesh ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/glslminifier ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/matc ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/matinfo ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/mipgen ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/normal-blending ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/resgen ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/roughness-prefilter ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/specular-color ${B}/host_tools/bin/
    mv ${D}${STAGING_DIR_NATIVE}/usr/bin/uberz ${B}/host_tools/bin/

    install -d ${B}/host_tools/docs
    mv ${D}${STAGING_DIR_NATIVE}/usr/docs/filamesh.md ${B}/host_tools/docs/
    mv ${D}${STAGING_DIR_NATIVE}/usr/docs/matinfo.md ${B}/host_tools/docs/
    mv ${D}${STAGING_DIR_NATIVE}/usr/docs/mipgen.md ${B}/host_tools/docs/
    mv ${D}${STAGING_DIR_NATIVE}/usr/docs/normal-blending.md ${B}/host_tools/docs/
    mv ${D}${STAGING_DIR_NATIVE}/usr/docs/roughness-prefilter.md ${B}/host_tools/docs/
    mv ${D}${STAGING_DIR_NATIVE}/usr/docs/specular-color.md ${B}/host_tools/docs/

    rm -rf ${D}${STAGING_DIR_NATIVE}

    cd ${B}
    zip -r host_tools.zip host_tools
    install -D host_tools.zip ${D}${datadir}/filament/host_tools.zip
    rm -rf host_tools
}

do_install:append:class-target () {
    rm ${D}/usr/LICENSE
    rm ${D}/usr/README.md

    # dependent recipe sets FILAMENT_HOST_TOOLS_ROOT to use
    install -Dm 644 ${WORKDIR}/ImportExecutables-Release.cmake \
        ${D}${includedir}/cmake/filament/ImportExecutables-Release.cmake

    install -d ${D}${libdir}/filament
    mv ${D}${libdir}/${BUILD_ARCH}/*.a ${D}${libdir}/filament
    rm -rf ${D}${libdir}/${BUILD_ARCH}

    install -D ${STAGING_DATADIR_NATIVE}/filament/host_tools.zip ${D}${datadir}/filament/host_tools.zip
}

PACKAGES =+ "${PN}-host-tools"

FILES:${PN}-staticdev += "${libdir}"

FILES:${PN}-dev += "${includedir}"

FILES:${PN}-host-tools = "${datadir}"

BBCLASSEXTEND += "native nativesdk"
