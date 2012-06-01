SUMMARY = "SELinux library and simple utilities"
DESCRIPTION = "libselinux provides an API for SELinux applications to get and set \
process and file security contexts and to obtain security policy \
decisions.  Required for any applications that use the SELinux API."
SECTION = "base"
PR = "r1"
LICENSE = "Public Domain"
LIC_FILES_CHKSUM = "file://LICENSE;md5=84b4d2c6ef954a2d4081e775a270d0d0"
DEFAULT_PREFERENCE = "-1"

include selinux_git.inc
inherit lib_package

SRCREV = "339f8079d7b9dd1e0b0138e2d096dc7c60b2092e"
PV = "2.1.9+git${SRCPV}"

DEPENDS += "libsepol python python-native swig-native"

PACKAGES += "${PN}-python"
FILES_${PN}-python = "${libdir}/python${PYTHON_BASEVERSION}/site-packages/selinux/*"
FILES_${PN}-dbg += "${libdir}/python${PYTHON_BASEVERSION}/site-packages/selinux/.debug/*"

def get_git_policyconfigarch(d):
	import re
	target = d.getVar('TARGET_ARCH', True)
	p = re.compile('i.86')
	target = p.sub('i386',target)
	return "ARCH=%s" % (target)
EXTRA_OEMAKE += "${@get_git_policyconfigarch(d)}"

do_compile_append() {
    oe_runmake pywrap -j1 \
            INCLUDEDIR='${STAGING_INCDIR}' \
            LIBDIR='${STAGING_LIBDIR}' \
            PYLIBVER='python${PYTHON_BASEVERSION}' \
            PYINC='-I${STAGING_INCDIR}/$(PYLIBVER)' \
            PYLIB='-L${STAGING_LIBDIR}/$(PYLIBVER) -l$(PYLIBVER)' \
            PYTHONLIBDIR='${PYLIB}'
}

do_install_append() {
    oe_runmake install-pywrap swigify \
            DESTDIR=${D} \
            PYLIBVER='python${PYTHON_BASEVERSION}' \
            PYLIBDIR='${D}/${libdir}/$(PYLIBVER)'
}

BBCLASSEXTEND = "native"
