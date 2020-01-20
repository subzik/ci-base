import jenkins.model.*
import hudson.model.*
import org.jfrog.*
import org.jfrog.hudson.*
import org.jfrog.hudson.util.Credentials;

def instance = Jenkins.getInstance()
def builder_desc = instance.getDescriptor(org.jfrog.hudson.ArtifactoryBuilder.class)

def deployerCredentials = new CredentialsConfig("admin", "password", "")
def resolverCredentials = new CredentialsConfig("", "", "")

def artifactory_inst = [ new ArtifactoryServer(
  "artifactory",
  "http://artifactory:8081/artifactory",
  deployerCredentials,
  resolverCredentials,
  300,
  false,
  3,
  1 )
]

builder_desc.setArtifactoryServers(artifactory_inst)
builder_desc.save()
instance.save()

println("Jenkins Artifactory plugin has been configured successfully.")