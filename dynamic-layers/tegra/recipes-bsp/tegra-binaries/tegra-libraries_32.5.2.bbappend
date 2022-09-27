do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'true', 'false', d)}; then
        sed -i 's|libGLX_nvidia.so.0|/usr/lib/libnvvulkan-producer.so|g' ${D}/usr/lib/aarch64-linux-gnu/tegra/nvidia_icd.json
    fi
}
