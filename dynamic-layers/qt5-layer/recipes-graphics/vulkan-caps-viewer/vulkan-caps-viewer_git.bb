SUMMARY = "Vulkan hardware capability viewer"
DESCRIPTION = "Client application to display hardware implementation " \
   "details for GPUs supporting the Vulkan API by Khronos."
AUTHOR = "Sascha Willems"
HOMEPAGE = "https://github.com/SaschaWillems/VulkanCapsViewer"
BUGTRACKER = "https://github.com/SaschaWillems/VulkanCapsViewer/issues"
SECTION = "graphics"
CVE_PRODUCT = ""
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0389d9d616b56125bd46fcb2cb900a8b"

DEPENDS += " \
    qtbase \
    vulkan-loader \
"

REQUIRED_DISTRO_FEATURES = "vulkan"

SRCREV = "5c8298e03c39b813f2610d4c879ce5d191ab29b9"

SRC_URI = "gitsm://github.com/SaschaWillems/VulkanCapsViewer.git;protocol=https;branch=master"

inherit qmake5 features_check pkgconfig

PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)} \
"

# resolve the most common collision automatically by preferring wayland. this
# really only removes the unnecessary runtime deps. it doesn't change linking
# options

PACKAGECONFIG:remove ??= " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'x11', '', d)} \
"

# all of the package config options below are mutually exclusive.
# this list shows the priority of the options from highest to lowest

PACKAGECONFIG[wayland] = "-DWAYLAND=ON,-DWAYLAND=OFF,wayland wayland-native wayland-protocols libxkbcommon"
PACKAGECONFIG[x11] = "-DX11=ON,-DX11=OFF,libxcb libx11 libxrandr"

do_install:append() {
    # Create a wrapper script to set font paths and Qt platform
    install -d ${D}${bindir}
    mv ${D}${bindir}/vulkanCapsViewer ${D}${bindir}/vulkanCapsViewer.bin
    
    cat > ${D}${bindir}/vulkanCapsViewer << 'EOF'
#!/bin/sh
# Set Qt platform
export QT_QPA_PLATFORM="${QT_QPA_PLATFORM:-wayland}"

# Ensure fontconfig cache is available
if [ ! -d /var/cache/fontconfig ]; then
    fc-cache -fv 2>/dev/null || true
fi

# Set font paths for Qt
export QT_QPA_FONTDIR="${QT_QPA_FONTDIR:-/usr/share/fonts/ttf}"

exec ${bindir}/vulkanCapsViewer.bin "$@"
EOF
    chmod +x ${D}${bindir}/vulkanCapsViewer
}

FILES:${PN} += " \
    ${datadir} \
"

RDEPENDS:${PN} += " \
    qtdeclarative \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland', '', d)} \
    fontconfig-utils \
    liberation-fonts \
"

BBCLASSEXTEND = ""
