require ${BPN}.inc
DRIDRIVERS ??= ""
DRIDRIVERS_append_x86_class-target = ",r100,r200,nouveau,i965"
DRIDRIVERS_append_x86-64_class-target = ",r100,r200,nouveau,i965"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER_libgcc = "compiler-rt"
