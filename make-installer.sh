# clean out stuff from last installer creation
rm dist/kojo.zip
rm installer/Kojo-license.txt
rm -rf installer/bin/
rm -rf installer/etc/
rm -rf installer/ide/
rm -rf installer/kojo/
rm -rf installer/licenses/
rm -rf installer/platform/
rm -rf installer/puzzles/
rm -rf installer/icons/
rm -rf installer/stories/
rm installer/install.jar

# do a clean build
ant clean build-zip

# set up install dir structure
rm -rf installer/scratch/*
mkdir -p installer/scratch/unpack
unzip dist/kojo.zip -d installer/scratch/unpack
mv installer/scratch/unpack/kojo/* installer

cp kojo/Kojo-license.txt installer/
mkdir installer/licenses/
cp kojo/licenses/* installer/licenses/
mkdir installer/puzzles/
cp kojo/puzzles/puzzles.jar installer/puzzles/
cp kojo/etc/kojo.conf installer/etc/
mkdir installer/icons/
cp kojo/icons/* installer/icons/
mkdir installer/stories/
cp kojo/stories/* installer/stories/
mkdir installer/stories/music-loops/
cp kojo/stories/music-loops/* installer/stories/music-loops/
mkdir installer/kojo/initk
mkdir installer/kojo/libk


# remove test jars from install
# rm installer/kojo/modules/ext/cglib-nodep-2.1_3.jar
# rm installer/kojo/modules/ext/hamcrest-core-1.1.jar
# rm installer/kojo/modules/ext/hamcrest-library-1.1.jar
# rm installer/kojo/modules/ext/jmock-2.5.1.jar
# rm installer/kojo/modules/ext/jmock-junit4-2.5.1.jar
# rm installer/kojo/modules/ext/jmock-legacy-2.5.1.jar
# rm installer/kojo/modules/ext/objenesis-1.0.jar
# rm installer/kojo/modules/ext/Scalacheck-1.7-SNAPSHOT.jar

# remove test jar license files from install
rm installer/licenses/cglib-license.txt
rm installer/licenses/hamcrest-license.txt
rm installer/licenses/jmock-license.txt
rm installer/licenses/objenesis-license.txt
rm installer/licenses/scalacheck-license.txt

# remove jni native libs that came in with NB7
rm -rf installer/platform/modules/lib

# remove seemingly unused locale and auto-update jars
rm -rf installer/ide/modules/locale
rm -rf installer/ide/update
rm -rf installer/ide/update_tracking
rm -rf installer/platform/modules/locale
rm -rf installer/platform/update
rm -rf installer/platform/update_tracking

# run IzPack to create installer
cd installer
/home/lalit/IzPack/bin/compile install.xml

