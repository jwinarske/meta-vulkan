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
    unzip-native \
    zip-native \
    "

DEPENDS:class-target += "\
    filament-vk-native \
    vulkan-loader \
    "

REQUIRED_DISTRO_FEATURES:class-target = "vulkan"


TOOLCHAIN = "clang"
TOOLCHAIN_NATIVE = "clang"
TC_CXX_RUNTIME = "llvm"
PREFERRED_PROVIDER_llvm = "clang"
PREFERRED_PROVIDER_llvm-native = "clang-native"
PREFERRED_PROVIDER_libgcc = "compiler-rt"
LIBCPLUSPLUS = "-stdlib=libc++"


SRCREV = "v1.60.1"

SRC_URI = "\
    git://github.com/google/filament.git;protocol=https;branch=release \
    file://0001-disable-backend-tests.patch \
    file://0002-install-required-files.patch \
    file://0003-move-include-contents-to-include-filament.patch \
    file://0004-move-libraries-so-they-install.patch \
    file://0005-return-shader-type-mobile-for-linux-vulkan.patch \
    file://0006-spirv-tools-threads.patch \
    file://ImportExecutables-Release.cmake \
"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11 vulkan', d)}"

PACKAGECONFIG[opengl] = "-DFILAMENT_SUPPORTS_OPENGL=ON -DFILAMENT_SUPPORTS_EGL_ON_LINUX=ON,-DFILAMENT_SUPPORTS_OPENGL=OFF -DFILAMENT_SUPPORTS_EGL_ON_LINUX=OFF,virtual/egl virtual/libgles2"
PACKAGECONFIG[vulkan] = "-DFILAMENT_SUPPORTS_VULKAN=ON,-DFILAMENT_SUPPORTS_VULKAN=OFF,vulkan-loader"
PACKAGECONFIG[wayland] = "-DFILAMENT_SUPPORTS_WAYLAND=ON,-DFILAMENT_SUPPORTS_WAYLAND=OFF,wayland"
PACKAGECONFIG[x11] = "-DFILAMENT_SUPPORTS_X11=ON -DFILAMENT_SUPPORTS_XCB=ON,-DFILAMENT_SUPPORTS_X11=OFF -DFILAMENT_SUPPORTS_XCB=OFF,libxcb libx11 libxrandr"

inherit cmake pkgconfig features_check

# -D FILAMENT_USE_EXTERNAL_GLES3=OFF 
# -DCMAKE_STAGING_PREFIX=${FILAMENT_STAGING_DIR} 

EXTRA_OECMAKE:class-native += " \
    -D CMAKE_POSITION_INDEPENDENT_CODE=ON \
    -D BUILD_SHARED_LIBS=OFF \
    -D CMAKE_BUILD_TYPE=Release \
    -D CMAKE_BUILD_WITH_INSTALL_RPATH=ON \
    -D FILAMENT_BUILD_FILAMAT=ON \
    -D FILAMENT_SKIP_SAMPLES=ON \
    -D FILAMENT_SKIP_SDL2=ON \
    -D FILAMENT_ENABLE_LTO=OFF \
    -D FILAMENT_USE_SWIFTSHADER=OFF \
    "

EXTRA_OECMAKE:class-target += " \
    -D BUILD_SHARED_LIBS=OFF \
    -D CMAKE_BUILD_TYPE=Release \
    -D CMAKE_BUILD_WITH_INSTALL_RPATH=ON \
    -D FILAMENT_LINUX_IS_MOBILE=ON \
    -D FILAMENT_BUILD_FILAMAT=ON \
    -D FILAMENT_SKIP_SAMPLES=ON \
    -D FILAMENT_SKIP_SDL2=ON \
    -D FILAMENT_ENABLE_LTO=OFF \
    -D FILAMENT_USE_SWIFTSHADER=OFF \
    -D DIST_ARCH=${BUILD_ARCH} \
    -D IMPORT_EXECUTABLES_DIR=. \
    -D FILAMENT_HOST_TOOLS_ROOT=${UNPACKDIR}/host_tools/bin \
    "

do_configure:prepend:class-target () {
    # extract host_tools.zip
    rm -rf ${UNPACKDIR}/host_tools | true
    unzip ${STAGING_DATADIR_NATIVE}/filament/host_tools.zip -d ${UNPACKDIR}

    # overwrite auto generated version
    cp ${UNPACKDIR}/ImportExecutables-Release.cmake ${S}/ImportExecutables-Release.cmake
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
    install -D ${STAGING_DATADIR_NATIVE}/filament/host_tools.zip ${D}${datadir}/filament/host_tools.zip

    rm ${D}/usr/LICENSE
    rm ${D}/usr/README.md

    # dependent recipe sets FILAMENT_HOST_TOOLS_ROOT to use
    install -Dm 644 ${UNPACKDIR}/ImportExecutables-Release.cmake \
        ${D}${includedir}/cmake/filament/ImportExecutables-Release.cmake

    install -d ${D}${libdir}/filament
    mv ${D}${libdir}/*/*.a ${D}${libdir}/filament
    rm -rf ${D}${libdir}/${BUILD_ARCH}
}

PACKAGES =+ "${PN}-host-tools"


INHIBIT_PACKAGE_DEBUG_SPLIT:class-target = "1"

FILES:${PN}-staticdev += "${libdir}"
INSANE_SKIP:${PN}-staticdev = "buildpaths"

FILES:${PN}-dev += "${includedir}"

FILES:${PN}-host-tools = "${datadir}"

BBCLASSEXTEND += "native nativesdk"
