import jenkins.model.*

def pluginParameter="job-dsl timestamper sonar sonar-quality-gates workflow-aggregator git pipeline-stage-view github-branch-source ssh-slaves ws-cleanup artifactory"
def plugins = pluginParameter.split()
def instance = Jenkins.getInstance()
def pm = instance.getPluginManager()
def uc = instance.getUpdateCenter()
def installed = false

plugins.each {
  if (!pm.getPlugin(it)) {
    def plugin = uc.getPlugin(it)
    if (plugin) {
      println("Installing " + it)
      plugin.deploy()
	  sleep(10000)
      installed = true
    }
  }
}

instance.save()
//if (installed) {
println("All plugins have been installed successfully.")
println(plugins)
instance.doSafeRestart()
//}
