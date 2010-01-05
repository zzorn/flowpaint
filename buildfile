# Buildr build script.

require 'buildr/scala' 

VERSION_NUMBER = "0.3-SNAPSHOT"
GROUP = "org.flowpaint"

# Maven 2.0 remote repositories:
repositories.remote << "http://www.ibiblio.org/maven2/"
repositories.remote << "http://repository.jboss.org/maven2/"
repositories.remote << "http://scala-tools.org/repo-releases/"

# Used libraries:
MIGLAYOUT = transitive('com.miglayout:miglayout:jar:3.6')
JANINO = transitive('janino:janino:jar:2.5.15')
TROVE = transitive('trove:trove:jar:2.1.1')
JPEN = artifact('jpen:jpen:jar:100101').from('lib/jpen-2.jar')
BROWSERLAUNCHER = artifact('browserlauncher2:browserlauncher2:jar:1.3').from('lib/BrowserLauncher2-1_3.jar')

desc "The Flowpaint project.  http://www.flowpaint.org"
define "flowpaint" do

  project.version = VERSION_NUMBER
  project.group = GROUP

  resources

  compile.with JPEN, MIGLAYOUT, JANINO, BROWSERLAUNCHER, TROVE
  
  package :jar

end
