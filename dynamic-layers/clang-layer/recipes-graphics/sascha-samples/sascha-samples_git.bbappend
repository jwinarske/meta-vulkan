
DEPENDS += " \
    compiler-rt \
    libcxx \
    openmp \
"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"
PREFERRED_PROVIDER_libgomp = "openmp"
LIBCPLUSPLUS = "-stdlib=libc++"
