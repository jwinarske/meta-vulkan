
DEPENDS += " \
    compiler-rt \
    libcxx \
    openmp \
"

TOOLCHAIN = "clang"
TC_CXX_RUNTIME = "llvm"
PREFERRED_PROVIDER_llvm = "clang"
PREFERRED_PROVIDER_llvm-native = "clang-native"
PREFERRED_PROVIDER_libgcc = "compiler-rt"
PREFERRED_PROVIDER_libgomp = "openmp"
LIBCPLUSPLUS = "-stdlib=libc++"
