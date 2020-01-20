import jenkins.model.*
import hudson.plugins.*
def instance = Jenkins.getInstance()
def plugins.timestamper.TimestamperConfig timestamper_conf = instance.getDescriptor(plugins.timestamper.TimestamperConfig.class)
timestamper_conf.setAllPipelines(true)
instance.save()
println("Jenkins Timestamper plugin has been configured successfully.")