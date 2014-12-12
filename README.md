FITeagle 1 | Future Internet Testbed Experimentation and Management Framework 
=============================================================================

Remark: This is FITeagle in version 1. Initially developed to run in a
        simple servlet environment (jetty) it was now ported to an J2EE
        environment (WildFly). This version will not be developed further.

Prerequirements
---------------
You have an up and running WildFly environment. See
https://github.com/fiteagle/bootstrap/ for details.

Install
-------
mvn install -DskipTests && \
mvn wildfly:deploy -DskipTests

Testing
-------
 - Unit testing: ./src/main/bin/fiteaglectl test
 - Acceptance testing: cd acceptance-test && ./run.sh

Starting
--------
 - Jetty Version: ./src/main/bin/fiteaglectl start
 - WildFly Version: see above

Further Information
-------------------
For further information please have a look at [http://fiteagle.org](http://fiteagle.org).

FAQ
---
* Q: FITeagle tests seem to hang while testing cryptography methods on Linux
* A: Setup rng-tools (it should e.g. use /dev/urandom)
