# automatic-algorithm-selector
Automatic Algorithm Selection Using Case Based Reasoning

####  Dependency
`jColibri v2.1` 

#### Prerequisites
* download `jColibri v2.1` 
* create `lib` directory in `algoselector` and add `jColibri v2.1` inside e.g. `algoselector/lib/jcolibri2.jar`
* after that run the following command from `algoselector` directory
* `mvn install:install-file -Dfile=lib/jcolibri2.jar -DgroupId=jcolibri -DartifactId=jcolibri -Dversion=2.1 -Dpackaging=jar`

**Note**: The above Prerequisites should only be performed once, you don't need to follow the above steps with each build/run.

#### How to Build
* run `build.sh` for **Linux/Mac**
* run `build.bat` for **Windows**

**Note**: for the first time the build will take some time `(downloading artifacts)`, for build logs look at `build_log.log` file

#### How to Run
* run `algoselector/run.sh` for **Linux/Mac**
* run `algoselector/run.bat` for **Windows**

Now you are good to play with it :)
