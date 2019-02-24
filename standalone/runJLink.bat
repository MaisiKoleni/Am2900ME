REM Set the Version that is present in ./target/ and then jlink uses the jmods of the folders jmods_..., where the JDK jmods AND OpenJavaFX jmods are located. JAVA_HOME must be set.
SET VERSION=0.1.1
SET OPTIONS= --add-modules am2900me --launcher am2900me=am2900me/net.maisikoleni.am2900me.ui.Main --strip-debug --compress 2 --no-header-files --no-man-pages 
"%JAVA_HOME%/bin/jlink" %OPTIONS% -p jmods_jdk11+ojfx_win;../target/Am2900ME-%VERSION%.jar --output distribution/am2900me_%VERSION%_win
"%JAVA_HOME%/bin/jlink" %OPTIONS% -p jmods_jdk11+ojfx_linux;../target/Am2900ME-%VERSION%.jar --output distribution/am2900me_%VERSION%_linux
"%JAVA_HOME%/bin/jlink" %OPTIONS% -p jmods_jdk11+ojfx_mac;../target/Am2900ME-%VERSION%.jar --output distribution/am2900me_%VERSION%_mac
